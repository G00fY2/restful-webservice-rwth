package de.rwth.dbis.ugnm.resource;

import java.util.Iterator;
import java.util.List;
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

import org.codehaus.jettison.json.JSONArray;
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
    
//JSON-Array wird erstellt
    JSONArray j = new JSONArray();

                while(usit.hasNext()){
                	User user = usit.next();
                    JSONObject userNew = new JSONObject();
                    
                    try {
                    	userNew.put("username", user.getUsername());
                    	userNew.put("ep", user.getEp());
                            j.put(userNew);
                    } 
                    
                    catch (JSONException e) {
                            Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                            return CORS.makeCORS(r, _corsHeaders);
                    }
            }
            
            
            Response.ResponseBuilder r = Response.ok(j);
            return CORS.makeCORS(r, _corsHeaders);
    }


}