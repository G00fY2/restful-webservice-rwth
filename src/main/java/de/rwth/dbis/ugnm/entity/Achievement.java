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
        @NamedQuery(name = "Achievement.findAchievement", query = "SELECT a FROM Achievement a where a.id=:id")
})

//Alle Eigenschaften eines Achievements werden definiert

@XmlRootElement
public class Achievement {
 
//Id ist Primary	
	
        @Id
        @Column(name = "id", nullable = false)
        private int id;
                
        @Column(name = "description", nullable = false)
        private String description;
        
        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "url", nullable = false)
        private String url;
        
        
//Getter-Setter Methoden
        
        public void setId(int id) {
                this.id = id;
        }

        public int getId() {
                return id;
        }
        
        public void setDescription (String description) {
                this.description  = description;
        }

        public String getDescription () {
                return description;
        }
        
        public void setName(String name) {
                this.name = name;
        }

        public String getName() {
                return name;
        }
        
        public void setUrl(String url) {
                this.url = url;
        }

        public String getUrl() {
                return url;
        }

}