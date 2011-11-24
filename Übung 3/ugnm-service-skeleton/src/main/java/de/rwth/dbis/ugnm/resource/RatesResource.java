package de.rwth.dbis.ugnm.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.Rates;
import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.service.RatesService;
import de.rwth.dbis.ugnm.service.UserService;
import de.rwth.dbis.ugnm.service.AchievementService;
import java.sql.Timestamp;




@Path("/users/{email}/rates")
@Component
@Scope("request")
public class RatesResource {


        @Autowired
        RatesService ratesService;
        
        @Autowired
        AchievementService achievementService;
        
        @Autowired
        UserService userService;


        @Context UriInfo uriInfo;

        
//Gibt eine Liste aller Ratings zu einem bestimmten User aus

        @GET
        @Produces("application/json")
        public JSONObject getAllRates(@PathParam("email") String email) {

//Liste wird erstellt, Iterator wird erstellt, Vektor wird erstellt

                List<Rates> rateList = ratesService.getAllRatesUser(email);
                Iterator<Rates> rit = rateList.iterator();
                Vector<String> vRates = new Vector<String>(); 

//Schleife fügt an jedes Rating die ratesID des einzelnen Ratings an
//Dies macht eine referenz von der Liste auf das einzelne Rating möglich

                while(rit.hasNext()){
                        Rates r = rit.next();
                        String rUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + r.getRatesID();
                        vRates.add(rUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("rates",vRates);
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(500);
                }
        }
        
        
//Erstellt über PUT ein neues Rating        

        @PUT
    @Consumes("application/json")
    public Response createRating(@HeaderParam("authorization") String auth, @PathParam("email") String email, JSONObject o) throws JSONException{

//Ein neues Rating wird erstellt

                Rates rate = parseJson(o, email);

//Es wird überprüft ob der User authentifiziert ist

                if(rate.getMediumInstance()!= null && rate.getUserInstance() !=null){
                        if(authenticated(auth, rate.getUserInstance())){
                                ratesService.save(rate);
                        }
                        else{
                                throw new WebApplicationException(401);
                        }
                        return Response.ok().build();
                }
                else{
                        throw new WebApplicationException(400); 
                }
    }
        


//Das Json-Object wird geparst 

        private Rates parseJson(JSONObject o, String email){

                try {
                        String fkUrl = o.getString("url");
                        int rate = o.getInt("rate");
                        Rates rates = new Rates();
                        rates.setFKURL(fkUrl);
                        rates.setFKEMail(email);
                        rates.setRate(rate);
                        Timestamp time = new Timestamp(System.currentTimeMillis()); 
                        rates.setZeit(time);


                        return rates;
                } catch (JSONException e) {
                        throw new WebApplicationException(406);
                }
                
        }
        
        
        // Little gift from your tutors...
        // A simple authentication mechanism;
        // For use in one of the defined operations by referring 
        // to @HeaderParam("authorization") for authHeader.
        private boolean authenticated(String authHeader,User u){
                if(authHeader != null){
                        String[] dauth = null;
                        String authkey = authHeader.split(" ")[1];
                        if(Base64.isBase64(authkey)){
                                dauth = (new String(Base64.decode(authkey))).split(":");
                                if(dauth[0].equals(u.getEMail()) && dauth[1].equals(u.getPasswort())){
                                        return true;
                                }
                        }
                }
                return false;
        }

}
