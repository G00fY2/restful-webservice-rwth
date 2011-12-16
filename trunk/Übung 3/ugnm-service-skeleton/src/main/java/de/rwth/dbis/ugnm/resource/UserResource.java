package de.rwth.dbis.ugnm.resource;

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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.UserService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/users/{email}")
@Component
public class UserResource {

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
    	
//Gibt ueber GET einen einzelnen User aus
//GET User ueber Primary email 
   
        @GET
        @Produces("application/json")
        public Response getUser(@PathParam("email") String email) {
        	
//User-Objekt wird mit uebergebenen Parametern erzeugt
        	
                User u = userService.getByEmail(email);
                
//Wenn User-Object nicht = "null" wird das password auf null gesetzt und dieses Object ausgegeben               
//Andernfalls wird eine 404 WebApplicationException geschmissen
                
                if (u!=null){
                	u.setPassword(null);
                }
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
                Response.ResponseBuilder r = Response.ok(u);
                return CORS.makeCORS(r, _corsHeaders);
}


//Ermöglicht ueber PUT das aendern eines einzelnen Users 
        
        @PUT
    @Consumes("application/json")
    public Response updateUser(@HeaderParam("authorization") String auth, @PathParam("email") String email, JSONObject o) throws JSONException {

//Wenn übergebener Nutzer (als Json-Object) = null -> 404 WebApplicationException
        	
                if(o == null){
                	Response.ResponseBuilder r = Response.status(Status.NOT_ACCEPTABLE);
                    return CORS.makeCORS(r, _corsHeaders);
                }

//Wenn länge=0 wurde keine änderung vorgenommen -> notModified Response                 
                
                if(o.length() == 0){
                	Response.ResponseBuilder r = Response.status(Status.NOT_MODIFIED);
                	return CORS.makeCORS(r, _corsHeaders);
                }

//GET User ueber Primary email            
                
                User u = userService.getByEmail(email);
                
//Wenn User-Object nicht = "null" wird dieses ausgegeben
//Andernfalls wird eine 404 WebApplicationException geschmissen                  
                
                if(u == null){
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
            }
//Wenn User-Object nicht authenticated wird eine 401 WebApplicationException geschmissen                 
                
                if(!authenticated(auth, u)){
                	 Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                     return CORS.makeCORS(r, _corsHeaders);
                }               
                boolean changed = false;
               
//Wenn User authenticated über email und password wird der User geändert                
                
        if (o.has("username") && !o.getString("username").equals(u.getUsername())){
                u.setUsername(o.getString("username"));
                changed = true;
                }
        if (o.has("password") && !o.getString("password").equals(u.getPassword())){
                u.setPassword(o.getString("password"));
                        changed = true;
                }
        
                if(changed){
                	   Response.ResponseBuilder r = Response.status(Status.CREATED);
                       return CORS.makeCORS(r, _corsHeaders);
                } else {
                	Response.ResponseBuilder r = Response.status(Status.NOT_MODIFIED);
                    return CORS.makeCORS(r, _corsHeaders);
                }
    }

//Ermöglicht ueber DELETE das loeschen eines einzelnen Users 
        
        @DELETE
        public Response deleteUser(@HeaderParam("authorization") String auth, @PathParam("email") String email){

//GET User ueber Primary email        	
        	
                User u = userService.getByEmail(email);
                
//Wenn Achievement nicht "null" und user authenticated ist wird das Achievement gelöscht und ein ok-Response abgesetzt                
                
                if(u == null){
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
                if(!authenticated(auth, u)){
                	 Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                     return CORS.makeCORS(r, _corsHeaders);
                }
                userService.delete(u);
                
                Response.ResponseBuilder r = Response.status(Status.OK);
                return CORS.makeCORS(r, _corsHeaders);
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
                                if(dauth[0].equals(u.getEmail()) && dauth[1].equals(u.getPassword())){
                                        return true;
                                }
                        }
                }
                return false;
        }
}

