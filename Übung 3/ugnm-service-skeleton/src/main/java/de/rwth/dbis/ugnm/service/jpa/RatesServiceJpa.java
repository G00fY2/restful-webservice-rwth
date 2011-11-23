package de.rwth.dbis.ugnm.service.jpa;

import java.sql.Timestamp;
import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;
import de.rwth.dbis.ugnm.service.RatesService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("RatesService")
public class RatesServiceJpa implements RatesService{
        
        private EntityManager entityManager;

        @PersistenceContext
        public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public EntityManager getEntityManager() {
                return entityManager;
        }
        
        
        
        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean save(Rates rating) {
                entityManager.persist(rating);
                entityManager.flush();
                
                return true;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public List<Rates> getAllRatesUser(String fkEmail) {
                Query query = entityManager.createNamedQuery("Rates.findRatesUser");
                query.setParameter("FKEmail", fkEmail);
                List<Rates> ratings = null;
                ratings = query.getResultList();
                return ratings;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Rates get(String FKEMail, String FKURL){  
                Query queryFindRating = entityManager.createNamedQuery("Rates.findRate");
                queryFindRating.setParameter("FKEMail", fkEmail);
                queryFindRating.setParameter("FKURL", fkUrl);
                queryFindRating.setParameter("Zeit", zeit);
                List<Rates> ratings = queryFindRating.getResultList();
                Rates result = null;
                if(ratings.size() > 0) {
                        result = ratings.get(0);
                }
                return result;
        }

        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean delete(Rates rating) {
                rating = entityManager.getReference(Rates.class, rating.RatesID());
                if (rating == null)
                        return false;
                entityManager.remove(rating);
                entityManager.flush();
                return true;
        }


        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Rates findRating(Rates rating) {
                Rates result = null;
                Query queryFindMedia = entityManager.createNamedQuery("Rates.findRating");
                queryFindMedia.setParameter("ratesId", rating.getRatesID());
                List<Rates> ratings = queryFindMedia.getResultList();
                if(ratings.size() > 0) {
                        result = ratings.get(0);
                }
                return result;
        }

}
