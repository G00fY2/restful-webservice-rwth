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

import de.rwth.dbis.ugnm.entity.Achievement;
import de.rwth.dbis.ugnm.service.AchievementService;

@Path("/achievements/{identifier}")
@Component
@Scope("request")
public class AchievementResource {

        @Autowired
        AchievementService achievementService;
        
        @Context UriInfo uriInfo;
        
//Achievement wird über GET ausgegeben

        @GET
        @Produces("application/json")
        public Achievement getAchievement(@PathParam("identifier") int identifier) {
        
//Sucht ein Achievement mit dem übergebenen Identifier

                Achievement a = achievementService.getByIdentifier(identifier);
 
//Überprüft ob Achievement existiert
 
                if (a==null){
                        throw new WebApplicationException(404);
                }
                return a;
        }
}



//Keine anderen Methoden da kein User ein Achievement löschen/ändern/erstellen darf.
//Achiements sind vordefiniert

