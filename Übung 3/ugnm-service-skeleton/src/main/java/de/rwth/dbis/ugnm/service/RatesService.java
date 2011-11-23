package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;

public interface RatesService {
        public boolean save(Rates rating);
        public List<Rates> getAllRatesUser(String FKEMail);
        public boolean delete(Rates rating);
        public Rates findRating(int ratesId);
}