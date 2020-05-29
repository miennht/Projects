package com.fms.dto;

import java.math.BigDecimal;

public class Customer {
	private int customerCk,
				customerPhotoId;
	private String 	userId,
					customerLastName,
					customerFirstName,
					customerTitle,
					customerSex,
					customerBirthday,
					customerEmail,
					customerMobile,
					customerAdd1,
					customerAdd2,
					customerCity,
					customerState,
					customerZip,
					customerCounty,
					customerCountry;
	private BigDecimal  customerLat,
						customerLng;
	public Customer(){
		
	}
	
	public Customer(int customerCk, 
			String userId,
			String customerLastName,
			String customerFirstName,
			String customerTitle,
			String customerSex,
			int customerPhotoId,
			String customerBirthday,
			String customerEmail,
			String customerMobile,
			String customerAdd1,
			String customerAdd2,
			String customerCity,
			String customerState,
			String customerZip,
			String customerCounty,
			String customerCountry,
			BigDecimal customerLat,
			BigDecimal customerLng){
		this.customerCk = customerCk;
		this.userId = userId;
		this.customerLastName = customerLastName;
		this.customerFirstName = customerFirstName;
		this.customerTitle = customerTitle;
		this.customerSex = customerSex;
		this.customerPhotoId = customerPhotoId;
		this.customerBirthday = customerBirthday;
		this.customerEmail = customerEmail;
		this.customerMobile = customerMobile;
		this.customerAdd1 = customerAdd1;
		this.customerAdd2 = customerAdd2;
		this.customerCity = customerCity;
		this.customerState = customerState;
		this.customerZip = customerZip;
		this.customerCounty = customerCounty;
		this.customerCountry = customerCountry;
		this.customerLat = customerLat;
		this.customerLng = customerLng;
	}
	
	public int getCustomerCk(){
		return customerCk;
	}
	public void setCustomerCk(int customerCk){
		this.customerCk = customerCk;
	}
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getLastName(){
		return customerLastName;
	}
	public void setLastName(String customerLastName){
		this.customerLastName = customerLastName;
	}
	public String getFirstName(){
		return customerFirstName;
	}
	public void setFirstName(String customerFirstName){
		this.customerFirstName = customerFirstName;
	}
	public String getTitle(){
		return customerTitle;
	}
	public void setTitle(String customerTitle){
		this.customerTitle = customerTitle;
	}
	public String getSex(){
		return customerSex;
	}
	public void setSex(String customerSex){
		this.customerSex = customerSex;
	}
	public int getPhotoId(){
		return customerPhotoId;
	}
	public void setPhotoId(int customerPhotoId){
		this.customerPhotoId = customerPhotoId;
	}
	public String getBirthday(){
		return customerBirthday;
	}
	public void setBirthday(String customerBirthday){
		this.customerBirthday = customerBirthday;
	}
	public String getEmail(){
		return customerEmail;
	}
	public void setEmail(String customerEmail){
		this.customerEmail = customerEmail;
	}
	public String getMobile(){
		return customerMobile;
	}
	public void setMobile(String customerMobile){
		this.customerMobile = customerMobile;
	}
	public String getAdd1(){
		return customerAdd1;
	}
	public void setAdd1(String customerAdd1){
		this.customerAdd1 = customerAdd1;
	}
	public String getAdd2(){
		return customerAdd2;
	}
	public void setAdd2(String customerAdd2){
		this.customerAdd2 = customerAdd2;
	}
	public String getCity(){
		return customerCity;
	}
	public void setCity(String customerCity){
		this.customerCity = customerCity;
	}
	public String getState(){
		return customerState;
	}
	public void setState(String customerState){
		this.customerState = customerState;
	}
	public String getZip(){
		return customerZip;
	}
	public void setZip(String customerZip){
		this.customerZip = customerZip;
	}
	public String getCounty(){
		return customerCounty;
	}
	public void setCounty(String customerCounty){
		this.customerCounty = customerCounty;
	}
	public String getCountry(){
		return customerCountry;
	}
	public void setCountry(String customerCountry){
		this.customerCountry = customerCountry;
	}
	public BigDecimal getLat(){
		return customerLat;
	}
	public void setLat(BigDecimal customerLat){
		this.customerLat = customerLat;
	}
	public BigDecimal getLng(){
		return customerLng;
	}
	public void setLng(BigDecimal customerLng){
		this.customerLng = customerLng;
	}
}
