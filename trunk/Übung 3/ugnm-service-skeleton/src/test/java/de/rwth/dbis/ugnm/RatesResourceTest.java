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
	 * sendet einen GET Request an die Ressource /media. 
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media			GET		200	(Liste aller User erfolgreich geholt)
	 **/
    
    
    
	public void testGetSuccess() {
		// sende GET Request an Ressource /users und erhalte Antwort als Instanz der Klasse ClientResponse
		ClientResponse response = resource().path("/users/sven.hausburg@rwth-aachen.de/rates").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		// teste, ob die gelieferten Daten den entsprechenden MIME Typ für JSON aufweisen.
        assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON);
        
        // verarbeite die zurückgelieferten Daten als JSON Objekt.
        JSONObject o = response.getEntity(JSONObject.class);
        
        // teste, ob das gelieferte JSON Object ein Feld "users" besitzt.
        assertTrue(o.has("rates"));
        
	}
    
    
	@Test
	/*
	 * führt zuerst für einen nicht existierendes Medium ein DELETE aus. Dies sollte mit 404 fehlschlagen. 
	 * Danach wird dieses Medium mit Post und unter Angabe aller nötigen Parameter auf die Collection Ressource angelegt. 
	 * Dies sollte erfolgreich sein. Danach wird das selbe Medium wieder gelöscht.
	 * 
	 * deckt folgende spezifizierte Fälle ab:
	 * 
	 *   - /media/{url}	DELETE	404	(zu entfernendes Medium existiert nicht)
	 *   - /media/			POST	201 (neues Medium wurde erfolgreich angelegt)
	 *   - /media/{url}	DELETE	200 (bestehendes Medium erfolgreich entfernt)	
	 **/
	
	
	public void testDeletePostDelete() {
		
		// ---------- Delete auf nicht existierendes Medium ------------
		WebResource r = resource(); 
		
		// auf diese Art und Weise kann man eine HTTP Basic Authentifizierung durchführen.
        r.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		
        // sende DELETE Request an nicht existierende Ressource /media/www.medium4.de (sollte vor dem Test nicht existieren)
		ClientResponse response = r.path("/users/sven.hausburg@rwth-aachen.de/rates/IDIDID").delete(ClientResponse.class);
		
		// teste, ob der spezifizierte HTTP Status 404 (Not Found) zurückgeliefert wurde. 
        assertEquals(response.getStatus(), Status.NOT_FOUND.getStatusCode());
	
        // ----------- Erfolgreiches Anlegen eines Mediums ---------------
        
		// gebe JSON Content als String an.
		String content = "{'url':'www.medium2.de','rate':1}";
		
		// sende POST Request inkl. validem Content und unter Angabe des MIME Type application/json an Ressource /media.
		ClientResponse response2 = resource().path("/users/sven.hausburg@rwth-aachen.de/rates").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,content);
		
		// teste, ob der spezifizierte HTTP Status 201 (Created) zurückgeliefert wurde.
		assertEquals(response2.getStatus(), Status.CREATED.getStatusCode());
		
		WebResource r2 = resource(); 

        r2.addFilter(new HTTPBasicAuthFilter("sven.hausburg@rwth-aachen.de", "abc123")); 
		
		ClientResponse response3 = r.path("/users/sven.hausburg@rwth-aachen.de/rates/IDIDID").delete(ClientResponse.class);
        assertEquals(response3.getStatus(), Status.OK.getStatusCode());
	}
}