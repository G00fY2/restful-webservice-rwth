package de.rwth.dbis.ugnm.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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

@Path("/users/{email}/collect")
@Component
//@Scope("request")
public class CollectsResource {


        @Autowired
        CollectService collectService;
        
        @Autowired
        AchievementService achievementService;
        
        @Autowired
        UserService userService;
        
        @Context UriInfo uriInfo;

//Gibt ueber GET ein Liste aller Achievements aus
        
        @GET
        @Produces("application/json")
        public JSONObject getAllCollects(@PathParam("email") String email) {

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
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(500);
                }
        }
        
        
        
//Ermoeglicht ueber PUT das Erstellen eines einzelnen Achievements 
        
        @PUT
    @Consumes("application/json")
    public Response createCollect(@HeaderParam("authorization") String auth, @PathParam("email") String email, JSONObject o) throws JSONException{
        	
//Achievement-Objekt wird mit uebergebenen Parametern erzeugt 
        	
                Collect collect = parseCollectJsonFile(o, email);
                                
//Methode ueberprueft ob Achievement existiert
                
                if(achievementService.getById(collect.getAchievementId()) != null){
                        if(authenticated(auth, userService.getByEmail(email))){
                                collectService.save(collect);
                                return Response.ok().build();
                        }
                        else{
                                throw new WebApplicationException(401);
                        }
                        
                }
                else{
                        throw new WebApplicationException(406);
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
                        throw new WebApplicationException(406);
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