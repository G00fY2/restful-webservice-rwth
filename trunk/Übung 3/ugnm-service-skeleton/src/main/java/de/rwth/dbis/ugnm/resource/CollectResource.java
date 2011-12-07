package de.rwth.dbis.ugnm.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.util.Base64;


import de.rwth.dbis.ugnm.entity.Collect;
import de.rwth.dbis.ugnm.service.CollectService;

@Path("/users/{email}/collect/{achievementId}")
@Component
public class CollectResource {

        @Autowired
        CollectService collectService;

//Gibt ueber GET ein einzelnes Collect aus           
//GET Collect ueber Foreginkeys userEmail, achievementId     
        
        @GET
        @Produces("application/json")
        public Collect getCollect(@PathParam("email") String userEmail, @PathParam("achievementId") int achievementId){
        	
//Collect-Objekt wird mit uebergebenen Parametern erzeugt 
        	
                Collect c = collectService.findCollect(userEmail, achievementId);
                
//Wenn Collect-Object nicht = "null" wird dieses Object ausgegeben               
//Andernfalls wird eine 404 WebApplicationException geschmissen               
                
                if (c==null){
                        throw new WebApplicationException(404);
                }
                return c;
        }
        
        @DELETE
        public Response deleteCollect(@HeaderParam("authorization") String auth, @PathParam("email") String userEmail, @PathParam("achievementId") int achievementId){
                        if(admin_authenticated(auth)==false){
                                throw new WebApplicationException(401);
                        }
//GET Achievement ueber Primary Id              

                Collect collect = collectService.findCollect(userEmail, achievementId);
               
//Wenn Achievement nicht "null" ist wird das Achievement gelöscht und ein ok-Response abgesetzt
               
                if(collect!=null){
                        collectService.delete(collect);
                        return Response.ok().build();
                }
               
//Andernfalls wird eine 404 WebApplicationException geschmissen                
               
                else{
                        throw new WebApplicationException(404);
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