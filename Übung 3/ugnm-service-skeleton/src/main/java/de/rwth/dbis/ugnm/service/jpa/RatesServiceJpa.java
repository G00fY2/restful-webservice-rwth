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
        
//SAVE Rating       

        @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
        public boolean save(Rates rate) {
                entityManager.persist(rate);
                entityManager.flush();
                
                return true;
        }

//GET Liste aller Ratings zu einem User aus
        
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public List<Rates> getAllRatesOfUser(String email) {
                Query query = entityManager.createNamedQuery("Rates.findRatesOfUser");
                query.setParameter("userEmail", email);
                List<Rates> rates = null;
                rates = query.getResultList();
                return rates;
        }


//Gibt ein Rating über id aus
        
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        public Rates getRateById(int id) {
                Rates result = null;
                Query queryFindMedia = entityManager.createNamedQuery("Rates.findRateById");
                queryFindMedia.setParameter("id", id);
                List<Rates> rates = queryFindMedia.getResultList();
                if(rates.size() > 0) {
                        result = rates.get(0);
                }
                return result;
        }

}
