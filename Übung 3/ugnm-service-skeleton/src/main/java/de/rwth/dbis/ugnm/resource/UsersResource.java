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

	@Context UriInfo uriInfo;
	
	//Liste aller User ausgeben
	@GET
	@Produces("application/json")
	public List<User> getUsers(@Context UriInfo ui) {
	
		List<User> userlist = userService.getAll();
		
		return userlist;
	}
	
	
	//Einen neuen User erstellen
	@PUT
    @Consumes("application/json")
    public Response createUser(JSONObject o) throws JSONException {

		User u = JsonParse(o);
		
		return addIfDoesNotExist(u);

    }
	
	private void addIfDoesNotExist(User user) {
		if(userService.findUser(user) == null) {
			userService.save(user);
			UriBuilder urib = uriInfo.getAbsolutePathBuilder();
            URI userUri = urib.path(user.getEMail()).build();
            return Response.created(userUri).build();

		}
		else{
			throw new WebApplicationException(403);	
		}
	
	}
	 //JSON Datei parsen
    private User JsonParse(JSONObject o){

            try {
                    String passwort = o.getString("Passwort");
                    String benutzername = o.getString("Benutzername");
                    int ep = o.getInt("EP");
                    String vorname = o.getString("Vorname");
                    String nachname = o.getString("Nachname");
                    String email = o.getString("EMail");
                    int achievements =o.getInt("Achievements")
                    User u = new User();
                    u.setPasswort(passwort);
                    u.setBenutername(benutzername);
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

}

