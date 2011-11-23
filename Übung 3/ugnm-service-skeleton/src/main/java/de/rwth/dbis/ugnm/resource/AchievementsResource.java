package de.rwth.dbis.ugnm.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.rwth.dbis.ugnm.entity.Achievement;
import de.rwth.dbis.ugnm.service.AchievementService;

@Path("/achievements")
@Component
@Scope("request")
public class AchievementsResource {
        
        
        

        @Autowired
        AchievementService achievementService;

        @Context UriInfo uriInfo;

        
//Gibt eine Liste aller Achievements aus


        @GET
        @Produces("application/json")
        public JSONObject getAchievements() {

//Liste wird erstellt, Iterator wird erstellt, Vektor wird erstellt

                List<Achievement> achievements = achievementService.getAll();
                Iterator<Achievement> ait = achievements.iterator();               
                Vector<String> vAchievements = new Vector<String>();    

//Schleife fügt an jedes Achievement den Identifier des einzelnen Achievment an
//Dies macht eine referenz von der Liste auf das einzelne Achievement möglich

                while(ait.hasNext()){
                        Achievement a = ait.next();
                        String aUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + a.getIdentifier();
                        vAchievements.add(aUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("Achievements",vAchievements);
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(500);
                }
        }
}


//Keine anderen Methoden da ein Achievement weder gelöschen/geändert/erstellen werden darf.
//Achiements sind vordefiniert