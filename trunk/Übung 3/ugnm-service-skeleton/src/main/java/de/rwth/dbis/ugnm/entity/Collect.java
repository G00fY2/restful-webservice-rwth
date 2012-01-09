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

@Entity
@Table(name = "Collect")
@NamedQueries({ 
        @NamedQuery(name = "Collect.findCollect", query = "SELECT c FROM Collect c where c.userEmail=:userEmail and c.achievementId=:achievementId"),
        @NamedQuery(name = "Collect.findCollectOfUser", query = "SELECT c FROM Collect c where c.userEmail=:userEmail")
})

//Alle Eigenschaften von Collect werden definiert

@XmlRootElement
public class Collect {
 
//Id ist Primary	
	
        @Id
        @Column(name = "id", nullable = false)
        private int id;
                
        @Column(name = "userEmail", nullable = false)
        private String userEmail;
        
        @Column(name = "achievementId", nullable = false)
        private int achievementId;
        
        @ManyToOne
        @JoinColumn(name="userEmail", referencedColumnName="email", insertable = false, updatable = false)
        private User userInstance;
        
        @ManyToOne
        @JoinColumn(name="achievementId", referencedColumnName="id", insertable = false, updatable = false)
        private Achievement achievementInstance;
        
//Getter-Setter Methoden

        public int getId() {
                return id;
        }
        
        public void setUserEmail(String email) {
                this.userEmail = email;
        }

        public String getUserEmail() {
                return userEmail;
        }
        
        public void setAchievementId(int id) {
                this.achievementId = id;
        }
        
        public int getAchievementId(){
                return achievementId;
        }
        
        public User getUserInstance() {
            return userInstance;
        }
    
        public Achievement getAchievementInstance() {
            return achievementInstance;
    }
    
}