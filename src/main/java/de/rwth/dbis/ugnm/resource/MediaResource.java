package de.rwth.dbis.ugnm.resource;

import java.net.URI;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/media")
@Component
public class MediaResource {
        
        
        

        @Autowired
        MediumService mediumService;

        @Context UriInfo uriInfo;

        private String _corsHeaders;
        
        // each resource should have this method annotated with OPTIONS. This is needed for CORS.
        @OPTIONS
        public Response corsResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
                _corsHeaders = requestH;
                return CORS.makeCORS(Response.ok(), requestH);
        }
        
//Gibt ueber GET ein Liste aller Medien aus        
        
        @GET
        @Produces("application/json")
        public Response getAllMedia() {

                
                List<Medium> media = mediumService.getAll();
                Iterator<Medium> mit = media.iterator();
                
                Vector<String> vMedia = new Vector<String>();   
                
                while(mit.hasNext()){
                        Medium m = mit.next();
                        String uUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + m.getId();
                        vMedia.add(uUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.put("media",vMedia);
                        Response.ResponseBuilder r = Response.ok(j);
                        return CORS.makeCORS(r, _corsHeaders);
                } catch (JSONException e) {
                        Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }
        
//Ermoeglicht ueber PUT das Erstellen eines einzelnen Mediums       

        @PUT
    @Consumes("application/json")
    public Response createMedium(@HeaderParam("authorization") String auth, JSONObject o) throws JSONException {
                if(admin_authenticated(auth)==false){
                        Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                return CORS.makeCORS(r, _corsHeaders);
                }
   
            //Falls neues Medium nicht alle Daten hat gibt es einen 406 Error
            
                if(o == null || !(o.has("id") && o.has("url") && o.has("value") && o.has("description")&& o.has("tag"))){
                Response.ResponseBuilder r = Response.status(Status.NOT_ACCEPTABLE);
                return CORS.makeCORS(r, _corsHeaders);
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
                        URI mediumUri = ub.path(medium.getId()+"").build();
                        Response.ResponseBuilder r = Response.created(mediumUri);
                        return CORS.makeCORS(r, _corsHeaders);
                }
                else{
                        Response.ResponseBuilder r = Response.status(Status.CONFLICT);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }

//Parst die fuer Medium noetigen Attribute in Json          
        
        
        private Medium parseMediumJsonFile(JSONObject o){

                try {   
                                int id = o.getInt("id");
                        String url = o.getString("url");
                        int value = o.getInt("value");
                        String description = o.getString("description");
                        String tag = o.getString("tag");
                        Medium medium = new Medium();
                        medium.setId(id);
                        medium.setUrl(url);
                        medium.setValue(value);
                        medium.setDescription(description);
                        medium.setTag(tag);
                        return medium;
                } catch (JSONException e) {
                        return null;
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
