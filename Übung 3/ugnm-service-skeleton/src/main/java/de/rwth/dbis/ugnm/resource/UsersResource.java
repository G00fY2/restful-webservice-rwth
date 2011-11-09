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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.UserService;

//Alle benötigten Resourcen wurden importiert..

@Path("/users")
@Component
@Scope("request")


public class UsersResource {

	@Autowired
	UserService userService;

	@Context UriInfo uriInfo;
	
   
   
//Methode gibt eine Liste aller User aus (In Arrayform)
   
	@GET
	@Produces("application/json")
   
	public List<User> getUsers(@Context UriInfo ui) {
   
//Liste wird erstellt    
		List<User> userlist = userService.getAll();
//Liste wird ausgegeben
		return userlist;
	}
	
	
//Methode erstellt einen neuen User und wirft exception
   
   
    @PUT
    @Consumes("application/json")
    public Response createUser(JSONObject o) throws JSONException {
//Neuer User wird erstellt
		User u = JsonParse(o);
//Neuer User wird ausgegeben über Methode: addIfDoesNotExists 
		return addIfDoesNotExist(u);

    }
    
    
    
//Methode überprüft ob der angegebene User bereits existiert	
	private Response addIfDoesNotExist(User user) {
//Wenn der User der der Methode übergeben wurde noch nicht existiert wird eine neue URI angelegt und der User gespeichert   
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
   
   
//Methode parst die übergebenen Daten in JSON
  private User JsonParse(JSONObject o){
//Methode versucht, mit allen notwendigen Parametern ein neues JASONObject anzulegen  
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
        
//Bei Fehlerfall wirft die Methode die Exception 400

      } catch (JSONException j) {
       throw new WebApplicationException(400);
      }
                        
  }
}


