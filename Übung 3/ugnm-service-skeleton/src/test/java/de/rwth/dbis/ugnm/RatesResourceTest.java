package de.rwth.dbis.ugnm;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class RatesResourceTest extends JerseyTest{
    
	@Autowired
	ApplicationContext context;
	
	/*
	 * Testkonstruktor; sollte für alle von Euch geschriebenen Testklassen gleich sein.
	 **/
	
	
    public RatesResourceTest() throws Exception {
		super(new WebAppDescriptor.Builder("de.rwth.dbis.ugnm")
        .contextPath("")
        .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
    }
    
    
    
    
    
   
    @Test
    /*
   	 * Versucht ohne Authorisierung ein neues Rate zu erstellen -> 401
   	 * Versucht ein Rate mit fehlendem Parameter zu erstellen -> 406
   	 * 
   	 * deckt folgende spezifizierte Fälle ab:
   	 * 
   	 *   - /users/{email}/rates		PUT	401	(versucht unauthorisiert ein Rate zu erstellen)
   	 *   - /users/{email}/rates		PUT	406	(versucht mit fehlendem Paramter Rate zu erstellen)
   	 *   	
   	 **/
    
    public void testPutUnauthorizedPutMissingParaPutNonExist() {
    	WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'url':'www.medium2.de','rate':1}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response = resource().path("users/sven.hausburg@rwth-aachen.de/rates").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(response.getStatus(), Status.UNAUTHORIZED.getStatusCode());
		
		String content2 = "{}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = r.path("users/sven.hausburg@rwth-aachen.de/rates").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
		assertEquals(response2.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());
		
		// gebe JSON Content als String an.
		String content3 = "{'url':'www.medium99.de','rate':1}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response3 = r.path("users/sven.hausburg@rwth-aachen.de/rates").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content3);
		
		// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
		assertEquals(response3.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());		
	}
    

    
    
    
	@Test
	 /*
		 * Erstellt erfolgreich ein Rating.
		 * Erstellt erfolgreich ein zweites Rating.
		 * 
		 * deckt folgende spezifizierte Fälle ab:
		 * 
		 *   - /users/{email}/rates		PUT	201	(erstellt ein Rate)
	   	 *   - /users/{email}/rates		PUT	201	(erstellt ein Rate)
		 *   	
		 **/
	
	
	public void testPutPut() {
		WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'url':'www.medium2.de','rate':1}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response = r.path("users/sven.hausburg@rwth-aachen.de/rates").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 200 (Ok) zurückgeliefert wurde.
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
		
		// gebe JSON Content als String an.
		String content2 = "{'url':'www.medium2.de','rate':1}";
						
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = r.path("users/sven.hausburg@rwth-aachen.de/rates").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
				
		// teste, ob der spezifizierte HTTP Status 200 (Ok) zurückgeliefert wurde.
		assertEquals(response2.getStatus(), Status.OK.getStatusCode());
		
		}
	
	
	
	
	@Test
	/*
	 * sendet einen GET Request an die Ressource /users/{email}/rates. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/{email}/rates			GET		200	(Liste aller Rates erfolgreich geholt)
	 **/
    
    
    
	public void testGetSuccess() {
		// sende GET Request an Ressource /users und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("users/sven.hausburg@rwth-aachen.de/rates").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "users" besitzt.
        assertTrue(o.has("rates"));
        
	}
}
