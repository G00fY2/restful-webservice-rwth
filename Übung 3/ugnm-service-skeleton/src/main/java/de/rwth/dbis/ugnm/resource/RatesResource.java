package de.rwth.dbis.ugnm.resource;

import java.sql.Timestamp;
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
import org.springframework.stereotype.Component;
import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.Rates;
import de.rwth.dbis.ugnm.entity.User;
import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.entity.Collect;
import de.rwth.dbis.ugnm.service.AchievementService;
import de.rwth.dbis.ugnm.service.MediumService;
import de.rwth.dbis.ugnm.service.RatesService;
import de.rwth.dbis.ugnm.service.UserService;
import de.rwth.dbis.ugnm.service.CollectService;


@Path("/users/{email}/rates")
@Component

public class RatesResource {

    	@Autowired
    	CollectService collectService;

        @Autowired
        RatesService ratesService;
       
        @Autowired
        AchievementService achievementService;
       
        @Autowired
        MediumService mediumService;
       
        @Autowired
        UserService userService;
       
        @Context UriInfo uriInfo;

       
//Gibt ueber GET ein Liste aller Rates zu einem User aus
        
        @GET
        @Produces("application/json")
        public JSONObject getAllRates(@PathParam("email") String email) {

                List<Rates> rateList = ratesService.getAllRatesOfUser(email);
                Iterator<Rates> rit = rateList.iterator();
               
                Vector<String> vRates = new Vector<String>();
               
                while(rit.hasNext()){
                        Rates r = rit.next();
                        String rUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + r.getId() + "/" + r.getMediumUrl() + "/" + r.getRate();
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
       
       
       
//Ermöglicht ueber PUT das erstellen eines einzelnen Ratings 
        
        @PUT
    @Consumes("application/json")
    public Response createRate(@HeaderParam("authorization") String auth,@PathParam("email") String email, JSONObject o) throws JSONException{
                //Create a new rating..
                Rates rate = parseRateJsonFile(o, email);
                Medium m = mediumService.getByUrl(o.getString("url"));
                User u = userService.getByEmail(email); 
                //check if the Medium does exist
                if(mediumService.getByUrl(rate.getMediumUrl())!= null){
                        if(authenticated(auth, userService.getByEmail(email))){
                        	if(m.getValue()==rate.getRate()){
                        	    int ep = u.getEp()+100;
                        	    u.setEp(ep);
                        	    userService.update(u);
                        	    reached(ep, email);
                        	    ratesService.save(rate);
                          	    return Response.ok().build();
                        	}else{    
                                return Response.ok().build();
                        	}
                        }
                        else{
                                throw new WebApplicationException(401);
                        }
                       
                }
                else{
                        throw new WebApplicationException(406);
                }
    }
       

//Parst die fuer Rates nötigen Attribute in Json          
        
        private Rates parseRateJsonFile(JSONObject o, String email){

                try {
                        String mediumUrl = o.getString("url");
                        int rate = o.getInt("rate");
                        Rates rating = new Rates();      
                        rating.setMediumUrl(mediumUrl);
                        rating.setUserEmail(email);
                        rating.setRate(rate);
                        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
                        rating.setTime(tstamp);
                       
                        return rating;
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
                                if(dauth[0].equals(u.getEmail()) && dauth[1].equals(u.getPassword())){
                                        return true;
                                }
                        }
                }
                return false;
        }
       
        
        private Collect reached(int ep, String email){
            Collect collect = new Collect();
        	if(ep==100){
        		collect.setAchievementId(1);
        		collect.setUserEmail(email);
        		collectService.save(collect);
        	}
        	else if(ep==200){
        		collect.setAchievementId(2);
                collect.setUserEmail(email);
                collectService.save(collect);
         	}
        	else if(ep==500){
                collect.setAchievementId(3);
                collect.setUserEmail(email);
                collectService.save(collect);
        	}         
            return collect;
        }
}
