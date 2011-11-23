package de.rwth.dbis.ugnm.service.jpa;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Achievement;
import de.rwth.dbis.ugnm.service.AchievementService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("achievementService")
public class AchievementServiceJpa implements AchievementService{
        
        private EntityManager entityManager;

        @PersistenceContext
        public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public EntityManager getEntityManager() {
                return entityManager;
        }
        
        
        
        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean save(Achievement achievement) {
                entityManager.persist(achievement);
                entityManager.flush();
                
                return true;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public List<Achievement> getAll() {
                Query query = entityManager.createNamedQuery("Achievement.findAll");
                List<Achievement> achievements = null;
                achievements = query.getResultList();
                return achievements;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Achievement getByIdentifier(int identifier) {
                Query queryFindMedia = entityManager.createNamedQuery("Achievement.findAchievement");
                queryFindMedia.setParameter("identifier", identifier);
                List<Achievement> achievements = queryFindMedia.getResultList();
                Achievement result = null;
                if(achievements.size() > 0) {
                        result = achievements.get(0);
                }
                return result;
        }

        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean delete(Achievement achievement) {
                achievement = entityManager.getReference(Achievement.class, achievement.getIdentifier());
                if (achievement == null)
                        return false;
                entityManager.remove(achievement);
                entityManager.flush();
                return true;
        }

        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean update(Achievement achievement) {
                entityManager.merge(achievement);
                entityManager.flush();
                return true;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Achievement findAchievement(Achievement achievement) {
                Achievement result = null;
                Query queryFindMedia = entityManager.createNamedQuery("Achievement.findAchievement");
                queryFindMedia.setParameter("identifier", achievement.getIdentifier());

                List<Achievement> achievements = queryFindMedia.getResultList();
                if(achievements.size() > 0) {
                        result = achievements.get(0);
                }
                return result;
        }

}

