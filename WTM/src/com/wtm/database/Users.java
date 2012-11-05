package com.wtm.database;

public class Users implements Comparable<Users> {
	private String name;
	private String password;

	public Users(String username, String pass) {
		name = username;
		password = pass;
	}

	public Users() {
		name = "";
		password = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return password;
	}
	
	public String getEncryptedPwd() {
		return password+password.charAt(0);
	}


	public void setEncryptedPwd(String encrypt) {
		password = encrypt.substring(0, encrypt.length()-1);
	}

	
	public void setPwd(String pwd) {
		this.password = pwd;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Users user) {
		return name.compareTo(user.getName());
	}
	
	private String getMobileFormatPwd(){
		return password+password.charAt(0);
	}
	
	public String toSyncString()
	{
		return getName()+"\t"+getMobileFormatPwd()+"\n";
	}
}
