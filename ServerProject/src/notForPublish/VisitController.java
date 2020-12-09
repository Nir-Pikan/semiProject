package notForPublish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Visitor;
import io.DbController;
import modules.IController;
import modules.ServerRequest;
import ocsf.server.ConnectionToClient;

//TODO example not to publish
public class VisitController implements IController {

	DbController db;

	public VisitController() {
		db = DbController.getInstance();
		createTable();

	}

	private void createTable() {
		db.createTable(
				"visitor(firstname varchar(20),surname varchar(20),id varchar(12),email varchar(30),phone varchar(12),primary key(id));");

	}

	/**
	 * get visitor from database
	 * 
	 * @param id the id of the visitor
	 * @return Visitor wanted or null if not exists
	 */
	public Visitor getVisitor(String id) {
		ResultSet rs = db.sendQuery("select * from visitor where id=" + id + ";");
		if(rs==null)
			return null;
		Visitor v = null;
		try {
			if (rs.next()) {
				// Print out the values
				v = new Visitor(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return v;
	}

	/**
	 * set the Email of visitor
	 * 
	 * @param id       the id of the visitor
	 * @param newEmail the new email to set
	 * @return if changed successfully
	 */
	private boolean changeVisitorEmail(String id, String newEmail) {
		PreparedStatement ps = db.getPreparedStatement("UPDATE visitor SET email=? where id=?;");
		if (ps == null)
			return false;
		try {
			ps.setString(1, newEmail);
			ps.setString(2, id);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			DbController.printSQLException(e);
			return false;
		}

	}

	@Override
	public String handleRequest(ServerRequest request) {
		if (request == null)
			return "Unknown request";
		switch (request.job) {
		case "getVisitor":
			Visitor v = getVisitor(request.data);
			if(v!=null)
				return ServerRequest.gson.toJson(v);
			else
				return "not Found";
		case "changeVisitorEmail":
			String[] data = request.data.split(" ");
			String id = data[0];
			if (getVisitor(id)!= null &&changeVisitorEmail(id, data[1]))
				return "the email of " + id + " changed successfully";
			return "error";
		default:
			return "Unknown request";
		}

	}

}
