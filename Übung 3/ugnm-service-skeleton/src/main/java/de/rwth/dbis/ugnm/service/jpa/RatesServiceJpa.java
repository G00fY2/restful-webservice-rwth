package de.rwth.dbis.ugnm.service.jpa;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;
import de.rwth.dbis.ugnm.service.RatesService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("ratesService")
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
        public boolean save(Rates rate) {
                entityManager.persist(rate);
                entityManager.flush();
                
                return true;
        }

        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public List<Rates> getAllRatesUser(String email) {
                Query query = entityManager.createNamedQuery("Rates.findRatesUser");
                query.setParameter("fkEmail", email);
                List<Rates> rates = null;
                rates = query.getResultList();
                return rates;
        }

        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean delete(Rates rating) {
                rating = entityManager.getReference(Rates.class, rating.getRatesID());
                if (rating == null)
                        return false;
                entityManager.remove(rating);
                entityManager.flush();
                return true;
        }


        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Rates findRating(int ratesId) {
                Rates result = null;
                Query queryFindMedia = entityManager.createNamedQuery("Rates.findRating");
                queryFindMedia.setParameter("ratesId", ratesId);
                List<Rates> rates = queryFindMedia.getResultList();
                if(rates.size() > 0) {
                        result = rates.get(0);
                }
                return result;
        }

}
