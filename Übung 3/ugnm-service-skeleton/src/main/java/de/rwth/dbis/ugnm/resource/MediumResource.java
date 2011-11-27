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

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;

@Path("/media/{url}")
@Component
@Scope("request")
public class MediumResource {

        @Autowired
        MediumService mediumService;
        
        @Context UriInfo uriInfo;
        
        @GET
        @Produces("application/json")
        public Medium getMedium(@PathParam("url") String url){
                Medium m = mediumService.getByUrl(url);
                if (m==null){
                        throw new WebApplicationException(404);
                }
                return m;
        }
        
        //This updates a existing medium
        @PUT
    @Consumes("application/json")
    public Response updateMedium(@PathParam("url") String url, JSONObject o) throws JSONException {
                //Get the Medium by URL, if null then throw not found exception
                Medium m = mediumService.getByUrl(url);
                if(m != null){
                        //build the uri for response and parse the JSON File
                        // if successfull update the medium, else throw errors
                                Medium medium = parseJsonUpdateFile(o, url);
                                mediumService.update(medium);
                                UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                                URI mediumUri = ub.path(medium.getUrl()).build();
                                return Response.created(mediumUri).build();
                }
                else{
                        throw new WebApplicationException(404);
                }
    }
        
        @DELETE
        public Response deleteMedium(@PathParam("url") String url){
                //Get the Medium by URL, if null then throw not found exception
                Medium medium = mediumService.getByUrl(url);
                if(medium!=null){
                        //if successfully deleted the medium give OK-Response, else throw execptions
                        mediumService.delete(medium);
                        return Response.ok().build();
                }
                else{
                        throw new WebApplicationException(404);
                }
        }
        
        //parse the JSON File for the attributes, throw exception if not correcly formated
                private Medium parseJsonUpdateFile(JSONObject o, String url){
                        try {
                                int value = o.getInt("value");
                                String description = o.getString("description");
                                Medium medium = new Medium();
                                medium.setValue(value);
                                medium.setDescription(description);
                                medium.setUrl(url);
                                return medium;
                        } catch (JSONException e) {
                                throw new WebApplicationException(406);
                        }
                }
                
}
