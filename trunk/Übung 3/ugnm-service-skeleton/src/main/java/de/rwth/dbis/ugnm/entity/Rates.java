package de.rwth.dbis.ugnm.entity;

import java.sql.Timestamp;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "rates")
@NamedQueries({ 
        @NamedQuery(name = "Rates.findAll", query = "SELECT r FROM Rates r"),
        @NamedQuery(name = "Rates.findRate", query = "SELECT r FROM Rates r where r.RatesID=:RatesID"),
        @NamedQuery(name = "Rates.findRatesUser", query = "SELECT r FROM Rates r where r.fkEmail=:fkEmail")

})

@XmlRootElement
public class Rates {
       
 
        @Id
        @Column(name = "RatesID", nullable = false)
        private int ratesId;
        
        @Column(name = "Zeit", nullable = false)
        private Timestamp zeit;
        
        @Column(name = "Rate", nullable = false)
        private int rate;
        
        @Column(name = "FKURL", nullable = false)
        private String fkUrl;
        
        @Column(name = "FKEMail", nullable = false)
        private String fkEmail;


        public void setRatesID(int ratesId) {
                this.ratesId = ratesId;
        }

        public int getRatesID() {
                return ratesId;
        }

        public void setZeit(Timestamp zeit) {
                this.zeit = zeit;
        }

        public Timestamp getZeit() {
                return zeit;
        }
        
        public void setRate(int rate) {
                this.rate = rate;
        }

        public int getRate() {
                return rate;
        }
        
        public void setFKURL(String fkUrl) {
                this.fkUrl = fkUrl;
        }

        public String getFKURL() {
                return fkUrl;
        }
        
        public void setFKEMail(String fkEmail) {
                this.fkEmail = fkEmail;
        }

        public String getFKEMail() {
                return fkEmail;
        }
        
        @ManyToOne
        @JoinColumn(name="USER_MAIL", referencedColumnName="MAIL", insertable = false, updatable = false)
        private User userInstance;
        
        @ManyToOne
        @JoinColumn(name="MEDIUM_URL", referencedColumnName="URL", insertable = false, updatable = false)
        private Medium mediumInstance;
        
        public User getUserInstance() {
                return userInstance;
        }
        public Medium getMediumInstance() {
                return mediumInstance;
        }
}