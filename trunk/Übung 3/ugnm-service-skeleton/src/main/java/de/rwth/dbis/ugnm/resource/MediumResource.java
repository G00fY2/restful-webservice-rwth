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

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;

@Path("/media/{URL}")
@Component
@Scope("request")
public class MediumResource {

        @Autowired
        MediumService mediumService;
        
        @Context UriInfo uriInfo;
        
//Medium wird über GET ausgegeben       
        
        @GET
        @Produces("application/json")
        public Medium getMedium(@PathParam("URL") String url){
        
//Sucht ein Medium mit der übergebenen URL

                Medium m = mediumService.getByURL(url);
                
//Überprüft ob Medium existiert                
          
                if (m==null){
                        throw new WebApplicationException(404);
                }
                return m;
        }       
}

//Keine anderen Methoden da ein Medium weder gelöschen/geändert/erstellen werden darf.
//Medien sind vordefiniert
