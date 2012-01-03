package de.rwth.dbis.ugnm.resource;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

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
import de.rwth.dbis.ugnm.util.CORS;


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

    	private String _corsHeaders;
    	
    	// each resource should have this method annotated with OPTIONS. This is needed for CORS.
    	@OPTIONS
    	public Response corsResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
    		_corsHeaders = requestH;
    		return CORS.makeCORS(Response.ok(), requestH);
    	}
       
//Gibt ueber GET ein Liste aller Rates zu einem User aus
        
        @GET
        @Produces("application/json")
        public Response getAllRates(@PathParam("email") String email) {

                List<Rates> rateList = ratesService.getAllRatesOfUser(email);
                Iterator<Rates> rit = rateList.iterator();

                Vector<String> vRates = new Vector<String>();
               
                while(rit.hasNext()){
                        Rates r = rit.next();
                        String rUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + r.getId() + "/" + r.getMediumId() + "/" + r.getRate();
                        vRates.add(rUri);
                }
                try {
                    	JSONObject j = new JSONObject();
                    	j.append("rates", vRates);
                        Response.ResponseBuilder r = Response.ok(j);
                        return CORS.makeCORS(r, _corsHeaders);
                } catch (JSONException e) {
                	Response.ResponseBuilder r = Response.status(Status.INTERNAL_SERVER_ERROR);
                    return CORS.makeCORS(r, _corsHeaders);
                }
        }
       
       
       
//Ermoeglicht ueber PUT das Erstellen eines einzelnen Ratings (inkl. EPs + Achievements)
        
        @PUT
    @Consumes("application/json")
    public Response createRate(@HeaderParam("authorization") String auth,@PathParam("email") String email, JSONObject o) throws JSONException{
                
                Rates rate = parseRateJsonFile(o, email);
                Medium m = mediumService.getById(o.getInt("id"));
                User u = userService.getByEmail(email); 
                
                //prüft ob das Medium existiert
                if(mediumService.getById(rate.getMediumId())!= null){
                        if(authenticated(auth, userService.getByEmail(email))){
                        	ratesService.save(rate);
                        	
                        	//Vergibt 50 EP falls rate aequivalent value von Medium
                        	if(m.getValue()==rate.getRate()){
                        	    int ep = u.getEp()+50;
                        	    u.setEp(ep);
                        	    userService.update(u);
                        	    
                        	    //Ruft Methode zur Vergabe der Achievements auf (siehe unten)
                        	    reached(ep, email);
                        	    
                        	    Response.ResponseBuilder r = Response.status(Status.OK);
                                return CORS.makeCORS(r, _corsHeaders);
                        	}
                        	else{    
                        		Response.ResponseBuilder r = Response.status(Status.OK);
                                return CORS.makeCORS(r, _corsHeaders);
                        	}
                        }
                        else{
                        	Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                            return CORS.makeCORS(r, _corsHeaders);
                        }
                       
                }
                else{
                	Response.ResponseBuilder r = Response.status(Status.NOT_ACCEPTABLE);
                    return CORS.makeCORS(r, _corsHeaders);
                }
    }
       

//Parst die fuer Rates noetigen Attribute in Json          
        
        private Rates parseRateJsonFile(JSONObject o, String email){

                try {
                        int mediumId = o.getInt("mediumId");
                        int rate = o.getInt("rate");
                        Rates rating = new Rates();      
                        rating.setMediumId(mediumId);
                        rating.setUserEmail(email);
                        rating.setRate(rate);
                        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
                        rating.setTime(tstamp);
                       
                        return rating;
                } catch (JSONException e) {
                        return null;
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
       
        //Methode zur Vergabe der Achievements
        private void reached(int ep, String email){
        	
            Collect collect = new Collect();
    		collect.setUserEmail(email);
    		
        	if(ep==100 && achievementService.getById(1) != null){
        		collect.setAchievementId(1);
        		collectService.save(collect);
        	}
        	else if(ep==500 && achievementService.getById(2) != null){
        		collect.setAchievementId(2);
                collectService.save(collect);
         	}
        	else if(ep==1000 && achievementService.getById(3) != null){
                collect.setAchievementId(3);
                collectService.save(collect);
        	}         
        }
}
