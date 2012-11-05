package com.wtm.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class UserDataSource {

	// Database fields
	DatastoreService datastore;
	Key dbKey;

	public UserDataSource() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		dbKey = KeyFactory.createKey("CDB", "userDB");
	}

	public boolean canLogin(String name, String pswd) {
		Filter existFltr = new FilterPredicate("Username",
				FilterOperator.EQUAL, name);
		Query query = new Query("users", dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));
		if (usersExist.size() == 1) {
			Entity thisUser = usersExist.get(0);
			String returnedPassword = (String) thisUser.getProperty("Password");
			if (pswd.equals(returnedPassword)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public Users selectUser(String name) {
		Filter existFltr = new FilterPredicate("Username",
				FilterOperator.EQUAL, name);
		Query query = new Query("users", dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));
		Users returnedUser = null;

		if (usersExist.size() == 1) {
			returnedUser = entityToUser(usersExist.get(0));
		}
		return returnedUser;
	}

	public boolean userExists(String name) {
		Filter existFltr = new FilterPredicate("Username",
				FilterOperator.EQUAL, name);
		Query query = new Query("users", dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));
		if (usersExist.size() != 0)
			return true;
		return false;
	}

	public boolean changePassword(String name, String oldPswd, String newPswd) {
		Filter existFltr = new FilterPredicate("Username",
				FilterOperator.EQUAL, name);
		Query query = new Query("users", dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));

		Entity thisUser = usersExist.get(0);
		if (oldPswd.equals("")
				|| thisUser.getProperty("Password").equals(oldPswd)) {
			thisUser.setProperty("Password", newPswd);
			datastore.put(thisUser);
			return true;
		}
		return false;
	}

	public static long getUserID(String username) {
		long userid = 0;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "userDB");
		Filter existFltr = new FilterPredicate("Username",
				FilterOperator.EQUAL, username);
		Query query = new Query("users", dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));
		if (!usersExist.isEmpty()) {
			for (Entity thisUser : usersExist) {
				userid = thisUser.getKey().getId();
			}
		}
		return userid;
	}

	public Users getUserById(long id) {
		long tempId;
		System.out.println("id=" + id);
		Users newUser = new Users();
		Query query = new Query("users", dbKey).addSort("Username",
				Query.SortDirection.DESCENDING);
		List<Entity> userExists = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());

		if (!userExists.isEmpty()) {
			for (Entity thisUser : userExists) {
				tempId = thisUser.getKey().getId();
				if (tempId == id) {
					newUser = entityToUser(thisUser);
					break;
				}
			}
		}
		return newUser;
	}

	public Users entityToUser(Entity thisEntity) {
		String username = (String) thisEntity.getProperty("Username");
		String password = (String) thisEntity.getProperty("Password");
		Users thisUser = new Users(username, password);
		return thisUser;
	}

	public Users createUsers(String name, String pwd) {
		Entity user = new Entity("users", dbKey);
		user.setProperty("Username", name);
		user.setProperty("Password", pwd);
		datastore.put(user);

		Users newUser = new Users(name, pwd);
		return newUser;
	}

	public void deleteUser(String name) {
		Filter existFltr = new FilterPredicate("Username",
				FilterOperator.EQUAL, name);
		Query query = new Query("users", dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));
		if (!usersExist.isEmpty()) {
			for (Entity thisUser : usersExist) {
				datastore.delete(thisUser.getKey());
			}
		}
	}

	public List<Users> getAllUsers() {
		Query query = new Query("users", dbKey).addSort("Username",
				Query.SortDirection.DESCENDING);
		List<Entity> usersEntity = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));
		List<Users> users = new ArrayList<Users>();

		for (Entity thisUser : usersEntity) {
			users.add(entityToUser(thisUser));
		}
		Collections.sort(users);
		return users;
	}

	
}