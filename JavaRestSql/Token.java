package com.fms.dto;

import java.util.Date;

public class Token {
	
	private int 	tokenCk;
	private String 	userId,
					tokenHash;
	private Date	tokenCreationDt,
					tokenExpirationDt;
	private boolean	tokenIsExpired;

	public Token(){
		
	}
	
	public Token(String userId, String tokenHash, 
				Date tokenCreationDt, Date tokenExpirationDt, boolean tokenIsExpired){
		this.userId = userId;
		this.tokenHash = tokenHash;
		this.tokenCreationDt = tokenCreationDt;
		this.tokenExpirationDt = tokenExpirationDt;
		this.tokenIsExpired = tokenIsExpired;
	}
	
	public int getTokenCk(){
		return tokenCk;
	}
	public void setTokenCk(int tokenCk){
		this.tokenCk = tokenCk;
	}
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getTokenHash(){
		return tokenHash;
	}
	public void setTokenHash(String tokenHash){
		this.tokenHash = tokenHash;
	}
	public Date getTokenCreationDt(){
		return tokenCreationDt;
	}
	public void setTokenCreationDt(Date tokenCreationDt){
		this.tokenCreationDt = tokenCreationDt;
	}
	public Date getTokenExpirationDt(){
		return tokenExpirationDt;
	}
	public void setTokenExpirationDt(Date tokenExpirationDt){
		this.tokenExpirationDt = tokenExpirationDt;
	}
	public boolean getTokenIsExpired(){
		return tokenIsExpired;
	}
	public void setTokenIsExpired(boolean tokenIsExpired){
		this.tokenIsExpired = tokenIsExpired;
	}
}
