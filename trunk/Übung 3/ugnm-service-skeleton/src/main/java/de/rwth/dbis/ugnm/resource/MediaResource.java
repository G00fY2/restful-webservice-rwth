package de.rwth.dbis.ugnm.resource;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;

@Path("/media")
@Component
@Scope("request")
public class MediaResource {
        
        
        

        @Autowired
        MediumService mediumService;

        @Context UriInfo uriInfo;

        //Gives out the Path to every Medium Object
        @GET
        @Produces("application/json")
        public JSONObject getAllMedia() {

                
                List<Medium> media = mediumService.getAll();
                Iterator<Medium> mit = media.iterator();
                
                Vector<String> vMedia = new Vector<String>();   
                
                while(mit.hasNext()){
                        Medium m = mit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + m.getUrl();
                        vMedia.add(uUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("media",vMedia);
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(500);
                }
        }
        
        
        //This creates a new Medium
        @PUT
    @Consumes("application/json")
    public Response createMedium(JSONObject o) throws JSONException {
                //Create a new Medium..
                Medium medium = parseMediumJsonFile(o);
                //call this method to make sure no double entries are inserted
                return addIfDoesNotExist(medium);
    }
        
        private Response addIfDoesNotExist(Medium medium) {
                if(mediumService.findMedium(medium) == null) {
                        mediumService.save(medium);
                        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                        URI mediumUri = ub.path(medium.getUrl()).build();
                        return Response.created(mediumUri).build();
                }
                else{
                        throw new WebApplicationException(409);
                }
        }
        
        //parse the JSON File for the attributes
        private Medium parseMediumJsonFile(JSONObject o){

                try {
                        String url = o.getString("url");
                        int value = o.getInt("value");
                        String description = o.getString("description");
                        Medium medium = new Medium();
                        medium.setUrl(url);
                        medium.setValue(value);
                        medium.setDescription(description);
                        return medium;
                } catch (JSONException e) {
                        throw new WebApplicationException(406);
                }
                

                
        }
}