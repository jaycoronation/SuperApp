package com.application.alphacapital.superapp.pms.beans;

public class Users{
	private String password;
	private String role_id;
	private String user_name;
	private String admin_id;
	private String last_name;
	private String first_name;
	private String email;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setRole_id(String role_id){
		this.role_id = role_id;
	}

	public String getRole_id(){
		return role_id;
	}

	public void setUser_name(String user_name){
		this.user_name = user_name;
	}

	public String getUser_name(){
		return user_name;
	}

	public void setAdmin_id(String admin_id){
		this.admin_id = admin_id;
	}

	public String getAdmin_id(){
		return admin_id;
	}

	public void setLast_name(String last_name){
		this.last_name = last_name;
	}

	public String getLast_name(){
		return last_name;
	}

	public void setFirst_name(String first_name){
		this.first_name = first_name;
	}

	public String getFirst_name(){
		return first_name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}
