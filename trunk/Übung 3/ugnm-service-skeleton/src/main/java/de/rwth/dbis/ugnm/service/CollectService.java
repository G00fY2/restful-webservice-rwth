package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Collect;

public interface CollectService{
        public boolean save(Collect collect);
        public List<Collect> getAllAchievementsOfUser(String userEmail);
        public Collect findCollect(String userEmail, int achievementId);
        public boolean delete(Collect collect);
}