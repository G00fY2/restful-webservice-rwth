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
	 *   - /achievements			GET		200	(Liste aller User erfolgreich geholt)
	 **/
    

	public void testGetSuccess() {
		// sende GET Request an Ressource /achievements und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("achievements").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "achievements" besitzt.
        assertTrue(o.has("achievements"));
        
	}

    
    
    
    
	@Test
	/*
	 * führt zuerst für einen nicht existierendes Achievement ein DELETE aus. Dies sollte mit 404 fehlschlagen. 
	 * Danach wird dieses Achievement mit Put und unter Angabe aller nötigen Parameter auf die Collection Ressource angelegt. 
	 * Dies sollte erfolgreich sein. Danach wird das selbe Achievement wieder gelöscht.
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements/{url}	DELETE	404	(zu entfernendes Achievement existiert nicht)
	 *   - /achievements/			PUT	201 (neues Achievement wurde erfolgreich angelegt)
	 *   - /achievements/			PUT	409 (neues Achievement schon vorhanden)
	 *   - /achievements/			PUT	406 (neues Achievement mit fehlendem Parameter anlegen)
	 *   - /achievements/			PUT	401 (neues Achievement ohne Authorisierung)
	 *   - /achievements/{url}	DELETE	200 (bestehendes Achievement erfolgreich entfernt)
	 *   - /achievements/{url}	DELETE	401 (bestehendes Achievement entfernen ohne Authorisierung)	
	 *   - /achievements/{url}		PUT	406 (update Achievement mit fehlendem Parameter)
	 *   - /achievements/{url}		PUT	201 (update Achievement erfolgreich)
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
		assertEquals(response.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());	
		
		// ----------- Anlegen eines Achievements ohne Authorisierung---------------
		
		String content2 = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response1 = resource().path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(response1.getStatus(), Status.UNAUTHORIZED.getStatusCode());	
	}
	
	
	
	
	
	@Test
	/*
	 * sendet einen GET Request an die Ressource /achievements. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements			GET		200	(Liste aller User erfolgreich geholt)
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
		assertEquals(response1.getStatus(), Status.CREATED.getStatusCode());
	    
		// gebe JSON Content als String an.
		String content2 = "{'description':'Neues Test-Achievement Update','name':'Achievement9 Update','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = resource().path("achievements/9").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde.
		assertEquals(response2.getStatus(), Status.UNAUTHORIZED.getStatusCode());
		
		// sende GET Request an Ressource /Achievements/... und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response3 = resource().path("achievements/9").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
		assertEquals(response3.getType().toString(),MediaType.APPLICATION_JSON);
        
		// verarbeite die zurückgelieferten Daten als JSON Objekt.
		JSONObject o = response3.getEntity(JSONObject.class);
        
		// prüfe ob Objekt folgende Einträge aufweist
		assertTrue(o.has("id"));
        assertTrue(o.has("description"));  
        assertTrue(o.has("name"));  
        assertTrue(o.has("url"));  

		ClientResponse response4 = r.path("achievements/9").delete(ClientResponse.class);
        assertEquals(response4.getStatus(), Status.OK.getStatusCode());
		
	}
	
	
	
	
	
    @Test
	/*
	 * sendet einen GET Request an die Ressource /achievements. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements			GET		200	(Liste aller User erfolgreich geholt)
	 **/
    
	public void testDeletePutPutAgainDelete() {
		
		// ---------- Delete auf nicht existierendes Achievement ------------
		WebResource r = resource(); 
		
		// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		
        // sende DELETE Request an nicht existierende Ressource /achievements/9.medium4.de (sollte vor dem Test nicht existieren)
		ClientResponse response = r.path("achievements/9").delete(ClientResponse.class);
		
		// teste, ob der spezifizierte HTTP Status 404 (Not Found) zurückgeliefert wurde. 
        assertEquals(response.getStatus(), Status.NOT_FOUND.getStatusCode());
	
        // ----------- Erfolgreiches Anlegen eines Achievements ---------------
        
		// gebe JSON Content als String an.
		String content = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response1 = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(response1.getStatus(), Status.CREATED.getStatusCode());
		
		// ----------- Erfolgreiches Anlegen eines Achievements - Zweites mal ---------------

		// gebe JSON Content als String an.
		String content2 = "{'id':9,'description':'Neues Test-Achievement','name':'Achievement9','url':'/achievements/9'}";
		
		// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
		ClientResponse response2 = r.path("achievements").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
		
		// teste, ob der spezifizierte HTTP Status 409 (Conflict) zurückgeliefert wurde.
		assertEquals(response2.getStatus(), Status.CONFLICT.getStatusCode());
		
		ClientResponse response3 = r.path("achievements/9").delete(ClientResponse.class);
        assertEquals(response3.getStatus(), Status.OK.getStatusCode());
	}
	
    
    
    
    @Test
	/*
	 * sendet einen GET Request an die Ressource /achievements. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements			GET		200	(Liste aller User erfolgreich geholt)
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
	assertEquals(response1.getStatus(), Status.CREATED.getStatusCode());
	
	// ----------- Delete ohne Authorisierung ---------------
	WebResource r2 = resource(); 
	
	// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
    r2.addFilter(new HTTPBasicAuthFilter("nicht.authorisiert@rwth-aachen.de", "abc123")); 
	
    // sende DELETE Request an Ressource /achievements/9
	ClientResponse response2 = r2.path("achievements/9").delete(ClientResponse.class);
	
	// teste, ob der spezifizierte HTTP Status 401 (Unauthorized) zurückgeliefert wurde. 
    assertEquals(response2.getStatus(), Status.UNAUTHORIZED.getStatusCode());

	ClientResponse response3 = r.path("achievements/9").delete(ClientResponse.class);
    assertEquals(response3.getStatus(), Status.OK.getStatusCode());
	
	}
	
    
    
    
    @Test
	/*
	 * sendet einen GET Request an die Ressource /achievements. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /achievements			GET		200	(Liste aller User erfolgreich geholt)
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
	assertEquals(response.getStatus(), Status.CREATED.getStatusCode());
    
	// gebe JSON Content als String an.
	String content2 = "{}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
	ClientResponse response1 = r.path("achievements/9").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
	
	// teste, ob der spezifizierte HTTP Status 406 (Not Acceptable) zurückgeliefert wurde.
	assertEquals(response1.getStatus(), Status.NOT_ACCEPTABLE.getStatusCode());

	// gebe JSON Content als String an.
	String content3 = "{'description':'Neues Test-Achievement Update','name':'Achievement9 Update','url':'/achievements/9'}";
	
	// sende PUT Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /achievements.
	ClientResponse response2 = r.path("achievements/9").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content3);
	
	// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
	assertEquals(response2.getStatus(), Status.CREATED.getStatusCode());
	
	ClientResponse response3 = r.path("achievements/9").delete(ClientResponse.class);
    assertEquals(response3.getStatus(), Status.OK.getStatusCode());
	

	}
}