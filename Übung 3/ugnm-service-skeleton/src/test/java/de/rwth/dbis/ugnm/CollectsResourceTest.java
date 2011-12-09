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

public class CollectsResourceTest extends JerseyTest{
    
	@Autowired
	ApplicationContext context;
	
	/*
	 * Testkonstruktor; sollte für alle von Euch geschriebenen Testklassen gleich sein.
	 **/
	
	
    public CollectsResourceTest() throws Exception {
		super(new WebAppDescriptor.Builder("de.rwth.dbis.ugnm")
        .contextPath("")
        .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
    }
    
    
	
    
    @Test
    /*
	 * Versucht ohne Authorisierung ein neues Collect zu erstellen -> 401
	 * Versucht ein Collect mit fehlendem Parameter zu erstellen ->406
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/{email}/collect	PUT	401	(versucht unauthorisiert ein Collect zu erstellen)
	 *   - /users/{email}/collect	PUT	406	(versucht mit fehlendem Paramter Collect zu erstellen)
	 *   	
	 **/
    
    public void testPutUnauthorizedPutMissingParaPutNonExist() {
    	WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'achievementId':1}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response = resource().path("users/sven.hausburg@rwth-aachen.de/collect").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(response.getStatus(), Status.UNAUTHORIZED.getStatusCode());
		
		String content2 = "{}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = r.path("users/sven.hausburg@rwth-aachen.de/collect").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
		assertEquals(response2.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());
		
		// gebe JSON Content als String an.
		String content3 = "{'achievementId':999}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response3 = r.path("users/sven.hausburg@rwth-aachen.de/collect").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content3);
		
		// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
		assertEquals(response3.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());		
	}
    

    
    
    
	@Test
    /*
	 * Erstellt erfolgreich ein Collect.
	 * Erstellt erfolgreich ein zweites Collect.
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/{email}/collect	PUT	201	(erstellt ein Collect)
	 *   - /users/{email}/collect	PUT	201	(erstellt ein Collect)
	 *   	
	 **/
	
	
	public void testPutPut() {
		WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'achievementId':1}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response = r.path("users/sven.hausburg@rwth-aachen.de/collect").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 200 (Ok) zurückgeliefert wurde.
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
		
		// gebe JSON Content als String an.
		String content2 = "{'achievementId':2}";
						
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = r.path("users/sven.hausburg@rwth-aachen.de/collect").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
				
		// teste, ob der spezifizierte HTTP Status 200 (Ok) zurückgeliefert wurde.
		assertEquals(response2.getStatus(), Status.OK.getStatusCode());
		
		}
	
	
	
	
	@Test
	/*
	 * sendet einen GET Request an die Ressource /users/{email}/collect. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/{email}/collect		GET		200	(Liste aller Collects eines Users erfolgreich geholt)
	 *   
	 **/
    
    
    
	public void testGetSuccess() {
		// sende GET Request an Ressource /users und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("users/sven.hausburg@rwth-aachen.de/collect").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "users" besitzt.
        assertTrue(o.has("collect"));
        
	}
	
	
	
	
	
	@Test
	/*
	 * Löscht nacheinander die oeben erstellen Collects-. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/{email}/collect/{id}		DELETE		200	(löscht erflgreich das Collect)
	 *   - /users/{email}/collect/{id}		DELETE		200	(löscht erflgreich das Collect)
	 *   
	 **/
    
    
    
	public void DeleteDelete() {
		WebResource r = resource(); 
		
		// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		

		ClientResponse response = r.path("users/sven.hausburg@rwth-aachen.de/collect/1").delete(ClientResponse.class);
        assertEquals(response.getStatus(), Status.OK.getStatusCode());
        

		ClientResponse response1 = r.path("users/sven.hausburg@rwth-aachen.de/collect/2").delete(ClientResponse.class);
        assertEquals(response1.getStatus(), Status.OK.getStatusCode());
	}
	
}
