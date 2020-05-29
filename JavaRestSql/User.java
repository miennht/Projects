package com.fms.dto;

public class User {
	
	private int 	userCk;
	private String 	userId,
					password,
					phone,
					category,
					description;
	
	public User(){
		
	}
	
	public User(int userCk, String userId, String password, 
				String phone, String category, String description){
		this.userCk = userCk;
		this.password = password;
		this.phone = phone;
		this.category = category;
		this.description = description;
	}
	
	public int getUserCk(){
		return userCk;
	}
	public void setUserCk(int userCk){
		this.userCk = userCk;
	}
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPhone(){
		return phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	public String getCategory(){
		return category;
	}
	public void setCategory(String category){
		this.category = category;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
}
