package de.rwth.dbis.ugnm.entity;

import java.sql.Timestamp;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "rates")
@NamedQueries({ 
        @NamedQuery(name = "Rates.findAll", query = "SELECT r FROM Rates r"),
        @NamedQuery(name = "Rates.findRate", query = "SELECT r FROM Rates r where r.RatesID=:RatesID")
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
        
        @Column(name = "MediumURL", nullable = false)
        private String mediumUrl;
        
        @Column(name = "UserEMail", nullable = false)
        private String userEmail;


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
        
        public void setMediumURL(String mediumUrl) {
                this.mediumUrl = mediumUrl;
        }

        public String getMediumURL() {
                return mediumUrl;
        }
        
        public void setUserEMail(String userEmail) {
                this.userEmail = userEmail;
        }

        public String getUserEMail() {
                return userEmail;
        }
}