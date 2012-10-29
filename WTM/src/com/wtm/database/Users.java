package com.wtm.database;

public class Users {
	  private String name;
	  private String encryptedPwd;

	  Users(String username, String password){
		  name = username;
		  encryptedPwd = password;
	  }
	  
	  Users(){
		  name = "";
		  encryptedPwd= "";
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  public String getPwd() {
		  return encryptedPwd;
	  }
	  
	  public void setPwd(String pwd) {
		  this.encryptedPwd = pwd;
	  }

	  @Override
	  public String toString() {
	    return name;
	  }
} 
