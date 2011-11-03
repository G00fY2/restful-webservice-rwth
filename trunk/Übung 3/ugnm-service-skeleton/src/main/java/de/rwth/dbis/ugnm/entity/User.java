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
	@NamedQuery(name = "User.findUser", query = "SELECT u FROM User u where u.benutzername=:benutzername")
})
@XmlRootElement
public class User {
	
	@Column(name = "Passwort", nullable = false)
	private String pass;
	
	@Column(name = "Benutzername", nullable = false)
	private String benutzername;
	
	@Column(name = "Ep", nullable = false)
	private int ep;
	
	@Column(name = "Vorname", nullable = false)
	private String vorname;

	@Column(name = "Nachname", nullable = false)
	private String nachname;

	@Column(name = "E-Mail", nullable = false)
	private String mail;


	@Column(name = "Achievements", nullable = false)
	private String achievements;


	public void setPasswort(String pass) {
		this.pass = pass;
	}

	public String getPasswort() {
		return pass;
	}
	
	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}

	public String getBenutzername() {
		return benutzername;
	}
	
	public void setEP(String ep) {
		this.ep = ep;
	}

	public String getEP() {
		return ep;
	}

	public void setVorname(String vorname) {
		this.ep = vorname;
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

	public void setEMail(String mail) {
		this.mail = mail;
	}

	public String getEMail() {
		return mail;
	}

	public void setAchievements(String achievement) {
		this.achievement = achievement;
	}

	public String getAchievements() {
		return achievement;
	}
}
