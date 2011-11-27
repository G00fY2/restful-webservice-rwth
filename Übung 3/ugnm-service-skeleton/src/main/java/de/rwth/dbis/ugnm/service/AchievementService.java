package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.Achievement;

public interface AchievementService{
        public boolean save(Achievement achievement);
        public List<Achievement> getAll();
        public Achievement getById(int id);
        public boolean delete(Achievement achievement);
        public boolean update(Achievement achievement);
        public Achievement findAchievement(Achievement achievement);
}

