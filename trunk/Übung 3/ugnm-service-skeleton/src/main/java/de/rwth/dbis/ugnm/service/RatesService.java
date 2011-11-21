package de.rwth.dbis.ugnm.service;


import java.sql.Timestamp;
import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;

public interface RatesService {
        public boolean save(Rates rating);
        public List<Rates> getAll();
        public Rates get(String userEmail, String mediumUrl, Timestamp zeit);
        public boolean delete(Rates rating);
        public boolean update(Rates rating);
        public Rates findRating(Rates rating);
}