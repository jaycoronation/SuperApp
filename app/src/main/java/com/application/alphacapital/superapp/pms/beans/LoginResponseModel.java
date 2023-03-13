package com.application.alphacapital.superapp.pms.beans;

public class LoginResponseModel{
	private int success;
	private String message;
	private Users users;

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUsers(Users users){
		this.users = users;
	}

	public Users getUsers(){
		return users;
	}
}
