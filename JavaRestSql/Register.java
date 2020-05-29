package com.fms.jersey;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fms.CRUD.ArtistCRUD;
import com.fms.CRUD.CustomerCRUD;
import com.fms.CRUD.ImageCRUD;
import com.fms.CRUD.ProductCRUD;
import com.fms.CRUD.UserCRUD;
import com.fms.dto.Artist;
import com.fms.dto.Customer;
import com.fms.dto.FMSFile;
import com.fms.utility.Utility;
import com.google.gson.Gson;

@Path("/customer")
public class Register {
	@GET
	@Path("/doregister")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/customer/doregister?username=mien&password=mien&phone=714234887&category=C&description=User Description
	public String doRegister(@QueryParam("username") String uname, @QueryParam("password") String pwd, @QueryParam("phone") String phone, @QueryParam("category") String cat, @QueryParam("description") String desc){
		String respond = null;
		int returnCode = insertUser(uname, pwd, phone, cat, desc);
		if (returnCode > 0) {
			respond = Utility.constructJSON("register", true, returnCode);
		}else if (returnCode == -1) {
			respond = Utility.constructJSON("register", false, "You are already registered", returnCode);
		}else if (returnCode == -2) {
			respond = Utility.constructJSON("register", false, "You entered more than the allowed number of characters", returnCode);
		}else if (returnCode == -4) {
			respond = Utility.constructJSON("register", false, "Error occured, please contact Administration!", returnCode);
		}
		return respond;
	}
	
	@GET
	@Path("/doregister/customerdetails")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/customer/doregister/customerdetails?username=mien&firstname=mien&lastname=nguyen&title=Ms&sex=F&photo=25&birthdate=12/31/2016&email=mien@gmail.com&mobile=7142348887&address1=3300%20S%20Tamarac%20Dr&address2=M104&city=Denver&state=CO&zip=80231&county=Denver&country=USA&lat=39.6678689&lng=-104.7927772&modifyuser=DBA
	public String registerCustomer(@QueryParam("username") 	String uname,
								   @QueryParam("firstname") String firstName,
								   @QueryParam("lastname") 	String lastName,
								   @QueryParam("title") 	String title, 
								   @QueryParam("sex") 		String sex,
								   @QueryParam("photo")		int photo,
								   @QueryParam("birthdate") String birthDate,
								   @QueryParam("email") 	String email, 
								   @QueryParam("mobile") 	String mobile, 
								   @QueryParam("address1") 	String address1, 
								   @QueryParam("address2") 	String address2, 
								   @QueryParam("city") 		String city, 
								   @QueryParam("state") 	String state,
								   @QueryParam("zip")		String zip,
								   @QueryParam("county")	String county,
								   @QueryParam("country") 	String country,
								   @QueryParam("lat") 		BigDecimal lat,
								   @QueryParam("lng") 		BigDecimal lng,
								   @QueryParam("modifyuser")String modifyUser){
		System.out.println ("Inside registerCustomer jersey, birthDay:" + birthDate + "username: " + uname );
		//System.out.println ("Inside registerCustomer jersey, birthday" + birthday);
		String respond = null;
		int returnCode = insertCustomer(uname, firstName, lastName, title, sex, photo, birthDate, email, mobile, address1, address2, city, state, zip, county, country, lat, lng, modifyUser);
		if (returnCode > 0) {
			respond = Utility.constructJSON("register", true, returnCode);
		}else if (returnCode == -1) {
			respond = Utility.constructJSON("register", false, "You are already registered");
		}else if (returnCode == -2) {
			respond = Utility.constructJSON("register", false, "You entered more than the allowed number of characters");
		}else if (returnCode == -3) {
			respond = Utility.constructJSON("register", false, "UserID doesn't exist, please contact Administrator!");
		}else if (returnCode == -5){
			respond = Utility.constructJSON("register", false, "UserID doesn't exist, please contact Administrator!");
		}
		else if (returnCode == -4) {
			respond = Utility.constructJSON("register", false, "Error occured, please contact Administration!");
		}
		return respond;
	}
	
	@GET
	@Path("/doregister/getcustomer")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/customer/doregister/getcustomer?username=therapist8@mail.com
	public String getCustomer(@QueryParam("username") 	String uname){
		String customer = null;
		try{
			Customer tmpCust = readCustInfo(uname);
			Gson gson = new Gson();
			customer = gson.toJson(tmpCust);
		} catch (Exception ex){
			System.out.println("System Error " + ex);
		}
		System.out.println("/doregister/getcustomer, customer: " + customer);
		return customer;
	}
	
	@GET
	@Path("/doregister/updatecustomer")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/customer/doregister/updatecustomer?username=mien&firstname=mien&lastname=nguyen&title=Ms&sex=F&photo=25&birthdate=12/31/2016&email=mien@gmail.com&mobile=7142348887&address1=3300%20S%20Tamarac%20Dr&address2=M104&city=Denver&state=CO&zip=80231&county=Denver&country=USA&lat=39.6678689&lng=-104.7927772&modifyuser=DBA
	public String modifyCustomer(@QueryParam("username") 	String uname,
				   @QueryParam("lastname") 	String lastName,
				   @QueryParam("firstname") String firstName,
				   @QueryParam("title") 	String title, 
				   @QueryParam("sex") 		String sex,
				   @QueryParam("photo")		int photo,
				   @QueryParam("birthdate") String birthDate,
				   @QueryParam("email") 	String email, 
				   @QueryParam("mobile") 	String mobile, 
				   @QueryParam("address1") 	String address1, 
				   @QueryParam("address2") 	String address2, 
				   @QueryParam("city") 		String city, 
				   @QueryParam("state") 	String state,
				   @QueryParam("zip")		String zip,
				   @QueryParam("county")	String county,
				   @QueryParam("country") 	String country,
				   @QueryParam("lat") 		BigDecimal lat,
				   @QueryParam("lng") 		BigDecimal lng,
				   @QueryParam("modifyuser")String modifyUser){
		String updatedCustomerCk = null;
		try{
			ArrayList<Integer> customerCkList = updateCustomer(uname, lastName, firstName, title, sex, photo, birthDate, email, mobile, address1, address2, city, state, zip, county, country, lat, lng, modifyUser);
			Gson gson = new Gson();
			updatedCustomerCk = gson.toJson(customerCkList);
		} catch (Exception ex){
			System.out.println("System Error " + ex);
		}
		return updatedCustomerCk;
	}

	
	@GET
	@Path("/doregister/customerdetails/modifycustomeraddress")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/customer/doregister/customerdetails/modifycustomeraddress?username=therapist8@mail.com&address1=3300%20S%20Tamarac%20Dr&address2=M104&city=Denver&state=CO&zip=80231&county=Denver&country=USA&lattitude=39.5420036&longitude=-104.862701&modifyuser=DBA
	public String modifyCustomerAddress(@QueryParam("username") 	String uname,
									   @QueryParam("address1") 		String address1, 
									   @QueryParam("address2") 		String address2, 
									   @QueryParam("city") 			String city, 
									   @QueryParam("state") 		String state,
									   @QueryParam("zip")			String zip,
									   @QueryParam("county")		String county,
									   @QueryParam("country") 		String country,
									   @QueryParam("lattitude") 	BigDecimal lat,
									   @QueryParam("longitude") 	BigDecimal lng,
									   @QueryParam("modifyuser")	String modifyUser){
		String updatedAddressCks = null;
		try{
			ArrayList<Integer> customerCkList = updateCustomerAddress(uname, address1, address2, city, state, zip, county, country, lat, lng, modifyUser);
			Gson gson = new Gson();
			updatedAddressCks = gson.toJson(customerCkList);
		} catch (Exception ex){
			System.out.println("System Error " + ex);
		}
		return updatedAddressCks;
	}
	
	@GET
	@Path("/doregister/registercard")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/customer/doregister/registercard?username=mien&cardtype=1&cardno=0897708&expdate=12/31/2020&cvv=987&country=USA&zip=80013&modifyuser=DBA
	public String registerCard	  (@QueryParam("username") 	String uname,
								   @QueryParam("cardtype")  String cardType,
								   @QueryParam("cardno") 	String cardNo,
								   @QueryParam("expdate") 	String expDt, 
								   @QueryParam("cvv") 		String cvv,
								   @QueryParam("country")   String country, 
								   @QueryParam("zip") 		String zip, 
								   @QueryParam("modifyuser")	String modifyUser){
		String respond = null;
		int returnCode = addCard(uname, cardType, cardNo, expDt, cvv, country, zip, modifyUser);
		if (returnCode > 0) {
			respond = Utility.constructJSON("register", true, returnCode);
		}else if (returnCode == -1) {
			respond = Utility.constructJSON("register", false, "You are already registered");
		}else if (returnCode == -2) {
			respond = Utility.constructJSON("register",false, "You entered more than the allowed number of characters");
		}else if (returnCode == -3) {
			respond = Utility.constructJSON("register", false, "UserID doesn't exist, please contact Administrator!");
		}else if (returnCode == -5){
			respond = Utility.constructJSON("register", false, "UserID doesn't exist, please contact Administrator!");
		}
		else if (returnCode == -4) {
			respond = Utility.constructJSON("register", false, "Error occured, please contact Administration!");
		}
		return respond;
	}
	
	@GET
	@Path("/doregister/registerartist")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/com.fms.FMSRestfulWS/customer/doregister/registerartist?username=mien&artistcat=NAIL&artisttype=FREN&artistindividualind=I&artistdesc=FrenchStyle&artistpromid=987&atrxsourceid=12/15/2016&modifyuser=DBA
	public String registerArtist  (@QueryParam("username") 		String uname,
								   @QueryParam("artistcat") 	String artistCat,
								   @QueryParam("artisttype")  	String artistType,
								   @QueryParam("artistindividualind") 	String artistIndividualInd,
								   @QueryParam("artistdesc") 	String artistDesc, 
								   @QueryParam("artistpromid") 	String artistPromId, 
								   @QueryParam("atrxsourceid") 	String atrxSourceId,
								   @QueryParam("modifyuser")	String modifyUser){
		String respond = null;
		int returnCode = addArtist(uname, artistCat, artistType, artistIndividualInd, artistDesc, artistPromId, atrxSourceId, modifyUser);
		if (returnCode > 0) {
			respond = Utility.constructJSON("register", true, returnCode);
		}else if (returnCode == -1) {
			respond = Utility.constructJSON("register", false, "You are already registered");
		}else if (returnCode == -2) {
			respond = Utility.constructJSON("register",false, "You entered more than the allowed number of characters");
		}else if (returnCode == -3) {
			respond = Utility.constructJSON("register", false, "UserID doesn't exist, please contact Administrator!");
		}else if (returnCode == -4){
			respond = Utility.constructJSON("register", false, "UserID doesn't exist, please contact Administrator!");
		}
		else if (returnCode == -5) {
			respond = Utility.constructJSON("register", false, "Error occured, please contact Administration!");
		}
		return respond;
	}
	
	public int insertUser(String uname, String pwd, String phone, String cat, String desc){
		int result = -4;
		if (Utility.isNotNull(uname)&&Utility.isNotNull(pwd)) {
			//Hash the password before storing it to the DB
			String hash = Utility.hashToken(pwd);
			System.out.println("Inside Register, insertUser, generatedHash: " + hash);
			try{
				int insertedRec = UserCRUD.insertUser(uname, hash, phone, cat, desc);
				//int insertedRec = UserCRUD.insertUser(uname, pwd, phone, cat, desc);
				if(insertedRec > 0){
					result = insertedRec;
				}
			}catch (SQLException sqle){
				//Primary key violation
				if (sqle.getErrorCode() == 2627) {
					result =  -1;
				} 
				//Data length violation
				else if (sqle.getErrorCode() == 8152) {
					result = -2;
				}

			}catch (Exception e){
				result = -4;
			}
		}
		
		return result;
	}
	
	private boolean isCustomerDuplicate(String userId){
		boolean status = false;
		System.out.println("Inside isProductDuplication: ");
		if(Utility.isNotNull(userId)){
			try{
				System.out.println("userID not null, inside try: ");
				status = CustomerCRUD.isCustomerDuplicate(userId);
			}catch(Exception e){
				status = false;
			}
		}else {
			status = false;
		}

		return status;
	}
	private int insertCustomer(String userID, 
								String firstName, 
								String lastName, 
								String title, 
								String sex,
								int photo,
								String birthDate, 
								String email, 
								String mobile, 
								String address1, 
								String address2, 
								String city, 
								String state,
								String zip, 
								String county, 
								String country, 
								BigDecimal lat,
								BigDecimal lng,
								String modifyUser){
		int result = -4;
		if (Utility.isNotNull(userID)){
			if(!isCustomerDuplicate(userID)){
				System.out.println("insertCustomer, isCustomerDuplicate = " + isCustomerDuplicate(userID));
				try {
					int insertedRec = CustomerCRUD.insertCustomer(userID, lastName, firstName, title, sex, photo, birthDate, email, mobile, address1, address2, city, state, zip, county, country, modifyUser);
					System.out.println("insertCustomer, insertedRec: " + insertedRec);
					if (insertedRec >= 0 ){
						result = insertedRec;
					}
				}catch (SQLException sqle) {
					//Primary key violation
					if (sqle.getErrorCode() == 2627) {
						result = -1;
					} 
					//Data length violation
					else if (sqle.getErrorCode() == 8152) {
						result = -2;
					} 
					//UserID doesn't exist
					else if (sqle.getErrorCode() == 217){
						result = -3;
					}//Foreign key constraint violation
					else if (sqle.getErrorCode() == 547){
						result = -5;
					}
				}catch (Exception e){
					result = -4;
				}
			} else {
				try {
					ArrayList<Integer> updatedRecs = CustomerCRUD.updateCustomer(userID, lastName, firstName, title, sex, photo, birthDate, email, mobile, address1, address2, city, state, zip, county, country, lat, lng, modifyUser);
					if (updatedRecs.size() >= 0 ){
						result = updatedRecs.size();
					}
				}catch (SQLException sqle) {
					//Primary key violation
					if (sqle.getErrorCode() == 2627) {
						result = -1;
					} 
					//Data length violation
					else if (sqle.getErrorCode() == 8152) {
						result = -2;
					} 
					//UserID doesn't exist
					else if (sqle.getErrorCode() == 217){
						result = -3;
					}//Foreign key constraint violation
					else if (sqle.getErrorCode() == 547){
						result = -5;
					}
				}catch (Exception e){
					result = -4;
				}
			}
		}
		System.out.println("insertCustomer, result: " + result);
		return result;
	}
	
	private Customer readCustInfo(String uname){
		Customer cust = new Customer();
		cust = null;
		if (uname!=null){
			try {
				cust = CustomerCRUD.readCustomer(uname);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		System.out.println("readCustInfo, cust: " + cust);
		return cust;
	}
	public ArrayList<Integer> updateCustomer(String userID, 
												String lastName, 
												String firstName, 
												String title, 
												String sex,
												int photo,
												String birthDate, 
												String email, 
												String mobile, 
												String address1, 
												String address2, 
												String city, 
												String state,
												String zip, 
												String county, 
												String country, 
												BigDecimal lat,
												BigDecimal lng,
												String modifyUser) throws SQLException, Exception{
		
		ArrayList<Integer> updatedRecs = new ArrayList<Integer>();
		try {
			updatedRecs = CustomerCRUD.updateCustomer(userID, lastName, firstName, title, sex, photo, birthDate, email, mobile, address1, address2, city, state, zip, county, country, lat, lng, modifyUser);
			
		}catch (SQLException sqle) {
			updatedRecs = null;
			throw sqle;
		}catch (Exception e){
			updatedRecs = null;
			throw e;
		}
			
		return updatedRecs;
	}
	
	private ArrayList<Integer> updateCustomerAddress(String userID, 
									String address1, 
									String address2, 
									String city, 
									String state,
									String zip, 
									String county, 
									String country, 
									BigDecimal lat,
									BigDecimal lng,
									String modifyUser) throws SQLException, Exception{
		
		ArrayList<Integer> updatedRecs = new ArrayList<Integer>();
		try {
			updatedRecs = CustomerCRUD.updateCustAdd(userID, address1, address2, city, state, zip, county, country, lat, lng, modifyUser);
			System.out.println("Inside updateCustomerAddress, try, updatedRec = " + updatedRecs);
			
		}catch (SQLException sqle) {
			updatedRecs = null;
			throw sqle;
		}catch (Exception e){
			updatedRecs = null;
			throw e;
		}
			
		return updatedRecs;
	}

	private int addCard(String userID, String cardType, String cardNo, 
            			String expDate, String CVV, String country, 
            			String zip, String modifyUser){
		int result = -4;
		System.out.println("Inside addCard, result = " + result);
		if (Utility.isNotNull(userID)){
			try {
				int insertedRec = CustomerCRUD.addCard(userID, cardType, cardNo, expDate, CVV, country, zip, modifyUser);
				System.out.println("Inside addCard, try, insertedRec = " + insertedRec);
				if (insertedRec>0 ){
					result = insertedRec;
				}
			}catch (SQLException sqle) {
				//Primary key violation
				if (sqle.getErrorCode() == 2627) {
					result = -1;
				} 
				//Data length violation
				else if (sqle.getErrorCode() == 8152) {
					result = -2;
				} 
				//UserID doesn't exist
				else if (sqle.getErrorCode() == 217){
					result = -3;
				}
				//Foreign key constraint violation
				else if (sqle.getErrorCode() == 547){
					result = -5;
				}
			}catch (Exception e){
				result = -4;
			}
		}
		return result;
		
	}
	private int addArtist(String userID, 
						String artistCat,
						String artistType,
						String artistIndividualInd,
						String artistDesc,
						String artistPromId,
						String atrxSourceId,
						String modifyUser){
		int result = -5;
		if (Utility.isNotNull(userID)){
			try {
				int insertedRec = CustomerCRUD.addArtist(userID, artistCat, artistType, artistIndividualInd, artistDesc, artistPromId, atrxSourceId, modifyUser);
				System.out.println("Inside addArtist, try, insertedRec = " + insertedRec);
				if (insertedRec>0 ){
					result = insertedRec;
				}
			}catch (SQLException sqle) {
				//Primary key violation
				if (sqle.getErrorCode() == 2627) {
					result = -1;
				} 
				//Data length violation
				else if (sqle.getErrorCode() == 8152) {
					result = -2;
				} 
				//UserID doesn't exist
				else if (sqle.getErrorCode() == 217){
					result = -3;
				}
				//Foreign key constraint violation
				else if (sqle.getErrorCode() == 547){
					result = -4;
				}
			}catch (Exception e){
				System.out.println("exception inside Register.addArtist: " + e);
				result = -5;
			}
		}
		
		System.out.println("result inside Register.addArtist: " + result);
		return result;

		}
	}
