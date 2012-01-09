package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Collect;

public interface CollectService{
        public boolean save(Collect collect);
        public List<Collect> getAll(String userEmail);
        public Collect getById(int id);
        public boolean delete(Collect collect);
}