package de.rwth.dbis.ugnm.service;


import java.util.List;

import de.rwth.dbis.ugnm.entity.Rates;

public interface RatesService {
        public boolean save(Rates rate);
        public List<Rates> getAllRatesOfUser(String email);
        public Rates getRateById(int id);
}
