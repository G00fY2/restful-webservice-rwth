package de.rwth.dbis.ugnm.resource;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.UserService;

@Path("/users")
@Component
@Scope("request")
public class UsersResource {

	@Autowired
	UserService userService;

	@GET
	@Produces("application/json")
	public String getUsers(@Context UriInfo ui) {
		// TODO: implement logic
		throw new WebApplicationException(405);
	}
	
	@PUT
    @Consumes("application/json")
    public Response createUser(JSONObject o) throws JSONException {
        // TODO: implement logic
		throw new WebApplicationException(405);
    }
	
	private void addIfDoesNotExist(User user) {
		if(userService.findUser(user) == null) {
			userService.save(user);
		}
	}
}

