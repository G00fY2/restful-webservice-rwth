package de.rwth.dbis.ugnm.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

@Path("/users/toplist")
@Component
public class TopListResource {

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
   
//Methode gibt die TopList aus
   
        @GET
        @Produces("application/json")
   
        public Response getTopList() {
   
//Liste wird erstellt    
        	
                List<User> users = userService.getTopList();
                
//Iterator wird erstellt
                
    Iterator<User> usit = users.iterator();
    
//String-Array wird erstellt
    
    Vector<String> vUsers = new Vector<String>();
                while(usit.hasNext()){
                        User u = usit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + u.getEmail();
                        vUsers.add(uUri);
                }

//Liste wird ausgegeben
                try {
                    JSONObject j = new JSONObject();
                	j.put("users", vUsers);
                	Response.ResponseBuilder r = Response.ok(j);
                    return CORS.makeCORS(r, _corsHeaders);		
                } catch (JSONException e) {
        			Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
        			return CORS.makeCORS(r, _corsHeaders);
        		}

        	}
}