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

public class UsersResourceTest extends JerseyTest{
    
	@Autowired
	ApplicationContext context;
	
	/*
	 * Testkonstruktor; sollte für alle von Euch geschriebenen Testklassen gleich sein.
	 **/
	
	
    public UsersResourceTest() throws Exception {
		super(new WebAppDescriptor.Builder("de.rwth.dbis.ugnm")
        .contextPath("")
        .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
    }
    
    
    
    // ------------------- Test Methoden -------------------
    
    // hier haben wir Euch ein paar unfertige Beispiele angegeben, die aber das Testen Eurer Ressourcen
    // ganz gut demonstrieren. Das generelle Vorgehen ist:
    //
    //   - sende Request an Ressource
    //	 - empfange Response von Ressource
    //   - vergleiche Response gegen spezifiziertes Verhalten der Ressource
    //
    // Alle Testmethoden sind mit der Annotation @Test versehen.
    // Zusätzliche Hilfsmethoden sollten nicht mit @Test annotiert werden, können aber auch Asserts enthalten.
    // Bitte vergesst nicht, dass Euer Test vor und nach der Ausführung den gleichen Zustand in der Datenbank
    // hinterlassen sollte. Das ist bereits für das angegebene kleine Beispiel der Fall. Es wird ein User "mia"
    // erzeugt, und in einem weiteren Testfall wieder entfernt.
	
    
    
    
    @Test
	/*
	 * sendet einen GET Request an die Ressource /users. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users			GET		200	(Liste aller User erfolgreich geholt)
	 **/
    
	public void testGetUsersSuccess() {
		// sende GET Request an Ressource /users und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("users").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(MediaType.APPLICATION_JSON, response.getType().toString());
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "users" besitzt.
        assertTrue(o.has("users"));     
	}
    
    
    
    
    @Test
	/*
	 * Erstelle erfolgreich einen User, liest diesen aus und löscht ihn anschließend. 
	 * Danach wird versucht den gelöschten User erneut auszugeben. -> 404
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/			PUT	201 (neuer User wurde erfolgreich angelegt)
	 *   - /users/{email}	GET	200 (neuer User wurd ausgegeben)
	 *   - /users/{email}	DELETE	200 (User wurd erfolgreich gelöscht)
	 *   - /users/{email}	GET	404 (auszugebender User existiert nicht)
	 * 
	 *  
	 **/
    
    public void testPostGetDeleteGet() {             

        WebResource r = resource();     

        // ----------- Erfolgreiches Anlegen eines Users ---------------
       
        // gebe JSON Content als String an.
        String content = "{'email':'thomas.tomatenkop@gmx.de','username':'popo','password':'1234','name':'thomas tomatenkop'}";
        
        // sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /users.
        ClientResponse response = r.path("users").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
        
        // teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        
        // sende GET Request an Ressource /users und erhalte Antwort als Instanz der Klasse ClientResponse
        ClientResponse response2 = r.path("users/thomas.tomatenkop@gmx.de").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        
        // teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(MediaType.APPLICATION_JSON, response2.getType().toString());
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response2.getEntity(JSONObject.class);
        
     // prüfe ob Objekt folgende Einträge aufweist
        assertTrue(o.has("email"));   
        assertTrue(o.has("username")); 
        
        // ----------- Erfolgreiches Löschen des Users ---------------         
        WebResource r2 = resource();
        
        r2.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "1234")); 
        
        ClientResponse response3 = r2.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response3.getStatus());    
    }
    
    
    
    
	@Test
	/*
	 * führt zuerst für einen nicht existierenden User ein DELETE aus. Dies sollte mit 404 fehlschlagen. 
	 * Danach wird dieser User mit Post und unter Angabe aller nötigen Parameter auf die Collection Ressource angelegt. 
	 * Dies sollte erfolgreich sein. Danach wird der gleiche User wieder gelöscht.
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/{email}	DELETE	404	(zu entfernender User existiert nicht)
	 *   - /users/			POST	201 (neuer User wurde erfolgreich angelegt)
	 *   - /users/{email}	DELETE	200 (bestehender User erfolgreich entfernt)	
	 *   - /users/{email}	DELETE	404 (User nicht verhanden)	
	 **/
	
	public void testDeletePostDelete() {
		
		// ---------- Delete auf nicht existierenden User ------------
		WebResource r = resource(); 
		
		// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "1234")); 
		
        // sende DELETE Request an nicht existierende Ressource /users/mia (sollte vor dem Test nicht existieren)
		ClientResponse response = r.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
		
		// teste, ob der spezifizierte HTTP Status 404 (Not Found) zurückgeliefert wurde. 
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	
        // ----------- Erfolgreiches Anlegen eines Users ---------------
        
		// gebe JSON Content als String an.
		String content = "{'email':'thomas.tomatenkop@gmx.de','username':'popo','password':'1234','name':'thomas tomatenkop'}";
		
		// sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /users.
		ClientResponse response2 = resource().path("users").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response2.getStatus());
		
		WebResource r2 = resource(); 

        r2.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "1234")); 
		
		ClientResponse response3 = r.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response3.getStatus());
        
        ClientResponse response4 = r.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
        assertEquals(Status.NOT_FOUND.getStatusCode(), response4.getStatus());
	}
	
	
	
	
	@Test
	/*
	 * Erstellt einen User, versucht danach diesen unauthorisiert zu löschen, führt zu einem 404. 
	 * Danach wird der User erfolgreich gelöscht.
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/			POST	201 (neuer User wurde erfolgreich angelegt)
	 *   - /users/{email}	DELETE	406 (kann nicht gelöscht werden da Authorisierung fehlt)	
	 *   - /users/{email}	DELETE	200 (User erflogreih gelöscht)
	 *  
	 **/
	
	public void testPostDeleteNoAuthDeleteSuccess() {
	
        // ----------- Erfolgreiches Anlegen eines Users ---------------
		// gebe JSON Content als String an.
		String content = "{'email':'thomas.tomatenkop@gmx.de','username':'popo','password':'1234','name':'thomas tomatenkop'}";
		
		// sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /users.
		ClientResponse response2 = resource().path("users").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response2.getStatus());
		
		WebResource r2 = resource(); 

        r2.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "falsch")); 
		
		ClientResponse response3 = r2.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response3.getStatus());
        
        WebResource r3 = resource(); 

        r3.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "1234")); 
		
        ClientResponse response4 = r3.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response4.getStatus());
	}
	
	
	
	@Test
	/*
	 * Erstellt erfolgreich einen User. Versucht den User ein zweites mal zu erstellen -> 409 Confilct.
	 * Danach wird der User wieder gelöscht.
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/			POST	201 (neuer User wurde erfolgreich angelegt)
	 *   - /users/			POST	409 (Benutzer wird versucht erneut anzulegen)	
	 *   - /users/{email}	DELETE	200 (User erflogreih gelöscht)
	 *  
	 **/
	
	public void testPostPostAgainDelete(){
		// ----------- Erfolgreiches Anlegen eines Users ---------------
		// gebe JSON Content als String an.
		String content = "{'email':'thomas.tomatenkop@gmx.de','username':'popo','password':'1234','name':'thomas tomatenkop'}";
				
		// sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /users.
		ClientResponse response = resource().path("users").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
				
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		// ----------- Erneutes Anlegen des Users ---------------
				
		// sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /users.
		ClientResponse response2 = resource().path("users").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
				
		// teste, ob der spezifizierte HTTP Status 409 (Conflict) zurückgeliefert wurde.
		assertEquals(Status.CONFLICT.getStatusCode(), response2.getStatus());
		
		WebResource r = resource(); 

        r.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "1234")); 
		
        ClientResponse response4 = r.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response4.getStatus());
	}
	
	
	
	
	
	@Test
	/*
	 * Versucht einen User mit fehlenden Parametern anzulegen -> Bad Request
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /users/			POST	400 (neuer User kann nicht erstellt werden da Parameter fehlen)
	 *  
	 **/
	
	public void testPostMissing(){
	// ----------- Anlegen eines Users mit fehlenden Parametern---------------
			// gebe JSON Content als String an.
			String content = "{'email':'thomas.tomatenkop@gmx.de','name':'thomas tomatenkop'}";
					
			// sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /users.
			ClientResponse response = resource().path("users").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
					
			// teste, ob der spezifizierte HTTP Status 400 (Bad Request) zurückgeliefert wurde.
			assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	/*
	 * Erstellt erfolgreich einen User. Versucht einen User ohne Authorisierung up-zu-daten.
	 * Führt zu einem Unathorized. Updatet den User erfolgreich. Gibt den User aus.
	 * Löscht den User erfolgreich. Versucht den nicht mehr existierenden User zu updaten.
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 *
	 *   - /users/			POST	201 (Erstellt einen User)	
	 *   - /users/{email}	PUT		401 (versucht User unathorisiert zu updaten)
	 *   - /users/{email}	PUT		200 (Updatet den User erfolgreich)
	 *   - /users/{email}	GET		200 (Gibt den User aus)
	 *   - /users/{email}	DELETE	200	(löscht den User)
	 *   - /users/{emial}	PUT		404	(versucht nicht existierenden User zu updaten)
	 *  
	 **/
	
	public void testPostUpdateUnauthorizedUpdateGetDeleteUpdate(){
		// ----------- Erfolgreiches Anlegen eines Users ---------------
		// gebe JSON Content als String an.
		String content = "{'email':'thomas.tomatenkop@gmx.de','username':'popo','password':'1234','name':'thomas tomatenkop'}";
						
		// sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /users.
		ClientResponse response = resource().path("users").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
						
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		
		// ----------- Updaten ohne Authorisierung ---------------

		String content2 = "{'password':'neuesPW','name':'sven gurkenkop'}";
	
        ClientResponse response2 = resource().path("users/thomas.tomatenkop@gmx.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
        
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response2.getStatus());
        
     // ----------- Updaten success ---------------
        WebResource r2 = resource(); 

        r2.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "1234")); 
		
        ClientResponse response3 = r2.path("users/thomas.tomatenkop@gmx.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
        
        assertEquals(Status.OK.getStatusCode(), response3.getStatus());
        
     // ----------- Delete mit neuem Password ---------------
        WebResource r3 = resource(); 

        r3.addFilter(new HTTPBasicAuthFilter("thomas.tomatenkop@gmx.de", "neuesPW"));
        
        ClientResponse response4 = r3.path("users/thomas.tomatenkop@gmx.de").delete(ClientResponse.class);
        assertEquals(Status.OK.getStatusCode(), response4.getStatus());
     
     // ----------- Update non existing ---------------
        ClientResponse response5 = r2.path("users/thomas.tomatenkop@gmx.de").type(MediaType.APPLICATION_JSON).put(ClientResponse.class,content2);
        
        assertEquals(Status.NOT_FOUND.getStatusCode(), response5.getStatus());
        

	}

}
