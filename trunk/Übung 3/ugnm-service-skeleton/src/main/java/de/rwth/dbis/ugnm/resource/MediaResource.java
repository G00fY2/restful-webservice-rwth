package de.rwth.dbis.ugnm.resource;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;

@Path("/media")
@Component
public class MediaResource {
        
        
        

        @Autowired
        MediumService mediumService;

        @Context UriInfo uriInfo;

        
//Gibt ueber GET ein Liste aller Medien aus        
        
        @GET
        @Produces("application/json")
        public JSONObject getAllMedia() {

                
                List<Medium> media = mediumService.getAll();
                Iterator<Medium> mit = media.iterator();
                
                Vector<String> vMedia = new Vector<String>();   
                
                while(mit.hasNext()){
                        Medium m = mit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + m.getUrl();
                        vMedia.add(uUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("media",vMedia);
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(500);
                }
        }
        
//Ermoeglicht ueber PUT das Erstellen eines einzelnen Mediums       

        @PUT
    @Consumes("application/json")
    public Response createMedium(@HeaderParam("authorization") String auth, JSONObject o) throws JSONException {
        	if(admin_authenticated(auth)==false){
                throw new WebApplicationException(401);
        	}
   
//Medium-Objekt wird mit uebergebenen Parametern erzeugt
        	
            Medium medium = parseMediumJsonFile(o);
            return addIfDoesNotExist(medium);
    }

//Methode ueberprueft das Medium bereits existiert 
        
        private Response addIfDoesNotExist(Medium medium) {
                if(mediumService.findMedium(medium) == null) {
                        mediumService.save(medium);
                        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                        URI mediumUri = ub.path(medium.getUrl()).build();
                        return Response.created(mediumUri).build();
                }
                else{
                        throw new WebApplicationException(409);
                }
        }

//Parst die fuer Medium noetigen Attribute in Json          
        
        
        private Medium parseMediumJsonFile(JSONObject o){

                try {
                        String url = o.getString("url");
                        int value = o.getInt("value");
                        String description = o.getString("description");
                        Medium medium = new Medium();
                        medium.setUrl(url);
                        medium.setValue(value);
                        medium.setDescription(description);
                        return medium;
                } catch (JSONException e) {
                        throw new WebApplicationException(406);
                }  
        }
        
        
        public static boolean admin_authenticated(String authHeader){
            String adminEmail = "sven.hausburg@rwth-aachen.de";
            String adminPw = "abc123";
        if(authHeader != null){
                String[] dauth = null;
                String authkey = authHeader.split(" ")[1];
                if(Base64.isBase64(authkey)){
                        dauth = (new String(Base64.decode(authkey))).split(":");
                        if((dauth[0].toLowerCase().equals(adminEmail)) && dauth[1].equals(adminPw)){
                                return true;
                        }
                }
        }
        return false;
        }
}