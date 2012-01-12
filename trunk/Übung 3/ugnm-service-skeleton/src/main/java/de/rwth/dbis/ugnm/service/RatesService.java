package de.rwth.dbis.ugnm.service;


import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;

public interface RatesService {
        public boolean save(Rates rate);
        public List<Rates> getAllRatesOfUser(String email);
        public int getAlreadyRated(String email, int id);
        public Rates getRateById(int id);
        public boolean delete(Rates rate);
}
