package de.rwth.dbis.ugnm.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.Achievement;
import de.rwth.dbis.ugnm.service.AchievementService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/achievements/{id}")
@Component
public class AchievementResource {

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
        
//Gibt ueber GET ein einzelnes Achievement aus
//GET Achievement ueber Primary id
       
        @GET
        @Produces("application/json")
        public Response getAchievement(@PathParam("id") int id) {
   
//Achievement-Objekt wird mit uebergebenen Parametern erzeugt
               
                Achievement a = achievementService.getById(id);
               
//Wenn Achievement-Object nicht = "null" wird dieses ausgegeben
//Andernfalls wird eine 404 WebApplicationException geschmissen              
               
               
                if (a==null){
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
                Response.ResponseBuilder r = Response.ok(a);
                return CORS.makeCORS(r, _corsHeaders);
        }
       
//Ermöglicht ueber PUT das aendern eines einzelnen Achievements          
       
        @PUT
    @Consumes("application/json")
    public Response updateAchievement(@HeaderParam("authorization") String auth, @PathParam("id") int id, JSONObject o) throws JSONException {
        	 	if(admin_authenticated(auth)==false){
        	 		Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                    return CORS.makeCORS(r, _corsHeaders);
        	 	}
//GET Achievement ueber Primary id  
               
                Achievement a = achievementService.getById(id);
               
//Wenn Achievement nicht "null" ist wird das Achievement geupdated und ein created-Response abgesetzt                
               
                if(a != null){
                        Achievement achievement = parseJsonUpdateFile(o, id);
                        if(a!=achievement){
                                achievementService.update(achievement);
                                UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                                String relativePath = ""+achievement.getId();
                                URI achievementUri = ub.path(relativePath).build();
                                Response.ResponseBuilder r = Response.ok(achievementUri);
                                return CORS.makeCORS(r, _corsHeaders);
                        }
                        else{
                        	 Response.ResponseBuilder r = Response.status(Status.NOT_MODIFIED);
                             return CORS.makeCORS(r, _corsHeaders);
                        }
                }
               
//Andernfalls wird eine 404 WebApplicationException geschmissen                
               
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
    }
       
//Ermöglicht ueber DELETE das loeschen eines einzelnen Achievements
       
        @DELETE
        public Response deleteAchievement(@HeaderParam("authorization") String auth, @PathParam("id") int id){
                        if(admin_authenticated(auth)==false){
                        	Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                            return CORS.makeCORS(r, _corsHeaders);
                        }
//GET Achievement ueber Primary Id              

                Achievement achievement = achievementService.getById(id);
               
//Wenn Achievement nicht "null" ist wird das Achievement gelöscht und ein ok-Response abgesetzt
               
                if(achievement!=null){
                        achievementService.delete(achievement);
                        return Response.ok().build();
                }
               
//Andernfalls wird eine 404 WebApplicationException geschmissen                
               
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }
       
       
 //Parst die fuer Achievement noetigen Attribute in Json      
       
       
                private Achievement parseJsonUpdateFile(JSONObject o, int id){
                        try {
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

