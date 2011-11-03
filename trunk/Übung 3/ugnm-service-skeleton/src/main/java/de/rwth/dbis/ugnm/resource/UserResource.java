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
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.UserService;

@Path("/users/{login}")
@Component
@Scope("request")
public class UserResource {

	@Autowired
	UserService userService;

	@GET
	@Produces("application/json")
	public User getUser(@PathParam("login") String login) {
		// TODO: implement logic
		User u = userService.getByLogin(login);
		System.out.println(u.getName());
		throw new WebApplicationException(405);
	}
	
	@PUT
    @Consumes("application/json")
    public Response updateUser(@HeaderParam("authorization") String auth, @PathParam("login") String login, JSONObject o) throws JSONException {
		// TODO: implement logic
		throw new WebApplicationException(405);
    }
	
	@DELETE
	public Response deleteUser(@HeaderParam("authorization") String auth, @PathParam("login") String login){
		// TODO: implement logic
		throw new WebApplicationException(404);
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
				if(dauth[0].equals(u.getLogin()) && dauth[1].equals(u.getPass())){
					return true;
				}
			}
		}
		return false;
	}

}