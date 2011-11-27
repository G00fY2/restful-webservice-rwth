package de.rwth.dbis.ugnm.entity;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "User")
@NamedQueries({ 
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findUser", query = "SELECT u FROM User u where u.email=:email")
})


//Alle Eigenschaften eines Users werden definiert

@XmlRootElement
public class User {
	
//EMail ist PRIMARY

		@Id
		@Column(name = "email", nullable = false)
		private String email;
		        
        @Column(name = "username", nullable = false)
        private String username;
        
        @Column(name = "password", nullable = false)
        private String password;
        
        @Column(name = "name", nullable = false)
        private String name;
        
        @Column(name = "ep", nullable = false)
        private int ep;
           
        
//Getter+Setter Methoden

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    
        public void setUsername(String username) {
        	this.username = username;
        }

        public String getUsername() {
        	return username;
        }
        
        public void setPassword(String password) {
                this.password = password;
        }

        public String getPassword() {
                return password;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
        
        public void setEp(int ep) {
                this.ep = ep;
        }

        public int getEp() {
                return ep;
        }
}