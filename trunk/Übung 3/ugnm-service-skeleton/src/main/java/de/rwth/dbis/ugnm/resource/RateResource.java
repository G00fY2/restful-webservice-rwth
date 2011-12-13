package de.rwth.dbis.ugnm.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.util.Base64;

import de.rwth.dbis.ugnm.entity.Rates;
import de.rwth.dbis.ugnm.service.RatesService;
import de.rwth.dbis.ugnm.util.CORS;

@Path("/users/{email}/rates/{id}")
@Component
public class RateResource {

        @Autowired
        RatesService rateService;
 
    	private String _corsHeaders;
    	
    	// each resource should have this method annotated with OPTIONS. This is needed for CORS.
    	@OPTIONS
    	public Response corsResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
    		_corsHeaders = requestH;
    		return CORS.makeCORS(Response.ok(), requestH);
    	}
//Gibt ueber GET ein einzelnes Rate aus           
//GET Rate ueber Foreginkey userEmail, Primary id         
        
        @GET
        @Produces("application/json")
        public Response getRate(@PathParam("email") String userEmail, @PathParam("id") int id){
        	
//Rates-Objekt wird mit uebergebenen Parametern erzeugt        	
        
                Rates r1 = rateService.getRateById(id);
                
//Wenn Rate-Object nicht = "null" und die mail im object aequivalent zur uebergebenen usermail ist, wird dieses Object ausgegeben               
//Andernfalls wird eine 404 WebApplicationException geschmissen                  
                
                if (r1==null){
                	Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                    return CORS.makeCORS(r, _corsHeaders);
                }
                Response.ResponseBuilder r = Response.ok(r1);
                return CORS.makeCORS(r, _corsHeaders);
        }
        
        @DELETE
        public Response deleteRate(@HeaderParam("authorization") String auth, @PathParam("id") int id){
                        if(admin_authenticated(auth)==false){
                        	 Response.ResponseBuilder r = Response.status(Status.UNAUTHORIZED);
                             return CORS.makeCORS(r, _corsHeaders);
                        }
//GET Achievement ueber Primary Id              

                        Rates rate = rateService.getRateById(id);
               
//Wenn Achievement nicht "null" ist wird das Achievement gelöscht und ein ok-Response abgesetzt
               
                if(rate!=null){
                        rateService.delete(rate);
                        Response.ResponseBuilder r = Response.ok(rate);
                        return CORS.makeCORS(r, _corsHeaders);
                }
               
//Andernfalls wird eine 404 WebApplicationException geschmissen                
               
                else{
                	 Response.ResponseBuilder r = Response.status(Status.NOT_FOUND);
                     return CORS.makeCORS(r, _corsHeaders);
                }
        }
        
        public static boolean admin_authenticated(String authHeader){
            String adminEmail = "sven.hausburg@rwth-aachen.de";
            String adminPw = "abc123";
        if(authHeader != null){
                String[] dauth = null;
                String authkey = authHeader.split(" ")[1];
                if(Base64.isBase64(authkey)){
                        dauth = (new String(Base64.decode(authkey))).split(":");
                        if((dauth[0].toLowerCase().equals(adminEmail)) && dauth[1].equals(adminPw)){
                                return true;
                        }
                }
        }
        return false;
}
        
}
