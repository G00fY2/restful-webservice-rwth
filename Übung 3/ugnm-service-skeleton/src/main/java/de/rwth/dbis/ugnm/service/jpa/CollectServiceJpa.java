package de.rwth.dbis.ugnm.service.jpa;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Collect;
import de.rwth.dbis.ugnm.service.CollectService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("CollectService")
public class CollectServiceJpa implements CollectService{

        private EntityManager entityManager;

        @PersistenceContext
        public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public EntityManager getEntityManager() {
                return entityManager;
        }
        
        
//SAVE Achievement
        
        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean save(Collect collect) {
                entityManager.persist(collect);
                entityManager.flush();
                
                return true;
        }

        
//GET Liste aller Achievements eines Users
        
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public List<Collect> getAllAchievementsOfUser(String email) {
                Query query = entityManager.createNamedQuery("Collect.findCollectOfUser");
                query.setParameter("userEmail", email);
                List<Collect> collects = null;
                collects = query.getResultList();
                return collects;
        }

        
//Gibt über findCollect ein einzelnes collect zu einem User aus 
        
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Collect findCollect(String userEmail, int achievementId) {
                Query queryFindMedia = entityManager.createNamedQuery("Collect.findCollect");
                queryFindMedia.setParameter("achievementId", achievementId);
                queryFindMedia.setParameter("userEmail", userEmail);
                List<Collect> collects = queryFindMedia.getResultList();
                Collect result = null;
                if(collects.size() > 0) {
                        result = collects.get(0);
                }
                return result;
        }
        
//Löscht über DELETE ein Achievement 
        
        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean delete(Collect collect) {
                collect = entityManager.getReference(Collect.class, collect.getId());
                if (collect == null)
                        return false;
                entityManager.remove(collect);
                entityManager.flush();
                return true;
        }

}

