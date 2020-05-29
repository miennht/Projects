package com.fms.jersey;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fms.CRUD.TokenCRUD;
import com.fms.CRUD.UserCRUD;
import com.fms.dto.Token;
import com.fms.dto.User;
import com.fms.utility.Utility;
import com.google.gson.Gson;

@Path("/login")
public class Login {
	public static final int NO_MINS_TOKEN_EXPIRED = 120;
	@GET
	@Path("/dologin")
	@Produces(MediaType.APPLICATION_JSON)	
	public String doLogin(@QueryParam("username") String uname, @QueryParam("password") String pwd){
		System.out.println("Inside doLogin: ");
		String respond = "";
		if (checkCredential(uname, pwd)){
			respond = Utility.constructJSON("Login", true);
		}else {
			respond = Utility.constructJSON("Login", false, "Incorrect username or password");
		} 
		return respond;
	}
	
	@GET
	@Path("/getuser")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/login/getuser?username=A45@mail.com
	public String getUserInfo(@QueryParam("username") String uname){
		String user = null;
		try{
			User userTemp = getUser(uname);
			Gson gson = new Gson();
			user = gson.toJson(userTemp);
			System.out.println("Inside Login.java, getUserInfo, user: " + user);
		} catch (Exception ex){
			System.out.println("System Error " + ex);
		}
		return user;
	}
	@GET
	@Path("/updateuser")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/login/updateuser?username=A80&password=A80&phone=&catagory=C&desc=DESCRIPTION&modifyuser=DBA
	public String modifyUser(@QueryParam("username") 	String uname,
				   @QueryParam("password") 	String password,
				   @QueryParam("phone") 	String phone,
				   @QueryParam("category") 	String category, 
				   @QueryParam("desc") 		String desc,
				   @QueryParam("modifyuser")String modifyUser){
		String updatedUserCk = null;
		try{
			ArrayList<Integer> userCkList = updateUser(uname, password, phone, category, desc, modifyUser);
			Gson gson = new Gson();
			updatedUserCk = gson.toJson(userCkList);
		} catch (Exception ex){
			System.out.println("System Error " + ex);
		}
		return updatedUserCk;
	}
	
	@GET
	@Path("/savetoken")
	@Produces(MediaType.APPLICATION_JSON)	
	//http://localhost:8080/com.fms.FMSRestfulWS/login/savehash?username=roseng0102@yahoo.com
	public String savehash(@QueryParam("userid") String userid){
		int hashCk = -1;
		String respond = "";
		//String token = Utility.TokenGenerator(userid);
		String token = Utility.TokenGenerator_6digits();
		String hash = Utility.hashToken(token); //Token hashing is unnecessary but I still implement it to be consistent with password hashing
		try {
			hashCk = insertToken(userid, hash, NO_MINS_TOKEN_EXPIRED);
			if (hashCk > 0)
				respond = Utility.constructJSON("savehash", true, token);//Mien Note 05/19/2020: Save the hash to the DB but keep the token for later use in GUI
			else
				respond = Utility.constructJSON("savehash", false, "Error with saving token, please contact Administrator.");
		} catch (SQLException e) {
			e.printStackTrace();
			respond = Utility.constructJSON("savehash", false, "Error with saving token, please contact Administrator.");
		} catch (Exception e) {
			e.printStackTrace();
			respond = Utility.constructJSON("savehash", false, "Error with saving token, please contact Administrator.");
		}
		return respond;
	}
	
	@GET
	@Path("/gettoken")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/login/gethash?userid=roseng0102@yahoo.com
	public String getTokenInfo(@QueryParam("userid") String userid){
		String token = null;
		try{
			Token tokenTmp = readToken(userid); //This returns the hash string of the token
			System.out.println("Inside Login.java, getTokenInfo, tokenTmp: " + tokenTmp);
			Gson gson = new Gson();
			token = gson.toJson(tokenTmp);
			System.out.println("Inside Login.java, getTokenInfo, token: " + token);
		} catch (Exception ex){
			System.out.println("System Error " + ex);
		}
		return token;
	}
	@GET
	@Path("/deletetoken")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/login/deletehash?userid=roseng0102@yahoo.com
	public String deletehash(@QueryParam("userid") String userid){
		String respond = null;
		try{
			int deletedRows = deleteToken(userid);
			System.out.println("Inside Login.java, getTokenInfo, deletehash: " + deletedRows);
			if (deletedRows >=0)
				respond = Utility.constructJSON("deletehash", true, deletedRows);
		} catch (Exception ex){
			respond = Utility.constructJSON("deletehash", false, "System Error when deleting existing token for the user");
		}
		return respond;
	}
	
	@GET
	@Path("/istokenvalid")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/login/istokenvalid?userid=roseng0102@yahoo.com&token=roseng0102@yahoo.com:b8d9e097-c206-409c-975a-0f2e25e20f65
	public String checktoken(@QueryParam("userid") String userid,
							@QueryParam("token") String token){
		String respond = null;
		Token storedToken = null;
		if(Utility.isNotNull(userid) && Utility.isNotNull(token)){
			try {
				storedToken = readToken(userid);//This returns the hash string of the token
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (storedToken!=null){	
				System.out.println("Inside checkToken, storedToken: " + storedToken);
				String storedHash = storedToken.getTokenHash();
				System.out.println("Inside checkToken, storedHash: "+ storedHash);
				Boolean storedTokenExpired = storedToken.getTokenIsExpired();	
				System.out.println("Inside checkToken, storedTokenExpired: "+ storedTokenExpired);
				String generatedHash = Utility.hashToken(token);
				System.out.println("Inside checkToken, generatedHash: "+ generatedHash);
				if(!storedTokenExpired && storedHash.equals(generatedHash)){
					System.out.println("Inside checkToken, token is valid");
					respond = Utility.constructJSON("checktoken", true);
				}else
					respond = Utility.constructJSON("checktoken", false);
			}else 
				respond = Utility.constructJSON("checktoken", false, "No token found for the given user.");
		}
		return respond;
	}
	
	public static boolean checkCredential(String uname, String pwd){
		boolean status = false;
		if(Utility.isNotNull(uname)&&Utility.isNotNull(pwd)){
			//Hash the input password before comparing to the one in DB
			String hash = Utility.hashToken(pwd);
			System.out.println("Inside Login, checkCredential, generatedHash: " + hash);
			try{
				status = UserCRUD.checkLogin(uname, hash);
				System.out.println("Inside Login, checkCredential, status: " + status);
			}catch(Exception e){
				status = false;
			}
		}else {
			status = false;
		}

		return status;
	}
	
	private User getUser(String uname){
		User user = new User();
		if(Utility.isNotNull(uname)){
			try{
				user = UserCRUD.readUser(uname);
			}catch(Exception e){
				user = null;
			}
		}else {
			user = null;
		}
		System.out.println("Inside Login.java, getUser, returning user: " + user);
		return user;
	}
	private ArrayList<Integer> updateUser(String userId, 
			String password, 
			String userPhone, 
			String userCategory, 
			String userDesc,
			String modifyUser) throws SQLException, Exception{
		ArrayList<Integer> updatedRecs = new ArrayList<Integer>();
		//Hash the password before updating
		String hash = Utility.hashToken(password);
		System.out.println("Inside Login.java, updateUser, hash: " + hash);
		try {
			updatedRecs = UserCRUD.updateUser(userId, hash, userPhone, userCategory, userDesc, modifyUser);
		}catch (SQLException sqle) {
			updatedRecs = null;
			throw sqle;
		}catch (Exception e){
			updatedRecs = null;
			throw e;
		}
		
		return updatedRecs;
	}
	private int insertToken(String username, String hash, int noOfMinsTokenExpired) throws SQLException, Exception{

		int insertedRec = -10;
		try {
			//Before inserting a new token, delete all tokens for the given user
			int deletedRed = TokenCRUD.deleteToken(username);
			insertedRec = TokenCRUD.insertToken(username, hash, noOfMinsTokenExpired);
			System.out.println("Inside Login, insetHash, insertedRec: " + insertedRec);
		}catch (SQLException sqle) {
			throw sqle;
		}catch (Exception e){
			throw e;
		}
		
		return insertedRec;
	}
	private Token readToken(String userId) throws SQLException, Exception{
		Token token = new Token();
		if(Utility.isNotNull(userId)){
			try{
				token = TokenCRUD.readToken(userId);
			}catch(Exception e){
				token = null;
			}
		}else {
			token = null;
		}
		System.out.println("Inside Login.java, readToken, returning token: " + token);
		return token;
	}
	
	private int deleteToken(String userId) throws SQLException, Exception{
		int deletedRows = 0;
		if(Utility.isNotNull(userId)){
			try{
				deletedRows = TokenCRUD.deleteToken(userId);
			}catch(Exception e){
				throw e;
			}
		}
		System.out.println("Inside Login.java, deleteToken, returning deletedRows: " + deletedRows);
		return deletedRows;
	}
}

