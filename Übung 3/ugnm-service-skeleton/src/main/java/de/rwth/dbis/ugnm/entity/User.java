package de.rwth.dbis.ugnm.entity;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USER")
@NamedQueries({ 
	@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
	@NamedQuery(name = "User.findUser", query = "SELECT u FROM User u where u.Benutzername=:Benutzername")
})
@XmlRootElement
public class User {
	
	@Column(name = "Passwort", nullable = false)
	private String passwort;
	
	@Column(name = "Benutzername", nullable = false)
	private String benutzername;
	
	@Column(name = "EP", nullable = false)
	private int ep;
	
	@Column(name = "Vorname", nullable = false)
	private String vorname;
	
	@Column(name = "Nachname", nullable = false)
	private String nachname;
	
	@Id
	@Column(name = "EMail", nullable = false)
	private String email;
		
	

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}

	public String getBenutzername() {
		return benutzername;
	}
	
	public void setEP(int ep) {
		this.ep = ep;
	}

	public int getEP() {
		return ep;
	}
	
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getVorname() {
		return vorname;
	}
	
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getNachname() {
		return nachname;
	}
	
	public void setEMail(String email) {
		this.email = email;
	}

	public String getEMail() {
		return email;
	}
}
