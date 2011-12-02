package de.rwth.dbis.ugnm.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.rwth.dbis.ugnm.entity.Rates;
import de.rwth.dbis.ugnm.service.RatesService;

@Path("/users/{email}/rates/{id}")
@Component
@Scope("request")
public class RateResource {

        @Autowired
        RatesService rateService;
 
//Gibt ueber GET ein einzelnes Rate aus           
//GET Collect ueber Foreginkey userEmail, Primary id         
        
        @GET
        @Produces("application/json")
        public Rates getRate(@PathParam("email") String userEmail, @PathParam("id") int id){
        	
//Rates-Objekt wird mit uebergebenen Parametern erzeugt        	
        
                Rates r = rateService.getRateById(id);
                
//Wenn Rate-Object nicht = "null" und die mail im object aequivalent zur uebergebenen usermail ist, wird dieses Object ausgegeben               
//Andernfalls wird eine 404 WebApplicationException geschmissen                  
                
                if (r==null){
                        throw new WebApplicationException(404);
                }
                return r;
        }
        
}
