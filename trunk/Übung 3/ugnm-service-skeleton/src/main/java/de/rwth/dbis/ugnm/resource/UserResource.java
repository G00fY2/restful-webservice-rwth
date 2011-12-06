package de.rwth.dbis.ugnm.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.UserService;

@Path("/users/{email}")
@Component
public class UserResource {

        @Autowired
        UserService userService;

        @Context UriInfo uriInfo;
        
//Gibt ueber GET einen einzelnen User aus
//GET User ueber Primary email 
   
        @GET
        @Produces("application/json")
        public User getUser(@PathParam("email") String email) {
        	
//User-Objekt wird mit uebergebenen Parametern erzeugt
        	
                User u = userService.getByEmail(email);
                
//Wenn User-Object nicht = "null" wird das password auf null gesetzt und dieses Object ausgegeben               
//Andernfalls wird eine 404 WebApplicationException geschmissen
                
                if (u!=null){
             u.setPassword(null);
                }
                else{
             throw new WebApplicationException(404);
                }
                return u;
}


//Ermöglicht ueber PUT das aendern eines einzelnen Users 
        
        @PUT
    @Consumes("application/json")
    public Response updateUser(@HeaderParam("authorization") String auth, @PathParam("email") String email, JSONObject o) throws JSONException {

//Wenn übergebener Nutzer (als Json-Object) = null -> 404 WebApplicationException
        	
                if(o == null){
                        throw new WebApplicationException(400);
                }

//Wenn länge=0 wurde keine änderung vorgenommen -> notModified Response                 
                
                if(o.length() == 0){
                         return Response.notModified().build();
                }

//GET User ueber Primary email            
                
                User u = userService.getByEmail(email);
                
//Wenn User-Object nicht = "null" wird dieses ausgegeben
//Andernfalls wird eine 404 WebApplicationException geschmissen                  
                
                if(u == null){
                        throw new WebApplicationException(404);
                }
                
//Wenn User-Object nicht authenticated wird eine 401 WebApplicationException geschmissen                 
                
                if(!authenticated(auth, u)){
                        throw new WebApplicationException(401);
                }               
                boolean changed = false;
               
//Wenn User authenticated über email und password wird der User geändert                
                
        if (o.has("email") && !o.getString("email").equals(u.getEmail())){
                u.setUsername(o.getString("email"));
                        changed = true;
                }
        if (o.has("password") && !o.getString("password").equals(u.getPassword())){
                u.setPassword(o.getString("password"));
                        changed = true;
                }
        
                if(changed){
                        userService.update(u);
                        return Response.ok().build();
                } else {
                        return Response.notModified().build();
                }
    }

//Ermöglicht ueber DELETE das loeschen eines einzelnen Users 
        
        @DELETE
        public Response deleteUser(@HeaderParam("authorization") String auth, @PathParam("email") String email){

//GET User ueber Primary email        	
        	
                User u = userService.getByEmail(email);
                
//Wenn Achievement nicht "null" und user authenticated ist wird das Achievement gelöscht und ein ok-Response abgesetzt                
                
                if(u == null){
                        throw new WebApplicationException(404);
                }
                if(!authenticated(auth, u)){
                        throw new WebApplicationException(401);
                }
                userService.delete(u);
                
                return Response.ok().build();
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

