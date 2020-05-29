package com.fms.CRUD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fms.DBConnection.DBConnection;
import com.fms.dto.Token;
import com.fms.dto.User;

public class TokenCRUD {
	public static int insertToken(String username, String hash, int noOfMinsTokenExpired) throws Exception {
		int returnRec = 0;
		Connection dbConn = null;
		try{
			try{
				dbConn = DBConnection.getConnection();
			}catch (Exception e){
				e.printStackTrace();
			}
			String sql = "{call FMS_PROC_TOKN_INSERT(?,?,?,?)}";
			CallableStatement statement = dbConn.prepareCall(sql);	 
			statement.setString("userId", username);
			statement.setString("tokenHash", hash);
			statement.setInt("minsToAdd", noOfMinsTokenExpired);
			statement.registerOutParameter("insertedRec", java.sql.Types.INTEGER);
			statement.execute();
			returnRec = statement.getInt("insertedRec");
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
		return returnRec;
	}

	public static Token readToken(String userId) throws SQLException, Exception{
		Token token = null;
		System.out.println("Inside TokenCRUD, readToken, token: " + token);
		Connection dbConn = null;
		try{
			try{
				dbConn = DBConnection.getConnection();
			}catch (Exception e){
				e.printStackTrace();
			}
			String sql = "{call FMS_PROC_TOKN_READ(?)}";
			CallableStatement statement = dbConn.prepareCall(sql);	 
			statement.setString("userId", userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()==true)	//As ALL the tokens for the given userId were deleted before a new token is inserted, 
				//only ONE token will be returned here			
			{	token = new Token();
				System.out.println("Inside TokenCRUD, readToken, rs.next()==true");
				token.setTokenCk(rs.getInt("TOKN_CK"));
				token.setUserId(rs.getString("USUS_ID"));
				token.setTokenHash(rs.getString("TOKN_HASH_TOKEN"));
				token.setTokenCreationDt(rs.getDate("TOKN_CREATION_DT"));
				token.setTokenExpirationDt(rs.getDate("TOKN_EXPIRATION_DT"));
				token.setTokenIsExpired(rs.getBoolean("TOKN_IS_EXPIRED"));
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
		System.out.println("Inside TokenCRUD, readToken, token: " + token);
		return token;
	}
	public static int deleteToken(String userId) throws SQLException, Exception{
		int deletedRows = 0;
		Connection dbConn = null;
		try{
			try{
				dbConn = DBConnection.getConnection();
			}catch (Exception e){
				e.printStackTrace();
			}
			String sql = "{call FMS_PROC_TOKN_DELETE(?,?)}";
			CallableStatement statement = dbConn.prepareCall(sql);	 
			statement.setString("userId", userId);
			statement.registerOutParameter("deletedRecs", java.sql.Types.INTEGER);
			statement.execute();
			deletedRows = statement.getInt("deletedRecs");
			System.out.println("Inside TokenCRUD, deleteToken, deletedRows: " + deletedRows);
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
		return deletedRows;
	}
}
