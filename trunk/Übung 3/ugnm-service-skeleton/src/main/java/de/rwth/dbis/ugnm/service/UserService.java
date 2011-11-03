package de.rwth.dbis.ugnm.service;

import java.util.List;

import de.rwth.dbis.ugnm.entity.User;

public interface UserService {
	public boolean save(User user);
	public List<User> getAll();
	public User getByBenutzername(String benutzername);
	public User getByVorname(String vorname);
	public User getByNachname(String nachname);
	public boolean delete(User user);
	public boolean update(User user);
	public User findUser(User user);
}

