package de.rwth.dbis.ugnm.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.UserService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/users")
@Component
public class UsersResource {

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
   
//Methode gibt eine Liste aller User aus
   
        @GET
        @Produces("application/json")
   
        public Response getUsers() {
   
//Liste wird erstellt    
        	
                List<User> users = userService.getAll();
                
//Iterator wird erstellt
                
    Iterator<User> usit = users.iterator();
    
//String-Array wird erstellt
    
    Vector<String> vUsers = new Vector<String>();
                while(usit.hasNext()){
                        User u = usit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + u.getEmail();
                        vUsers.add(uUri);
                }
                JSONObject j = new JSONObject();
//Liste wird ausgegeben
                try {
                	
                	j.append("users", vUsers);
                	Response.ResponseBuilder r = Response.ok(j);
                    return CORS.makeCORS(r, _corsHeaders);		
                } catch (JSONException e) {
        			Response.ResponseBuilder r = Response.serverError();
        			return CORS.makeCORS(r, _corsHeaders);
        		}

        	}
        
//Erstellt einen User und fügt diesen mittels PUT hinzu
        
          @PUT
          @Consumes("application/json")  
          public Response putUser(JSONObject o) throws JSONException {
        
                if(o == null || !(o.has("email") && o.has("username") && o.has("password") && o.has("name"))){
                	Response.ResponseBuilder r = Response.status(Status.NOT_ACCEPTABLE);
                    return CORS.makeCORS(r, _corsHeaders);
                }
                else{
        			User nu = new User();
        			nu.setEmail((String) o.get("email"));
        			nu.setUsername((String) o.get("username"));
        			nu.setPassword((String) o.get("password"));
        			nu.setName((String) o.get("name"));
        			nu.setEp(0);

        			if(userService.findUser(nu) == null) {
        				userService.save(nu);
        				URI location;
        				try {
        					location = new URI(uriInfo.getAbsolutePath().toASCIIString() + "/" + o.get("email"));
        					
        					Response.ResponseBuilder r = Response.created(location);
        					return CORS.makeCORS(r, _corsHeaders);
        					
        				} catch (URISyntaxException e) {
        					Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
        					return CORS.makeCORS(r, _corsHeaders);
        				}
        			}
        			else{
        				Response.ResponseBuilder r = Response.status(Status.CONFLICT);
        				return CORS.makeCORS(r, _corsHeaders);
        			}
        		}
          }

}