package de.rwth.dbis.ugnm.resource;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.Achievement;
import de.rwth.dbis.ugnm.service.AchievementService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/achievements")
@Component
public class AchievementsResource {
        
        
        

        @Autowired
        AchievementService achievementService;

        @Context UriInfo uriInfo;

    	private String _corsHeaders;
    	
    	// each resource should have this method annotated with OPTIONS. This is needed for CORS.
    	@OPTIONS
    	public Response corsResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
    		_corsHeaders = requestH;
    		return CORS.makeCORS(Response.ok(), requestH);
    	}
    	
//Gibt ueber GET ein Liste aller Achievement aus
             
        @GET
        @Produces("application/json")
        public Response getAchievements() {

                List<Achievement> achievements = achievementService.getAll();
                Iterator<Achievement> ait = achievements.iterator();
                
                Vector<String> vAchievements = new Vector<String>();    
                
                while(ait.hasNext()){
                        Achievement a = ait.next();
                        String aUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + a.getId();
                        vAchievements.add(aUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("achievements",vAchievements);
                        Response.ResponseBuilder r = Response.ok(j);
                        return CORS.makeCORS(r, _corsHeaders);
                } catch (JSONException e) {
                	Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }
        
        
//Ermöglicht ueber PUT das Erstellen eines einzelnen Achievements  
        
        
        @PUT
    @Consumes("application/json")
    public Response createAchievement(@HeaderParam("authorization") String auth, JSONObject o) throws JSONException {
        	if(admin_authenticated(auth)==false){
        		Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                return CORS.makeCORS(r, _corsHeaders);
        	}
        	
            //Falls neues Achievement nicht alle Daten hat gibt es einen 400 Error
            
        	if(o == null || !(o.has("id") && o.has("description") && o.has("name"))){
                Response.ResponseBuilder r = Response.status(Status.BAD_REQUEST);
                return CORS.makeCORS(r, _corsHeaders);
            }
        	
//Achievement-Objekt wird mit uebergebenen Parametern erzeugt 
        	
                Achievement achievement = parseAchievementJsonFile(o);
                
//Methode überprüft ob Achievement bereits existiert
                
                return addIfDoesNotExist(achievement);
    }
        
        private Response addIfDoesNotExist(Achievement achievement) {
                if(achievementService.findAchievement(achievement) == null) {
                        achievementService.save(achievement);
                        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                        String relativePath = ""+achievement.getId();
                        URI achievementUri = ub.path(relativePath).build();
                        Response.ResponseBuilder r = Response.created(achievementUri);
                        return CORS.makeCORS(r, _corsHeaders);
                }
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_ACCEPTABLE);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }
        

        //Parst die fuer Achievements nötigen Attribute in Json           
        
        private Achievement parseAchievementJsonFile(JSONObject o){
            try {
            	    int id = o.getInt("id");
                    String description = o.getString("description");
                    String name = o.getString("name");
                    String url = o.getString("url");
                    Achievement achievement = new Achievement();
                    achievement.setId(id);
                    achievement.setDescription(description);
                    achievement.setName(name);
                    achievement.setUrl(url);
                    return achievement;
            } catch (JSONException e) {
                    return null;
            }
                
        }
        
        // Admin Authentifizierung-Methode
        public static boolean admin_authenticated(String authHeader){
            String adminEmail = "sven.hausburg@rwth-aachen.de";
            String adminPw = "abc123";
        if(authHeader != null){
                String[] dauth = null;
                String authkey = authHeader.split(" ")[1];
                if(Base64.isBase64(authkey)){
                        dauth = (new String(Base64.decode(authkey))).split(":");
                        if((dauth[0].toLowerCase().equals(adminEmail)) && dauth[1].equals(adminPw)){
                                return true;
                        }
                }
        }
        return false;
        }
}