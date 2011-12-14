package de.rwth.dbis.ugnm.resource;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
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

import de.rwth.dbis.ugnm.entity.Collect;
import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.CollectService;
import de.rwth.dbis.ugnm.service.AchievementService;
import de.rwth.dbis.ugnm.service.UserService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/users/{email}/collect")
@Component
public class CollectsResource {


        @Autowired
        CollectService collectService;
        
        @Autowired
        AchievementService achievementService;
        
        @Autowired
        UserService userService;
        
        @Context UriInfo uriInfo;

    	private String _corsHeaders;
    	
    	// each resource should have this method annotated with OPTIONS. This is needed for CORS.
    	@OPTIONS
    	public Response corsResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
    		_corsHeaders = requestH;
    		return CORS.makeCORS(Response.ok(), requestH);
    	}
//Gibt ueber GET ein Liste aller Collects aus
        
        @GET
        @Produces("application/json")
        public Response getAllCollects(@PathParam("email") String email) {

                List<Collect> collectList = collectService.getAllAchievementsOfUser(email);
                Iterator<Collect> cit = collectList.iterator();
                
                Vector<String> vCollect = new Vector<String>();      
                
                while(cit.hasNext()){
                        Collect c = cit.next();
                        String rUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + c.getId();
                        vCollect.add(rUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("collect",vCollect);
                        Response.ResponseBuilder r = Response.ok(j);
                        return CORS.makeCORS(r, _corsHeaders);
                } catch (JSONException e) {
                	Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }
        
        
        
//Ermoeglicht ueber PUT das Erstellen eines einzelnen Collects 
        
        @PUT
    @Consumes("application/json")
    public Response createCollect(@HeaderParam("authorization") String auth, @PathParam("email") String email, JSONObject o) throws JSONException{
        	
//Collect-Objekt wird mit uebergebenen Parametern erzeugt 
        	
                Collect collect = parseCollectJsonFile(o, email);
                                
//Methode ueberprueft ob Collect existiert
                
                if(achievementService.getById(collect.getAchievementId()) != null){
                        if(authenticated(auth, userService.getByEmail(email))){
                                collectService.save(collect);
                                UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                                URI collectUri = ub.path(collect.getUserEmail()).build();
                                Response.ResponseBuilder r = Response.created(collectUri);
                                return CORS.makeCORS(r, _corsHeaders);
                        }
                        else{
                            	Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                                return CORS.makeCORS(r, _corsHeaders);
                        }
                        
                }
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_ACCEPTABLE);
                    return CORS.makeCORS(r, _corsHeaders);
                }
    }
        
//Parst die fuer Collect noetigen Attribute in Json   

        private Collect parseCollectJsonFile(JSONObject o, String email){

                try {
                        int achievementId = o.getInt("achievementId");
                        Collect collect = new Collect();   
                        collect.setAchievementId(achievementId);
                        collect.setUserEmail(email);
                        
                        return collect;
                } catch (JSONException e) {
                        return null;
                }
                
        }
        
        
        // Little gift from your tutors...
        // A simple authentication mechanism;
        // For use in one of the defined operations by referring 
        // to @HeaderParam("authorization") for authHeader.
        private boolean authenticated(String authHeader,User u){
                if(authHeader != null){
                        String[] dauth = null;
                        String authkey = authHeader.split(" ")[1];
                        if(Base64.isBase64(authkey)){
                                dauth = (new String(Base64.decode(authkey))).split(":");
                                if(dauth[0].equals(u.getEmail()) && dauth[1].equals(u.getPassword() )) {
                                        return true;
                                }
                        }
                }
                return false;
        }
        
}