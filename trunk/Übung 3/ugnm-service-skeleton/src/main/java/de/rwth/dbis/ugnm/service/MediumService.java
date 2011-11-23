package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Medium;

public interface MediumService{
        public boolean save(Medium medium);
        public List<Medium> getAll();
        public Medium getByURL(String url);
        public Medium findMedium(Medium medium);
}
