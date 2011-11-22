package de.rwth.dbis.ugnm.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
        
        @GET
        @Produces("application/json")
        public Rates getRates(@PathParam("RatesID") String id){
                Rates r = ratesService.getByRatesID(id);
                if (r==null){
                        throw new WebApplicationException(404);
                }
                return r;
        }       
}
