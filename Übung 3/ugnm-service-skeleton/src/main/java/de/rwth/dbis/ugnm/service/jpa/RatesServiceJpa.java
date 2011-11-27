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


@Service("ratesService")
public class RatesServiceJpa implements RatesService{
        
        // the EntityManager:
        private EntityManager entityManager;

        @PersistenceContext
        public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public EntityManager getEntityManager() {
                return entityManager;
        }
        
        
        // save a rating association
        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean save(Rates rate) {
                entityManager.persist(rate);
                entityManager.flush();
                
                return true;
        }

        // get all rates of one user
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public List<Rates> getAllRatesOfUser(String userEmail) {
                Query query = entityManager.createNamedQuery("Rates.findRatesOfUser");
                query.setParameter("userEmail", userEmail);
                List<Rates> rates = null;
                rates = query.getResultList();
                return rates;
        }

        // get a rating association of one medium and user
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Rates get(String userEmail, String mediumUrl, Timestamp time){  
                Query queryFindRate = entityManager.createNamedQuery("Rates.findRate");
                queryFindRate.setParameter("userEmail", userEmail);
                queryFindRate.setParameter("mediumUrl", mediumUrl);
                queryFindRate.setParameter("time", time);
                List<Rates> rates = queryFindRate.getResultList();
                Rates result = null;
                if(rates.size() > 0) {
                        result = rates.get(0);
                }
                return result;
        }

        // delete a rating association
        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean delete(Rates rate) {
                rate = entityManager.getReference(Rates.class, rate.getId());
                if (rate == null)
                        return false;
                entityManager.remove(rate);
                entityManager.flush();
                return true;
        }


        // find a rating association by the id
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Rates findRate(int id) {
                Rates result = null;
                Query queryFindMedia = entityManager.createNamedQuery("Rates.findRate");
                queryFindMedia.setParameter("id", id);
                List<Rates> rates = queryFindMedia.getResultList();
                if(rates.size() > 0) {
                        result = rates.get(0);
                }
                return result;
        }
        
        
        //Not implemented, but might make sense!
        @Override
        public List<Rates> getAllRatesOfMedium(String URL) {
                // TODO Auto-generated method stub
                return null;
        }

}
