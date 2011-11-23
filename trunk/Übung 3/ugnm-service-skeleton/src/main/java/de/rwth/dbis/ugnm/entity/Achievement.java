package de.rwth.dbis.ugnm.entity;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Achievement")
@NamedQueries({ 
        @NamedQuery(name = "Achievement.findAll", query = "SELECT a FROM Achievement a"),
        @NamedQuery(name = "Achievement.findUser", query = "SELECT a FROM Achievement a where a.identifier=:identifier")
})

//Alle Eigenschaften des Achievements werden Definiert

@XmlRootElement
public class Achievement{
        
        @Column(name = "Bild", nullable = false)
        private String bild;
        
        @Column(name = "Beschreibung", nullable = false)
        private String beschreibung;
        
        @Column(name = "Name", nullable = false)
        private String name;


//Identifier ist PRIMARY 


        @Id
        @Column(name = "Identifier", nullable = false)
        private int identifier;
        

//Getter+Setter Methoden

        public void setBild(String bild) {
                this.bild = bild;
        }

        public String getBild() {
                return bild;
        }

        public void setBeschreibung(String beschreibung) {
                this.beschreibung = beschreibung;
        }

        public String getBeschreibung() {
                return beschreibung;
        }
        
        public void setName(String name) {
                this.name = name;
        }

        public String getName() {
                return name;
        }
        
        public void setIdentifier(int identifier) {
                this.identifier = identifier;
        }

        public int getIdentifier() {
                return identifier;
        }
}