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

public class AchievementsResourceTest extends JerseyTest{
    
	@Autowired
	ApplicationContext context;
	
	/*
	 * Testkonstruktor; sollte für alle von Euch geschriebenen Testklassen gleich sein.
	 **/
	
	
    public AchievementsResourceTest() throws Exception {
		super(new WebAppDescriptor.Builder("de.rwth.dbis.ugnm")
        .contextPath("")
        .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
    }
    
    
	
    
    @Test
	/*
	 * sendet einen GET Request an die Ressource /achievements. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements			GET		200	(Liste aller Achievements erfolgreich geholt)
	 **/
    

	public void testGetSuccess() {
		// sende GET Request an Ressource /achievements und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("achievements").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(MediaType.APPLICATION_JSON, response.getType().toString());
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "achievements" besitzt.
        assertTrue(o.has("achievements"));
        
	}

    
    
    
    
    @Test
	/*
	 * Versucht ein Achievement zuerst mit fehlenden Parametern zu erstellen -> 406
	 * Versucht danach ein Achievement ohne Authorisierung zu erstellen -> 401
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements/			PUT	406 (neues Achievement mit fehlendem Parameter anlegen)
	 *   - /achievements/			PUT	401 (neues Achievement ohne Authorisierung)
	 *   
	 **/
	
	public void testPutWithoutDataPutUnauthorized() {
		// ----------- Anlegen eines Achievements missing data---------------
        WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
		assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());	
		
		// ----------- Anlegen eines Achievements ohne Authorisierung---------------
		
		String content2 = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response1 = resource().path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response1.getStatus());	
	}
	
	
	
	
	
	@Test
	/*
	 * Erstellt per Put erfolgreich ein neues Achievement. Versucht dieses unathorisiert zu updaten -> 401
	 * Liest dann per Get das Achievement aus. Löscht es danach erfolgreich. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements/			PUT	201 (neues Achievement wurde erfolgreich angelegt)
	 *   - /achievements/{id}		PUT	401 (Achievement unathorisiert versucht zu updaten)
	 *   - /achievements/{id}		GET	200 (neues Achievement wurde ausgegeben)
	 *   - /achievements/{id}	DELETE	200 (Achievement erfolgreich entfernt)
	 *   
	 **/
	
	public void testPutUpdateUnauthorizedGetDelete() {
		 // ----------- Erfolgreiches Anlegen eines Achievements ---------------
        WebResource r = resource(); 
        
        // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
        
		// gebe JSON Content als String an.
		String content = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response1 = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response1.getStatus());
	    
		// gebe JSON Content als String an.
		String content2 = "{'description':'Neues Test-Achievement Update','name':'Achievement9 Update','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = resource().path("achievements/9").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response2.getStatus());
		
		// sende GET Request an Ressource /Achievements/... und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response3 = resource().path("achievements/9").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
		assertEquals(MediaType.APPLICATION_JSON, response3.getType().toString());
        
		// verarbeite die zurückgelieferten Daten als JSON Objekt.
		JSONObject o = response3.getEntity(JSONObject.class);
        
		// prüfe ob Objekt folgende Einträge aufweist
		assertTrue(o.has("id"));
        assertTrue(o.has("description"));  
        assertTrue(o.has("name"));  
        assertTrue(o.has("url"));  

		ClientResponse response4 = r.path("achievements/9").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response4.getStatus());
		
	}
	
	
	
	
	
    @Test
	/*
	 * Löscht ein nicht existierendes Achievement -> 404
	 * Erstellt mittels Put ein neues Achievement. Versucht dieses erneut zu erstellen -> 409 Confilct
	 * Löscht das Achievement anschließend 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements/{id}			DELETE		404	(versucht nicht existierendes Achievement zu löschen)
	 *   - /achievements/				PUT 		201 (erstellt ein neues Achievement)
	 *   - /achievements/				PUT			409 (versucht das Achievement erneut zu erstellen)
	 *   - /achievements/{id}			DELETE		200	(löscht das Achievement)
	 **/
    
	public void testDeletePutPutAgainDelete() {
		
		// ---------- Delete auf nicht existierendes Achievement ------------
		WebResource r = resource(); 
		
		// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		
        // sende DELETE Request an nicht existierende Ressource /achievements/9 (sollte vor dem Test nicht existieren)
		ClientResponse response = r.path("achievements/9").delete(ClientResponse.class);
		
		// teste, ob der spezifizierte HTTP Status 404 (Not Found) zurückgeliefert wurde. 
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	
        // ----------- Erfolgreiches Anlegen eines Achievements ---------------
        
		// gebe JSON Content als String an.
		String content = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response1 = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response1.getStatus());
		
		// ----------- Erfolgreiches Anlegen eines Achievements - Zweites mal ---------------

		// gebe JSON Content als String an.
		String content2 = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 409 (Conflict) zurückgeliefert wurde.
		assertEquals(Status.CONFLICT.getStatusCode(), response2.getStatus());
		
		ClientResponse response3 = r.path("achievements/9").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response3.getStatus());
	}
	
    
    
    
    @Test
	/*
	 * Erstellt ein Achievement, versucht anschließened es es unathorisiert zu löschen -> 401
	 * Löscht das Achievement 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements			PUT		201	(erstellt ein neues Achievement)
	 *   - /achievements/{id}		DELETE	401 (versucht das Achievement unathorisiert zu löschen)
	 *   - /achievements/{id}   	DELETE	200	(löscht das Achievement)
	 **/
    
	public void testPutDeleteUnauthorizedDelete() {

    // ----------- Erfolgreiches Anlegen eines Achievements ---------------
    WebResource r = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
    r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String content = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
	ClientResponse response1 = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
	assertEquals(Status.CREATED.getStatusCode(), response1.getStatus());
	
	// ----------- Delete ohne Authorisierung ---------------
	WebResource r2 = resource(); 
	
	// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
    r2.addFilter(new HTTPBasicAuthFilter("nicht.authorisiert@rwth-aachen.de", "abc123")); 
	
    // sende DELETE Request an Ressource /achievements/9
	ClientResponse response2 = r2.path("achievements/9").delete(ClientResponse.class);
	
	// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde. 
    assertEquals(Status.UNAUTHORIZED.getStatusCode(), response2.getStatus());

	ClientResponse response3 = r.path("achievements/9").delete(ClientResponse.class);
    assertEquals(Status.OK.getStatusCode(), response3.getStatus());
	
	}
	
    
    
    
    @Test
   	/*
   	 * Erstellt ein Achievement. Versucht es ohne Daten zu updaten -> 406
   	 * Updatet das Achievement erfolgreich.
   	 * Löscht das Achievement. 
   	 * 
   	 * deckt folgende spezifizierte Fälle ab:
   	 * 
   	 *   - /achievements			PUT		201	(erstellt ein neues Achievement)
   	 *   - /achievements/{id}		PUT		406 (versucht Achievement ohne Daten zu updaten)
   	 * 	 - /achievements/{id}		PUT		201 (Updatet das Achievement)
   	 *   - /achievements/{id}		DELETE	200	(Löscht das Achievement) 
   	 **/
    
	public void testPutUpdateMissingParaUpdateDelete() {
	// ----------- Erfolgreiches Anlegen eines Achievements ---------------
    WebResource r = resource(); 
    
    // auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
    r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
    
	// gebe JSON Content als String an.
	String content = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
	ClientResponse response = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
	assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    
	// gebe JSON Content als String an.
	String content2 = "{}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
	ClientResponse response1 = r.path("achievements/9").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
	
	// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
	assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response1.getStatus());

	// gebe JSON Content als String an.
	String content3 = "{'description':'Neues Test-Achievement Update','name':'Achievement9 Update','url':'/achievements/9'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
	ClientResponse response2 = r.path("achievements/9").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content3);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
	assertEquals(Status.CREATED.getStatusCode(), response2.getStatus());
	
	ClientResponse response3 = r.path("achievements/9").delete(ClientResponse.class);
    assertEquals(Status.OK.getStatusCode(), response3.getStatus());
	

	}
}