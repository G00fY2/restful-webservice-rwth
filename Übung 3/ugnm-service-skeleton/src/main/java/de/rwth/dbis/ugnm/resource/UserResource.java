package de.rwth.dbis.ugnm.resource;

import java.net.URI;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.UserService;

@Path("/users/{email}")
@Component
@Scope("request")
public class UserResource {

	@Autowired
	UserService userService;

	@Context UriInfo uriInfo;
	
	@GET
	@Produces("application/json")
	public User getUser(@PathParam("email") String email) {
	
		User u = userService.getByEMail(email);
		if (u!=null){
             u.setPasswort("passwort");
             System.out.println(u.getBenutzername());
		}
		else{
             throw new WebApplicationException(404);
		}
		return u;
}

	
	@PUT
    @Consumes("application/json")
    public Response updateUser(@HeaderParam("authorization") String auth, @PathParam("email") String email, JSONObject o) throws JSONException {
		User u = userService.getByEMail(email);
        if(u != null){
                if(authenticated(auth,u)){
                        User user = JsonParseU(o, email);
                        userService.update(u);
                        UriBuilder urib = uriInfo.getAbsolutePathBuilder();
                        URI userUri = urib.path(u.getEMail()).build();
                        return Response.created(userUri).build();
                }
                else{
                        throw new WebApplicationException(401);
                }
        }
        else{
                throw new WebApplicationException(404);
        }
}

	
	@DELETE
	public Response deleteUser(@HeaderParam("authorization") String auth, @PathParam("email") String email){
		User u = userService.getByEMail(email);
        if(u!=null){
        	if(authenticated(auth,u)){
                userService.delete(u);
                UriBuilder urib = uriInfo.getAbsolutePathBuilder();
                URI userUri = urib.path(u.getEMail()).build();
                return Response.created(userUri).build();
        	}
        	else{
                throw new WebApplicationException(401);
        	}
        }
        else{
        	throw new WebApplicationException(404);
        }
	}

    private User JsonParseU(JSONObject o, String mail){
   	 try {
            String passwort = o.getString("Passwort");
            String benutzername = o.getString("Benutzername");
            int ep = o.getInt("EP");
            String vorname = o.getString("Vorname");
            String nachname = o.getString("Nachname");
            String email = o.getString("EMail");
            int achievements =o.getInt("Achievements");
            User u = new User();
            u.setPasswort(passwort);
            u.setBenutzername(benutzername);
            u.setEP(ep);
            u.setVorname(vorname);
            u.setNachname(nachname);
            u.setEMail(email);
            u.setAchievements(achievements);
            return u;
    } catch (JSONException j) {
            throw new WebApplicationException(400);
    }
                
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
				if(dauth[0].equals(u.getEMail()) && dauth[1].equals(u.getPasswort())){
					return true;
				}
			}
		}
		return false;
	}
}