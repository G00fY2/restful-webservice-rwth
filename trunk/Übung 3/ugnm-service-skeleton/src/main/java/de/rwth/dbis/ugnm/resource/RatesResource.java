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
import de.rwth.dbis.ugnm.service.Rates;



@Path("/users/{EMail}/ratings")
@Component
@Scope("request")
public class RatesResource {


        @Autowired
        RatesService ratesService;

        @Context UriInfo uriInfo;

        
        //Get all Ratings of the User
        @GET
        @Produces("application/json")
        public JSONObject getAllRates(@PathParam("EMail") String email) {

                List<Rates> rateList = ratesService.getAllRatesOfUser(email);
                Iterator<Rates> rit = rateList.iterator();
                
                Vector<String> vRates = new Vector<String>(); 
                
                while(rit.hasNext()){
                        Rates r = rit.next();
                        String rUri = uriInfo.getAbsolutePath().toASCIIString() + "/" + r.getRatesID();
                        vRates.add(rUri);
                }

                try {
                        JSONObject j = new JSONObject();
                        j.append("Rate",vRates);
                        return j;
                } catch (JSONException e) {
                        throw new WebApplicationException(500);
                }
        }
        
        
        
        //This creates a new Rating
        @PUT
    @Consumes("application/json")
    public Response createRating(@HeaderParam("authorization") String auth, @PathParam("EMail") String email, JSONObject o) throws JSONException{
                //Create a new rating..
                Rates rate = parseJson(o, email);
                //check if authenticated
                if(rate.getMediumInstance()!= null && rate.getUserInstance() !=null){
                        if(authenticated(auth, rate.getUserInstance())){
                                ratesAssociationService.save(rate);
                        }
                        else{
                                throw new WebApplicationException(401);
                        }
                        return Response.ok().build();
                }
                else{
                        throw new WebApplicationException(404); //Not the right exception!!
                }
    }
        
        //parse the JSON File for the attributes
        private Rates parseJson(JSONObject o, String email){

                try {
                        String mediumURL = o.getString("URL");
                        int rate = o.getInt("Rate");
                        Rates rate = new Rates();
                        rate.setMediumURL(mediumURL);
                        rate.setUserEMail(email);
                        rate.setRate(rate);

                        return rateAssociation;
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
                                if(dauth[0].equals(u.getMail()) && dauth[1].equals(u.getPass())){
                                        return true;
                                }
                        }
                }
                return false;
        }

}
