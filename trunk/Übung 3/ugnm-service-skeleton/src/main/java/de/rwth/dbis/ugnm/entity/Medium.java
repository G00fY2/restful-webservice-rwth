package de.rwth.dbis.ugnm.entity;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Medium")
@NamedQueries({ 
        @NamedQuery(name = "Medium.findAll", query = "SELECT m FROM Medium m"),
        @NamedQuery(name = "Medium.findMedium", query = "SELECT m FROM Medium m where m.url=:url")
})

//Alle Eigenschaften des Mediums werden Definiert

@XmlRootElement
public class Medium {
        
        @Column(name = "Beschreibung", nullable = false)
        private String beschreibung;
        
        @Column(name = "Value", nullable = false)
        private int value;
        
//URL ist PRIMARY         
        
        @Id
        @Column(name = "URL", nullable = false)
        private String url;
                
                
//Getter+Setter Methoden        

        public void setBeschreibung(String beschreibung) {
                this.beschreibung = beschreibung;
        }

        public String getBeschreibung() {
                return beschreibung;
        }

        public void setValue(int value) {
                this.value = value;
        }

        public int getValue() {
                return value;
        }
        
        public void setURL(String url) {
                this.url = url;
        }

        public String getURL() {
                return url;
        }
}