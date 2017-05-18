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
	 * Testkonstruktor; sollte für alle von Euch geschriebenen Testklassen gleich sein.
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
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media			GET		200	(Liste aller Medien erfolgreich geholt)
	 **/
    

	public void testGetSuccess() {
		// sende GET Request an Ressource /media und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("media").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(MediaType.APPLICATION_JSON, response.getType().toString());
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "media" besitzt.
        assertTrue(o.has("media"));
        
	}

    
    
    
    
	@Test
	/*
	 * Versucht ein Medium zuerst mit fehlenden Parametern zu erstellen -> 406
	 * Versucht danach ein Medium ohne Authorisierung zu erstellen -> 401
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media/			PUT	406 (neues Medium mit fehlendem Parameter anlegen)
	 *   - /media/			PUT	401 (neues Medium ohne Authorisierung)
	 *   
	 **/
	
	public void testPutWithoutDataPutUnauthorized() {
		// ----------- Anlegen eines Mediums missing data---------------
        WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
		assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());	
		
		// ----------- Anlegen eines Mediums ohne Authorisierung---------------
		
		String content2 = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response1 = resource().path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response1.getStatus());	
	}
	
	
	
	
	
	@Test
	/*
	 * Erstellt per Put erfolgreich ein neues Medium. Versucht dieses unathorisiert zu updaten -> 401
	 * Liest dann per Get das Medium aus. Löscht es danach erfolgreich. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media/			PUT	201 (neues Medium wurde erfolgreich angelegt)
	 *   - /media/{url}		PUT	401 (Medium unathorisiert versucht zu updaten)
	 *   - /media/{url}		GET	200 (neues Medium wurde ausgegeben)
	 *   - /media/{url}	DELETE	200 (Medium erfolgreich entfernt)
	 *   
	 **/
    
	public void testPutUpdateUnauthorizedGetDelete() {
		 // ----------- Erfolgreiches Anlegen eines Mediums ---------------
        WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response1 = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response1.getStatus());
	    
		//---------------Update Unathorized------------------
		
		// gebe JSON Content als String an.
		String content2 = "{'url':'www.medium4.de','value':1,'description':'Medium 4 Update'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response2 = resource().path("media/www.medium4.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response2.getStatus());
		
		
		// Get-Medium
		// sende GET Request an Ressource /medium/... und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response3 = resource().path("media/www.medium4.de").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
		assertEquals(MediaType.APPLICATION_JSON, response3.getType().toString());
        
		// verarbeite die zurückgelieferten Daten als JSON Objekt.
		JSONObject o = response3.getEntity(JSONObject.class);
        
		// prüfe ob Objekt folgende Einträge aufweist
		assertTrue(o.has("url"));
        assertTrue(o.has("value"));  
        assertTrue(o.has("description"));  

		ClientResponse response4 = r.path("media/www.medium4.de").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response4.getStatus());
		
	}
	
	
	
	
	
    @Test
	/*
	 * Löscht ein nicht existierendes Medium -> 404
	 * Erstellt mittels Put ein neues Medium. Versucht dieses erneut zu erstellen -> 409 Confilct
	 * Löscht das Medium anschließend 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media/{url}			DELETE		404	(versucht nicht existierendes Medium zu löschen)
	 *   - /media/				PUT 		201 (erstellt ein neues Medium)
	 *   - /media/				PUT			409 (versucht das Medium erneut zu erstellen)
	 *   - /media/{url}			DELETE		200	(löscht das Medium)
	 **/
    
	public void testDeletePutPutAgainDelete() {
		
		// ---------- Delete auf nicht existierendes Medium ------------
		WebResource r = resource(); 
		
		// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		
        // sende DELETE Request an nicht existierende Ressource /media/www.medium4.de (sollte vor dem Test nicht existieren)
		ClientResponse response = r.path("media/www.medium4.de").delete(ClientResponse.class);
		
		// teste, ob der spezifizierte HTTP Status 404 (Not Found) zurückgeliefert wurde. 
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	
        // ----------- Erfolgreiches Anlegen eines Mediums ---------------
        
		// gebe JSON Content als String an.
		String content = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response1 = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response1.getStatus());
		
		// ----------- Erfolgreiches Anlegen eines Mediums - Zweites mal ---------------

		// gebe JSON Content als String an.
		String content2 = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response2 = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 409 (Conflict) zurückgeliefert wurde.
		assertEquals(Status.CONFLICT.getStatusCode(), response2.getStatus());
		
		ClientResponse response3 = r.path("media/www.medium4.de").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response3.getStatus());
	}
	
    
    
    
    @Test
	/*
	 * Erstellt ein Medium, versucht anschließened es es unathorisiert zu löschen -> 401
	 * Löscht das Medium 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media			PUT		201	(erstellt ein neues Medium)
	 *   - /media/{url}		DELETE	401 (versucht das Medium unathorisiert zu löschen)
	 *   - /media/{url}   	DELETE	200	(löscht das Medium)
	 **/
    
	public void testPutDeleteUnauthorizedDelete() {

    // ----------- Erfolgreiches Anlegen eines Mediums ---------------
    WebResource r = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
    r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String content = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response1 = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
	assertEquals(Status.CREATED.getStatusCode(), response1.getStatus());
	
	// ----------- Delete ohne Authorisierung ---------------
	WebResource r2 = resource(); 
	
	// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
    r2.addFilter(new HTTPBasicAuthFilter("nicht.authorisiert@rwth-aachen.de", "abc123")); 
	
    // sende DELETE Request an Ressource /media/www.medium4.de
	ClientResponse response2 = r2.path("media/www.medium4.de").delete(ClientResponse.class);
	
	// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde. 
    assertEquals(Status.UNAUTHORIZED.getStatusCode(), response2.getStatus());

    
	ClientResponse response3 = r.path("media/www.medium4.de").delete(ClientResponse.class);
    assertEquals(Status.OK.getStatusCode(), response3.getStatus());
	
	}
	
    
    
    
    @Test
	/*
	 * Erstellt ein Medium. Versucht es ohne Daten zu updaten -> 406
	 * Updatet das Medium erfolgreich.
	 * Löscht das Medium. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media			PUT		201	(erstellt ein neues Medium)
	 *   - /media/{url}		PUT		406 (versucht Medium ohne Daten zu updaten)
	 * 	 - /media/{url}		PUT		201 (Updatet das Medium)
	 *   - /media/{url}		DELETE	200	(Löscht das Medium) 
	 **/
    
	public void testPutUpdateMissingParaUpdateDelete() {
	// ----------- Erfolgreiches Anlegen eines Mediums ---------------
    WebResource r = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
    r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String content = "{'url':'www.medium4.de','value':0,'description':'Neues Test Medium 4'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response = r.path("media").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    
	// gebe JSON Content als String an.
	String content2 = "{}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response1 = r.path("media/www.medium4.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
	
	// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
	assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response1.getStatus());
    
	// gebe JSON Content als String an.
	String content3 = "{'value':1,'description':'Medium 4 Update'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
	ClientResponse response2 = r.path("media/www.medium4.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content3);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
	assertEquals(Status.CREATED.getStatusCode(), response2.getStatus());

	ClientResponse response3 = r.path("media/www.medium4.de").delete(ClientResponse.class);
    assertEquals(Status.OK.getStatusCode(), response3.getStatus());
	

	}
}