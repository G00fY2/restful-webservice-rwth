package de.rwth.dbis.ugnm.service.jpa;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Medium;
import de.rwth.dbis.ugnm.service.MediumService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("mediumService")
public class MediumServiceJpa implements MediumService{
        
        private EntityManager entityManager;

        @PersistenceContext
        public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public EntityManager getEntityManager() {
                return entityManager;
        }
        
        
        
        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean save(Medium medium) {
                entityManager.persist(medium);
                entityManager.flush();
                
                return true;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public List<Medium> getAll() {
                Query query = entityManager.createNamedQuery("Medium.findAll");
                List<Medium> medium = null;   //hier users durch medium ersetzt
                medium = query.getResultList();
                return medium;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Medium getByURL(String url) {
                Query queryFindMedia = entityManager.createNamedQuery("Medium.findMedium");
                queryFindMedia.setParameter("url", url);
                List<Medium> media = queryFindMedia.getResultList();
                Medium result = null;
                if(media.size() > 0) {
                        result = media.get(0);
                }
                return result;
        }

        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean delete(Medium medium) {
                medium = entityManager.getReference(Medium.class, medium.getURL());
                if (medium == null)
                        return false;
                entityManager.remove(medium);
                entityManager.flush();
                return true;
        }

        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean update(Medium medium) {
                entityManager.merge(medium);
                entityManager.flush();
                return true;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Medium findMedium(Medium medium) {
                Medium result = null;
                Query queryFindMedia = entityManager.createNamedQuery("Medium.findMedium");
                queryFindMedia.setParameter("url", medium.getURL());

                List<Medium> media = queryFindMedia.getResultList();
                if(media.size() > 0) {
                        result = media.get(0);
                }
                return result;
        }

}
