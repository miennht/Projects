package com.fms.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	
	public static String dbClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static String url = "jdbc:sqlserver://"; 
	public static String serverName = "localhost";
	public static String databaseName = "FMS";
	public static String userName = "sa";
	public static String password = "serviceone";
	public static String selectedMethod = "cursor";
	
	//Constructor
	public DBConnection(){
		
	}
	
	public static Connection getConnection(){
		Connection conn = null;
		try{
			Class.forName(dbClass);
			conn = DriverManager.getConnection(url+serverName+";databasename="+databaseName+";selectedMethod="+selectedMethod+";", userName, password);
			if (conn!=null) System.out.println("Connection successful!");
		}catch (Exception e){
			e.printStackTrace();
			System.out.println ("Error Trace in getConnection(): " + e.getMessage());
		}
		return conn;
	}
}
