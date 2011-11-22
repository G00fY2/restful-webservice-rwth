package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;

public interface RatesService {
        public boolean save(Rates rating);
        public List<Rates> getAllRatesUser(String EMail);
        public Rates getByRatesID(int id);
        public boolean delete(Rates rating);
        public boolean update(Rates rating);
        public Rates findRating(Rates rating);
}