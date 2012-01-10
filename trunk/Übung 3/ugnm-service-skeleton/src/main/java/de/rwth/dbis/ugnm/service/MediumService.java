package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Medium;

public interface MediumService{
        public boolean save(Medium medium);
        public List<Medium> getAll();
        public List<Medium> getAllRandom();
        public Medium getById(int id);
        public boolean delete(Medium medium);
        public boolean update(Medium medium);
        public Medium findMedium(Medium medium);
}
