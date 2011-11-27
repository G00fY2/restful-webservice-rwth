package de.rwth.dbis.ugnm.entity;


import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "Rates")
@NamedQueries({ 
        @NamedQuery(name = "Rates.findAll", query = "SELECT r FROM Rates r"),
        @NamedQuery(name = "Rates.findRate", query = "SELECT r FROM Rates r where r.mediumUrl=:mediumUrl and r.userEmail=:userEmail and r.time=:time"),
        @NamedQuery(name = "Rates.findRatesOfUser", query = "SELECT r FROM Rates r where r.userEmail=:userEmail")
})


@XmlRootElement
public class Rates {
         
        @Id
        @Column(name = "id", nullable = false)
        private int id;
        
        @Column(name = "time")
        private Timestamp time;
        
        @Column(name = "rate", nullable = false)
        private int rate;
        
        @Column(name = "mediumUrl", nullable = false)
        private String mediumUrl;
       
        @Column(name = "userEmail", nullable = false)
                private String userEmail;

        
        @ManyToOne
        @JoinColumn(name="userEmail", referencedColumnName="email", insertable = false, updatable = false)
        private User userInstance;
        
        @ManyToOne
        @JoinColumn(name="mediumUrl", referencedColumnName="url", insertable = false, updatable = false)
        private Medium mediumInstance;
        
        //Getter+Setter Methoden:
        
        public User getUserInstance() {
                return userInstance;
        }
        public Medium getMediumInstance() {
                return mediumInstance;
        }
        
        public void setId(int id) {
                this.id = id;
        }

        public int getId() {
                return id;
        }
       
        public Timestamp getTime(){
            return time;
        }
    
        public void setTime(Timestamp time){
            this.time = time;
        }
       
        public int getRate() {
            return rate;
        }
    
        public void setRate(int rate) {
            this.rate = rate;
        }
        
        public void setMediumUrl(String mediumUrl) {
            this.mediumUrl = mediumUrl;
        }
    
        public String getMediumUrl(){
            return mediumUrl;
    	}

        public void setUserEmail(String userEmail) {
                this.userEmail = userEmail;
        }

        public String getUserEmail() {
                return userEmail;
        }   

}