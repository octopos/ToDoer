package com.wtm.database;


import java.util.ArrayList;
import java.util.List;

public class UserDataSource {

	  // Database fields

	  public UserDataSource() {
	    
	  }

	  
	  public Users selectUser(String name) {
		    
		  Users newUser = new Users();
		  return newUser;
	  }
	  
	  public Users getUserById(long id) {
		  Users newUser =  new Users();
		  return newUser;		  
	  }

	  public void updateUsers(Users user) {
		  

	  }
	  
	  public Users createUsers(String name, String pwd) {
	    Users newUser =  new Users();
	    return newUser;
	  }

	  public void deleteUser(Users user) {
	    
	  }

	  public List<Users> getAllUsers() {
	    List<Users> allusers = new ArrayList<Users>();
	    return allusers;
	  }

}