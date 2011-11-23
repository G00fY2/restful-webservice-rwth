package de.rwth.dbis.ugnm.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.rwth.dbis.ugnm.entity.Rates;
import de.rwth.dbis.ugnm.service.RatesService;

@Path("/rates/{ratesId}")
@Component
@Scope("request")
public class RateResource {

        @Autowired
        RatesService ratesService;
        
        @Context UriInfo uriInfo;
     
   
//Ein Rating wird über GET ausgegeben  
  
        @GET
        @Produces("application/json")
        public Rates getRates(@PathParam("RatesID") int id){

//Sucht ein Rating mit der übergebenen ID

                Rates r = ratesService.findRating(id);
                
//Überprüft ob das Rating existiert
                          
                if (r==null){
                        throw new WebApplicationException(404);
                }
                return r;
        }       
}

//Keine anderen Methoden da ein Rating weder gelöschen oder geändert werden darf.
//Rates stehen nach der erstellung fest.
