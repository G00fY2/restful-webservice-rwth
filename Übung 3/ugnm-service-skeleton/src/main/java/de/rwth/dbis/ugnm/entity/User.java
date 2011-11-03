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
	@NamedQuery(name = "User.findUser", query = "SELECT u FROM User u where u.login=:login")
})
@XmlRootElement
public class User {
	
	@Id
	@Column(name = "ID", nullable = false)
	private int id;
	
	@Column(name = "LOGIN")
	private String login;
	
	@Column(name = "PASS")
	private String pass;
	
	@Column(name = "NAME")
	private String name;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPass() {
		return pass;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
