package io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**<pre>class for the connection to the DataBase
 * contains the userName, Password and SchemeName to the DB
 * Singelton</pre>
 * */
public class DbController implements IDbController {

	private static DbController instance = null;
	
	private static final String dbHost = "localhost";
	private static final String dbSchemeName = "parkdb";
	private static final String dbUser = "root";
	private static final String dbPass = "Aa123456"; 
	
	private Connection conn;
	
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
            conn = DriverManager.getConnection("jdbc:mysql://"+dbHost+"/"+dbSchemeName+"?serverTimezone=CAT",dbUser,dbPass);
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
		ex.printStackTrace();
	}
	
	/**creates table in the dataBase only if not exists
	 * it will take the crateStatement received and add prefix of \"CREATE TABLE IF NOT EXISTS \"
	 * @param creatStatment the table to create
	 * @return if query went through without errors
	 * */
	@Override
	public boolean createTable(String creatStatment) {
		
		try {
			conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS "+creatStatment);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**send update to the db
	 * @param statment query to send
	 * @return if query went through without errors
	 * */
	@Override
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
	
	/**send query to the db
	 * @param statment query to send
	 * @return the ResultSet if succeeded or null if failed
	 * */
	@Override
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
	@Override
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
