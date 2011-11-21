package de.rwth.dbis.ugnm.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
   
        public JSONObject getUsers() {
   
//Liste wird erstellt    
                List<User> users = userService.getAll();
//Iterator wird erstellt
    Iterator<User> usit = users.iterator();
//String-Array wird erstellt
    Vector<String> vUsers = new Vector<String>();
                while(usit.hasNext()){
                        User u = usit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + u.getEMail();
                        vUsers.add(uUri);
                }
//Liste wird ausgegeben
                try {
                        JSONObject j = new JSONObject();
                        j.append("users",vUsers);
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
                }
                
        }
        
          @POST
    @Consumes("application/json")  
public Response putUser(JSONObject o) throws JSONException {
        
                if(o == null || !(o.has("email") && o.has("benutzername") && o.has("passwort"))){
                        throw new WebApplicationException(Status.BAD_REQUEST);
                }
                else{
                User nu = new User();
                nu.setEMail((String) o.get("email"));
                nu.setPasswort((String) o.get("passwort"));
                nu.setBenutzername((String) o.get("benutzername"));
                        nu.setEP(0);
                        nu.setVorname((String) o.get("vorname"));
                        nu.setNachname((String) o.get("nachname"));
                
                if(userService.findUser(nu) == null) {
                        userService.save(nu);
                        URI location;
                                try {
                                        location = new URI(uriInfo.getAbsolutePath().toASCIIString() + "/" + o.get("EMail"));
                                        return Response.created(location).build();
                                } catch (URISyntaxException e) {
                                        throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
                                }
                }
                else{
                        throw new WebApplicationException(Status.CONFLICT);
                }
        }
    }
        
}