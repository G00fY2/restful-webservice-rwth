package de.rwth.dbis.ugnm.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.rwth.dbis.ugnm.entity.Achievement;
import de.rwth.dbis.ugnm.service.AchievementService;

@Path("/achievements/{id}")
@Component
@Scope("request")
public class AchievementResource {

        @Autowired
        AchievementService achievementService;
        
        @Context UriInfo uriInfo;
        
//Gibt ueber GET ein einzelnes Achievement aus
//GET Achievement ueber Primary id
        
        @GET
        @Produces("application/json")
        public Achievement getAchievement(@PathParam("id") int id) {
                Achievement a = achievementService.getById(id);
                if (a==null){
                        throw new WebApplicationException(404);
                }
                return a;
        }
        
//Ermöglicht ueber PUT das aendern eines einzelnen Achievements          
        
        @PUT
    @Consumes("application/json")
    public Response updateAchievement(@PathParam("id") int id, JSONObject o) throws JSONException {
        	
//GET Achievement über Primary Id   
        	
                Achievement a = achievementService.getById(id);
                
//Wenn Achievement nicht "null" ist wird das Achievement geupdated und ein created-Response abgesetzt                
                
                if(a != null){
                        Achievement achievement = parseJsonUpdateFile(o, id);
                        if(a!=achievement){
                                achievementService.update(achievement);
                                UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                                String relativePath = ""+achievement.getId();
                                URI achievementUri = ub.path(relativePath).build();
                                return Response.created(achievementUri).build();
                        }
                        else{
                                return Response.notModified().build();
                        }
                }
                
//Andernfalls wird eine 404 WebApplicationException geschmissen                 
                
                else{
                        throw new WebApplicationException(404);
                }
    }
        
//Ermöglicht ueber DELETE das loeschen eines einzelnen Achievements 
        
        @DELETE
        public Response deleteAchievement(@PathParam("id") int id){

//GET Achievement über Primary Id        	

                Achievement achievement = achievementService.getById(id);
                
//Wenn Achievement nicht "null" ist wird das Achievement gelöscht und ein ok-Response abgesetzt
                
                if(achievement!=null){
                        achievementService.delete(achievement);
                        return Response.ok().build();
                }
                
//Andernfalls wird eine 404 WebApplicationException geschmissen                
                
                else{
                        throw new WebApplicationException(404);
                }
        }
        
        
 //Parst die fuer Achievement nötigen Attribute in Json       
        
        
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
                                throw new WebApplicationException(409);
                        }
                }
}
