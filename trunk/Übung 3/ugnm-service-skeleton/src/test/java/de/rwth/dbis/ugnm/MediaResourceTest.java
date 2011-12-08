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

public class MediaResourceTest extends JerseyTest{
    
	@Autowired
	ApplicationContext context;
	
	/*
	 * Testkonstruktor; sollte f�r alle von Euch geschriebenen Testklassen gleich sein.
	 **/
	
	
    public MediaResourceTest() throws Exception {
		super(new WebAppDescriptor.Builder("de.rwth.dbis.ugnm")
        .contextPath("")
        .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
    }
    
    
	
    
    @Test
	/*
	 * sendet einen GET Request an die Ressource /media. 
	 * 
	 * deckt folgende spezifizierte F�lle ab:
	 * 
	 *   - /media			GET		200	(Liste aller User erfolgreich geholt)
	 **/
    

	public void testGetSuccess() {
		// sende GET Request an Ressource /media und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("media").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ f�r JSON aufweisen.
        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
        
        // verarbeite die zur�ckgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "media" besitzt.
        assertTrue(o.has("media"));
        
	}
    
    @Test
    public void testGetMedium1() {
    	// sende GET Request an Ressource /media und erhalte Antwort als Instanz der Klasse ClientResponse
    			ClientResponse response = resource().path("media/www.medium1.de").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    			
    			// teste, ob die gelieferten Daten den entsprechenden MIME Typ f�r JSON aufweisen.
    	        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
    	        
    	        // verarbeite die zur�ckgelieferten Daten als JSON Objekt.
    	        JSONObject o = response.getEntity(JSONObject.class);
    	        
    	        // teste, ob das gelieferte JSON Object ein Feld "media" besitzt.
    	        assertTrue(o.has("media"));
    }
    
    @Test
    public void testGetNonExisting() {
    	// sende GET Request an Ressource /media und erhalte Antwort als Instanz der Klasse ClientResponse
    			ClientResponse response = resource().path("media").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    			
    			// teste, ob die gelieferten Daten den entsprechenden MIME Typ f�r JSON aufweisen.
    	        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
    	        
    	        // verarbeite die zur�ckgelieferten Daten als JSON Objekt.
    	        JSONObject o = response.getEntity(JSONObject.class);
    	        
    	        // teste, ob das gelieferte JSON Object ein Feld "media" besitzt.
    	        assertTrue(o.has("media"));
    }
    
    
    @Test
    public void testGetFailtureField() {
		// sende GET Request an Ressource /media und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("media").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ f�r JSON aufweisen.
        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
        
        // verarbeite die zur�ckgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "users" besitzt.
        assertFalse(o.has("media123"));  
	}
    
	@Test
	/*
	 * f�hrt zuerst f�r einen nicht existierendes Medium ein DELETE aus. Dies sollte mit 404 fehlschlagen. 
	 * Danach wird dieses Medium mit Put und unter Angabe aller n�tigen Parameter auf die Collection Ressource angelegt. 
	 * Dies sollte erfolgreich sein. Danach wird das selbe Medium wieder gel�scht.
	 * 
	 * deckt folgende spezifizierte F�lle ab:
	 * 
	 *   - /media/{url}	DELETE	404	(zu entfernendes Medium existiert nicht)
	 *   - /media/			PUT	201 (neues Medium wurde erfolgreich angelegt)
	 *   - /media/			PUT	409 (neues Medium schon vorhanden)
	 *   - /media/			PUT	406 (neues Medium mit fehlendem Parameter anlegen)
	 *   - /media/			PUT	401 (neues Medium ohne Authorisierung)
	 *   - /media/{url}	DELETE	200 (bestehendes Medium erfolgreich entfernt)
	 *   - /media/{url}	DELETE	401 (bestehendes Medium entfernen ohne Authorisierung)	
	 *   - /media/{url}		PUT	406 (update Medium mit fehlendem Parameter)
	 *   - /media/{url}		PUT	201 (update Medium erfolgreich)
	 **/
	
	public void testPutWithoutData() {
		// ----------- Anlegen eines Mediums missing data---------------
        WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'value':0,'description':'Neues Test Medium 4'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response1 = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zur�ckgeliefert wurde.
		assertEquals(response1.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());		
	}
	
	@Test
	public void testDeletePutPutAgainDelete() {
		
		// ---------- Delete auf nicht existierendes Medium ------------
		WebResource r = resource(); 
		
		// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		
        // sende DELETE Request an nicht existierende Ressource /media/www.medium4.de (sollte vor dem Test nicht existieren)
		ClientResponse response = r.path("media/www.medium4.de").delete(ClientResponse.class);
		
		// teste, ob der spezifizierte HTTP Status 404 (Not Found) zur�ckgeliefert wurde. 
        assertEquals(response.getStatus(), Status.NOT_FOUND.getStatusCode());
	
        // ----------- Erfolgreiches Anlegen eines Mediums ---------------
        WebResource r2 = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
        r2.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response1 = r2.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zur�ckgeliefert wurde.
		assertEquals(response1.getStatus(), Status.CREATED.getStatusCode());
		
		// ----------- Erfolgreiches Anlegen eines Mediums - Zweites mal ---------------
        WebResource r3 = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
        r3.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content2 = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response2 = r2.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 409 (Conflict) zur�ckgeliefert wurde.
		assertEquals(response2.getStatus(), Status.CONFLICT.getStatusCode());
		
		WebResource r4 = resource(); 

        r4.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		
		ClientResponse response3 = r.path("media/www.medium4.de").delete(ClientResponse.class);
        assertEquals(response3.getStatus(), Status.OK.getStatusCode());
	}
	
	@Test
	public void testDeletePutDeleteFailureDeleteAuth() {

	// ---------- Delete auf nicht existierendes Medium ------------
	WebResource r = resource(); 
	
	// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
    r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
	
    // sende DELETE Request an nicht existierende Ressource /media/www.medium4.de (sollte vor dem Test nicht existieren)
	ClientResponse response = r.path("media/www.medium4.de").delete(ClientResponse.class);
	
	// teste, ob der spezifizierte HTTP Status 404 (Not Found) zur�ckgeliefert wurde. 
    assertEquals(response.getStatus(), Status.NOT_FOUND.getStatusCode());

    // ----------- Erfolgreiches Anlegen eines Mediums ---------------
    WebResource r2 = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
    r2.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String content = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response1 = r2.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zur�ckgeliefert wurde.
	assertEquals(response1.getStatus(), Status.CREATED.getStatusCode());
	
	// ----------- Delete ohne Authorisierung ---------------
	WebResource r3 = resource(); 
	
	// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
    r3.addFilter(new HTTPBasicAuthFilter("nicht.authorisiert@rwth-aachen.de", "abc123")); 
	
    // sende DELETE Request an Ressource /media/www.medium4.de
	ClientResponse response2 = r3.path("media/www.medium4.de").delete(ClientResponse.class);
	
	// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zur�ckgeliefert wurde. 
    assertEquals(response2.getStatus(), Status.UNAUTHORIZED.getStatusCode());
    
	WebResource r4 = resource(); 

    r4.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
	
	ClientResponse response3 = r.path("media/www.medium4.de").delete(ClientResponse.class);
    assertEquals(response3.getStatus(), Status.OK.getStatusCode());
	
	}
	
	@Test
	public void testPutUpdateFailureUpdateSuccessDelete() {
	// ----------- Erfolgreiches Anlegen eines Mediums ---------------
    WebResource r = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
    r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String content = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zur�ckgeliefert wurde.
	assertEquals(response.getStatus(), Status.CREATED.getStatusCode());
	
	WebResource r2 = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
    r2.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String contentUpdate = "{}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response1 = r2.path("media/www.medium4.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,contentUpdate);
	
	// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zur�ckgeliefert wurde.
	assertEquals(response1.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());

	WebResource r3 = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchf�hren.
    r3.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String contentUpdate2 = "{'url':'www.medium4.de','value':1,'description':'Medium 4 Update'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response2 = r3.path("media/www.medium4.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,contentUpdate2);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zur�ckgeliefert wurde.
	assertEquals(response2.getStatus(), Status.CREATED.getStatusCode());
	
	WebResource r4 = resource(); 

    r4.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
	
	ClientResponse response3 = r.path("media/www.medium4.de").delete(ClientResponse.class);
    assertEquals(response3.getStatus(), Status.OK.getStatusCode());
	

	}
}