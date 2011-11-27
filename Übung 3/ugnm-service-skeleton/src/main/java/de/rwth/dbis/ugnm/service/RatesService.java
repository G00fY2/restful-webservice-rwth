package de.rwth.dbis.ugnm.service;


import java.sql.Timestamp;
import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;

public interface RatesService {
        public boolean save(Rates rate);
        public List<Rates> getAllRatesOfUser(String userEmail);
        public List<Rates> getAllRatesOfMedium(String url);
        public Rates get(String userEmail, String mediumUrl, Timestamp time);
        public boolean delete(Rates rate);
        public Rates findRate(int id);
}
