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
        @NamedQuery(name = "Medium.allRandom", query = "SELECT m FROM Medium m ORDER BY RAND()"),
        @NamedQuery(name = "Medium.findAll", query = "SELECT m FROM Medium m"),
        @NamedQuery(name = "Medium.findMedium", query = "SELECT m FROM Medium m where m.id=:id")
})


//Alle Eigenschaften eines Mediums werden definiert

@XmlRootElement
public class Medium {
         
//Id ist PRIMARY
	
        @Id
        @Column(name = "id", nullable = false)
        private int id;
        
        @Column(name = "url", nullable = false)
        private String url;
               
        @Column(name = "value", nullable = false)
        private int value;
       
        @Column(name = "description", nullable = false)
        private String description;


//Getter+Setter Methoden
       
        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
    	}
        
        public void setUrl(String url) {
                this.url = url;
        }

        public String getUrl() {
                return url;
        }
       
        public void setValue(int value) {
                this.value = value;
        }

        public int getValue() {
                return value;
        }
       
        public void setDescription(String description) {
                this.description = description;
        }
       
        public String getDescription() {
                return description;
        }
}

