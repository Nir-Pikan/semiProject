package io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbController {

	private static DbController instance = null;
	
	private static final String dbHost = "localhost";
	private static final String dbSchemeName = "parkdb";
	private static final String dbUser = "root";
	private static final String dbPass = "Aa123456";
	
	private Connection conn;
	private PreparedStatement create;
	
	private DbController() throws SQLException {
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	 System.out.println("Driver definition failed");
        	 }
        
        try 
        {
            conn = DriverManager.getConnection("jdbc:mysql://"+dbHost+"/"+dbSchemeName+"?serverTimezone=IST",dbUser,dbPass);
            System.out.println("SQL connection succeed");
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            printSQLException(ex);
            throw ex;
            }
	}

	/**Print the value of SQL exception*/
	public static void printSQLException(SQLException ex) {
		System.out.println("SQLException: " + ex.getMessage());
		System.out.println("SQLState: " + ex.getSQLState());
		System.out.println("VendorError: " + ex.getErrorCode());
	}
	
	/**creates table in the dataBase only if not exists
	 * it will take the crateStatement received and add prefix of \"CREATE TABLE IF NOT EXISTS \"
	 * @param creatStatment the table to create
	 * @return if query went through without errors
	 * */
	public boolean createTable(String creatStatment) {
		
		try {
			conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS "+creatStatment);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**send update query to the db
	 * @param Statment query to send
	 * @return if query went through without errors
	 * */
	public boolean sendUpdate(String statment) {
		
		try {
			Statement s =conn.createStatement();
			s.executeUpdate(statment);
		} catch (SQLException e) {
			printSQLException(e);
			return false;
		}
		return true;
	}
	
	/**send update query to the db
	 * @param statment query to send
	 * @return the ResultSet if succeeded or null if failed
	 * */
	public ResultSet sendQuery(String statment) {
		
		try {
			Statement s =conn.createStatement();
			return s.executeQuery(statment);
		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}
	}
	
	/**create PreparedStatment to the DB
	 * @param query query to send
	 * @return the PreparedStatement if succeeded or null if failed
	 * */
	public PreparedStatement getPreparedStatement(String query) {
		try {
			return conn.prepareStatement(query);
		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}
	}

	/**connects to the mySQL server, must run before \"getInstance()\"*/
	public static void init() throws SQLException {
		if (instance == null) {
			instance = new DbController();
		}
		
	}
	
	/**get the instance of DbController, not like other singleton,\nDbController need to be initialized with DbController.Init() before usage(only one time)*/
	public static DbController getInstance() {
		if (instance == null) {
			throw new DbNotInitialized();
		}
		return instance;
	}
	
	/**Exception if \"DbController.getInstance()\" was called without calling \"DbController.init()\" first*/
	private static class DbNotInitialized extends RuntimeException{

		public DbNotInitialized() {
			super("Db not initialized, but referenced");
		}
		
	}

}
