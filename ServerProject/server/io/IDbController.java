package io;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface IDbController {

	/**creates table in the dataBase only if not exists
	 * it will take the crateStatement received and add prefix of \"CREATE TABLE IF NOT EXISTS \"
	 * @param creatStatment the table to create
	 * @return if query went through without errors
	 * */
	boolean createTable(String creatStatment);

	/**send update to the db
	 * @param statment query to send
	 * @return if query went through without errors
	 * */
	boolean sendUpdate(String statment);

	/**send query to the db
	 * @param statment query to send
	 * @return the ResultSet if succeeded or null if failed
	 * */
	ResultSet sendQuery(String statment);

	/**create PreparedStatment to the DB
	 * @param query query to send
	 * @return the PreparedStatement if succeeded or null if failed
	 * */
	PreparedStatement getPreparedStatement(String query);

}