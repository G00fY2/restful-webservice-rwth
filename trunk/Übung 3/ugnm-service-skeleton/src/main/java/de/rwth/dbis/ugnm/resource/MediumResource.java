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

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/media/{id}")
@Component
public class MediumResource {

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
    	
//Gibt ueber GET ein einzelnes Medium aus
//GET Medium ueber Primary id
        
        @GET
        @Produces("application/json")
        public Response getMedium(@PathParam("id") int id){
                Medium m = mediumService.getById(id);
                if (m==null){
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
                Response.ResponseBuilder r = Response.ok(m);
                return CORS.makeCORS(r, _corsHeaders);
        }
      
        
//Ermoeglicht ueber PUT das aendern eines einzelnen Mediums 
        
        
        @PUT
    @Consumes("application/json")
    public Response updateMedium(@HeaderParam("authorization") String auth, @PathParam("id") int id, JSONObject o) throws JSONException {
        		if(admin_authenticated(auth)==false){
        			Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                    return CORS.makeCORS(r, _corsHeaders);
        		}

            	if(o == null || !(o.has("url") && o.has("value") && o.has("description") && o.has("tag"))){
                    Response.ResponseBuilder r = Response.status(Status.NOT_ACCEPTABLE);
                    return CORS.makeCORS(r, _corsHeaders);
                }
            	
            	//GET Medium ueber Primary id
                Medium m = mediumService.getById(id);

//Wenn Achievement nicht "null" ist wird das Medium geupdated und ein created-Response abgesetzt                 
                
                if(m != null){

                                Medium medium = parseJsonUpdateFile(o, id);
                                mediumService.update(medium);
                                UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                                URI mediumUri = ub.path(medium.getId()+"").build();
                                Response.ResponseBuilder r = Response.created(mediumUri);
                                return CORS.makeCORS(r, _corsHeaders);
                }

//Andernfalls wird eine 404 WebApplicationException geschmissen
                
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
    }
        
//Ermoeglicht ueber DELETE das loeschen eines einzelnen Mediums 
        
        @DELETE
        public Response deleteMedium(@HeaderParam("authorization") String auth, @PathParam("id") int id){
        		if(admin_authenticated(auth)==false){
        			Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                    return CORS.makeCORS(r, _corsHeaders);
        		}
//GET Medium ueber Primary id        	
        	
                Medium medium = mediumService.getById(id);
                if(medium!=null){
                	
//Wenn Medium nicht "null" ist wird das Medium geloescht und ein ok-Response abgesetzt
                	
                        mediumService.delete(medium);
                        Response.ResponseBuilder r = Response.status(Status.OK);
                        return CORS.makeCORS(r, _corsHeaders);	
                }
                
//Andernfalls wird eine 404 WebApplicationException geschmissen             
                
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }
        
//Parst die fuer Achievement noetigen Attribute in Json   
        
                private Medium parseJsonUpdateFile(JSONObject o, int id){
                        try {
                                int value = o.getInt("value");
                                String description = o.getString("description");
                                String url = o.getString("url");
                                String tag = o.getString("tag");
                                Medium medium = new Medium();
                                medium.setValue(value);
                                medium.setDescription(description);
                                medium.setTag(tag);
                                medium.setUrl(url);
                                medium.setId(id);
                                return medium;
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
