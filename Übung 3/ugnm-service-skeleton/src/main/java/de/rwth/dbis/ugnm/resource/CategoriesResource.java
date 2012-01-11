package de.rwth.dbis.ugnm.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/media/cat{id}")
@Component
public class CategoriesResource {
        
        @Autowired
        MediumService mediumService;

        
        @Context UriInfo uriInfo;

    	private String _corsHeaders;
    	
    	// each resource should have this method annotated with OPTIONS. This is needed for CORS.
    	@OPTIONS
    	public Response corsResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
    		_corsHeaders = requestH;
    		return CORS.makeCORS(Response.ok(), requestH);
    	}
    	
//Gibt ueber GET ein Liste aller Medien aus        
        
        @GET
        @Produces("application/json")
        public Response getMediaCat(@PathParam("id") int id) { 
         
        	if(id == 1){
        	 List<Medium> media = mediumService.getMediaCat1();
             Iterator<Medium> mit = media.iterator();
             
             Vector<String> vMedia = new Vector<String>();  
                while(mit.hasNext()){
                        Medium m = mit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + m.getId();
                        vMedia.add(uUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.put("media",vMedia);
                        Response.ResponseBuilder r = Response.ok(j);
                        return CORS.makeCORS(r, _corsHeaders);
                } catch (JSONException e) {
                	Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                    return CORS.makeCORS(r, _corsHeaders);
                }
         }
        	
        	if(id == 2){
           	 List<Medium> media = mediumService.getMediaCat2();
                Iterator<Medium> mit = media.iterator();
                
                Vector<String> vMedia = new Vector<String>();  
                   while(mit.hasNext()){
                           Medium m = mit.next();
                           String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + m.getId();
                           vMedia.add(uUri);
                   }

                   try {
                           JSONObject j = new JSONObject();
                           j.put("media",vMedia);
                           Response.ResponseBuilder r = Response.ok(j);
                           return CORS.makeCORS(r, _corsHeaders);
                   } catch (JSONException e) {
                   	Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                       return CORS.makeCORS(r, _corsHeaders);
                   }
            }
        	
        	if(id == 3){
           	 List<Medium> media = mediumService.getMediaCat3();
                Iterator<Medium> mit = media.iterator();
                
                Vector<String> vMedia = new Vector<String>();  
                   while(mit.hasNext()){
                           Medium m = mit.next();
                           String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + m.getId();
                           vMedia.add(uUri);
                   }

                   try {
                           JSONObject j = new JSONObject();
                           j.put("media",vMedia);
                           Response.ResponseBuilder r = Response.ok(j);
                           return CORS.makeCORS(r, _corsHeaders);
                   } catch (JSONException e) {
                   	Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                       return CORS.makeCORS(r, _corsHeaders);
                   }
            }
        	else{
        		Response.ResponseBuilder r = Response.status(Status.BAD_REQUEST);
                return CORS.makeCORS(r, _corsHeaders);
        	}
        
        
        
        }
        
        
}