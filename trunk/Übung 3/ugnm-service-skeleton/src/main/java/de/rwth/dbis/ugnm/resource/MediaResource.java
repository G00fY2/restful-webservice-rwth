



package de.rwth.dbis.ugnm.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
        public JSONObject getAllMedium() {

                
                List<Medium> media = mediumService.getAll();
                Iterator<Medium> mit = media.iterator();
                
                Vector<String> vMedia = new Vector<String>();   
                
                while(mit.hasNext()){
                        Medium m = mit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + m.getURL();
                        vMedia.add(uUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("Media",vMedia);
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(500);
                }
        }

}

