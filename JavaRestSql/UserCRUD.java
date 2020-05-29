package com.fms.CRUD;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;




import java.util.ArrayList;

import com.fms.DBConnection.DBConnection;
import com.fms.dto.Product;
import com.fms.dto.User;

public class UserCRUD {
	
	public UserCRUD(){
		
	}
	
	public static boolean checkLogin(String userName, String password) throws Exception {
		boolean isUserAvailable = false;
		Connection dbConn = null;
		System.out.println("Inside UserCRUD, checkLogin, password: " + password);
		try{
			try{
				dbConn = DBConnection.getConnection();
			}catch (Exception e){
				e.printStackTrace();
			}
			Statement stm = dbConn.createStatement();
			String query = "SELECT * FROM CER_USUS_USER WHERE USUS_ID = '" + userName
                    + "' AND USUS_PASSWORD = " + "'" + password + "'";
			System.out.println("Inside UserCRUD, checkLogin, query: " + query);
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()){
				isUserAvailable = true;
			}
		}catch (SQLException sqle) {
			throw sqle;
		}catch (Exception e){
			if (dbConn!=null){
				dbConn.close();
			}
			throw e;
		}finally{
			if (dbConn!=null){
				dbConn.close();
			}
		}
		return isUserAvailable;
	}
	
	public static int insertUser(String userName, String password, String phone, String category, String userDescription) throws SQLException, Exception{
		int insertedRec = 0;
		//boolean insertStatus=false;
		Connection dbConn = null;
		try{
			try{
				dbConn = DBConnection.getConnection();
			}catch (Exception e){
				e.printStackTrace();
			}
			/*Statement stm = dbConn.createStatement();
			String sysUser = "SYSTEM_USER";
			String date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
			.format(Calendar.getInstance().getTime());
			String sysUserQuery = "Select SYSTEM_USER";
			ResultSet sysUserRS = stm.executeQuery(sysUserQuery);
			if (sysUserRS.next()){
				sysUser = sysUserRS.getString(1);
			}
			String query = "INSERT INTO CER_USUS_USER(USUS_ID, USUS_PASSWORD, USUS_PHONE, USUS_CAT, USUS_DESC, USUS_LAST_UPD_DT, USUS_LAST_UPD_USER) "
						+ "values ('"+ userName +"',"
						+ "'" + password + "',"
						+ "'" + phone + "',"
						+ "'" + category + "',"
						+ "'" + userDescription + "',"
						+ "'" + date + "',"
						+ "'" + sysUser + "')";
			System.out.println(query);
			int records = stm.executeUpdate(query);
			if (records>0){
				insertStatus = true;
			}*/
			String sql = "{call FMS_PROC_INSERT_USER(?,?,?,?,?,?)}";
			CallableStatement statement = dbConn.prepareCall(sql);
			statement.setString(1, userName);
			statement.setString(2, password);
			statement.setString(3, phone);
			statement.setString(4, category);
			statement.setString(5, userDescription);
			statement.registerOutParameter(6, java.sql.Types.INTEGER);
			statement.execute();
			insertedRec = statement.getInt(6);
		}catch (Exception e) {
			if (dbConn!=null){
				dbConn.close();
			}
			throw e;
		}finally {
			if (dbConn!=null){
				dbConn.close();
			}
		}
		return insertedRec;
	}
	
	public static User readUser(String userID) throws SQLException, Exception{
		User user = null;
		System.out.println("Inside UserCRUD.readUser, userID: " + userID);
		Connection dbConn = null;
		try{
			try{
				dbConn = DBConnection.getConnection();
			}catch (Exception e){
				e.printStackTrace();
			}
			String sql = "{call FMS_PROC_USUS_READ_USER(?)}";
			CallableStatement statement = dbConn.prepareCall(sql);	 
			statement.setString("userID", userID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()==true)				
			{	user = new User();
				System.out.println("Inside UserCRUD.readUser, rs.next()==true ");
				user.setUserCk(rs.getInt("USUS_CK"));
				user.setUserId(rs.getString("USUS_ID"));
				user.setPassword(rs.getString("USUS_PASSWORD"));
				user.setPhone(rs.getString("USUS_PHONE"));
				user.setCategory(rs.getString("USUS_CAT"));
				user.setDescription(rs.getString("USUS_DESC"));
			}
		}catch (Exception e) {
			if (dbConn!=null){
				dbConn.close();
			}
			throw e;
		}finally {
			if (dbConn!=null){
				dbConn.close();
			}
		}
		return user;
	}
	
	public static ArrayList<Integer> updateUser(String userId, 
												String password, 
												String userPhone,
												String userCategory,
												String userDesc, 
												String modifyUser)
		throws SQLException, Exception{
		ArrayList<Integer> updatedUserCkList = new ArrayList<Integer>();
		Connection dbConn = null;
		try{
			try{
			dbConn = DBConnection.getConnection();
			}catch (Exception e){
			e.printStackTrace();
		}
			String sql = "{call FMS_PROC_USUS_UPDATE(?,?,?,?,?,?)}";
			CallableStatement statement = dbConn.prepareCall(sql);	 
			statement.setString("userId",userId);
			statement.setString("password",password);
			statement.setString("userPhone",userPhone);
			statement.setString("userCategory",userCategory);
			statement.setString("userDesc",userDesc);
			statement.setString("modifyUser",modifyUser);
			ResultSet rs = statement.executeQuery();
			while (rs.next()==true)
			{	System.out.println("Ck: " + rs.getInt("ck"));
				updatedUserCkList.add(rs.getInt("ck"));
			} 
			
		}catch (SQLException sqlex){
			throw sqlex;
		}catch (Exception ex){
			if (dbConn!=null){
				dbConn.close();
			}
			throw ex;
		}finally {
			if (dbConn!=null){
			dbConn.close();
			}
		}
		return updatedUserCkList;
	}

}
