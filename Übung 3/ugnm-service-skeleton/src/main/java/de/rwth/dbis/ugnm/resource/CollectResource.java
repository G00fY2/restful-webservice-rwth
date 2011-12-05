package de.rwth.dbis.ugnm.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.rwth.dbis.ugnm.entity.Collect;
import de.rwth.dbis.ugnm.service.CollectService;

@Path("/users/{email}/collect/{achievementId}")
@Component
@Scope("request")
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
        
}