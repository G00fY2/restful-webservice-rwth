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
        
        @GET
        @Produces("application/json")
        public Achievement getAchievement(@PathParam("id") int id) {
                Achievement a = achievementService.getById(id);
                if (a==null){
                        throw new WebApplicationException(404);
                }
                return a;
        }
        
        @PUT
    @Consumes("application/json")
    public Response updateAchievement(@PathParam("id") int id, JSONObject o) throws JSONException {
                //Get the User By Mail, if null then throw not found exception
                Achievement a = achievementService.getById(id);
                if(a != null){
                        //build the uri for response and parse the JSON File
                        // if successfull update the achievement, else throw errors
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
                else{
                        throw new WebApplicationException(404);
                }
    }
        
        @DELETE
        public Response deleteAchievement(@PathParam("id") int id){
                //Get the Achievement By Id, if null then throw not found exception
                Achievement achievement = achievementService.getById(id);
                if(achievement!=null){
                        //if successfully deleted the medium give OK-Response, else throw execptions
                        achievementService.delete(achievement);
                        return Response.ok().build();
                }
                else{
                        throw new WebApplicationException(404);
                }
        }
        
        //parse the JSON File for the attributes, throw exception if not correcly formated
                private Achievement parseJsonUpdateFile(JSONObject o, int id){
                        try {
                                String description = o.getString("description");
                                String name = o.getString("name");
                                String url = o.getString("url");
                                Achievement achievement = new Achievement();
                                achievement.setId(id);
                                achievement.setName(name);
                                achievement.setDescription(description);
                                achievement.setUrl(url);
                                return achievement;
                        } catch (JSONException e) {
                                throw new WebApplicationException(409);
                        }
                }
}
