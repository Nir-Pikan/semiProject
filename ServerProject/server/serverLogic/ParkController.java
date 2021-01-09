package serverLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DbController;
import db.IDbController;
import entities.Park;
import entities.ParkNameAndTimes;
import entities.PendingValueChangeRequest;
import entities.PendingValueChangeRequest.ParkAttribute;
import modules.IController;
import modules.ServerRequest;

/** the park controller class */
public class ParkController implements IController {
	IDbController db;

	/** creates the {@link ParkController} */
	public ParkController() {
		db = DbController.getInstance();
		createTable();
	}

	/**
	 * Constructor for testing, Constructor dependency injection
	 */
	public ParkController(IDbController cont) {
		db = cont;
	}

	/**
	 * Creates the Park table if not exist <br>
	 * table of {@link Park}s
	 */
	private void createTable() {
		db.createTable("park(parkId varchar(20),parkName  varchar(20),maxCapacity int,managerId  varchar(20),"
				+ "maxPreOrder int,avgVisitTime double,currentNumOfVisitors int,openTime int,"
				+ "closeTime int,primary key(parkId));");

		db.createTable("valueChangeRequest(parkId  varchar(20),"
				+ "attributeName ENUM('MaxCapacity','MaxPreOrder','AvgVisitTime'),"
				+ "requestedValue double,currentValue double,primary key(parkId,attributeName) ,"
				+ "foreign key(parkID) references park(parkId));");

	}

	/**
	 * get MaxPreOrder from {@link Park} with the given id
	 * 
	 * @param parkId the Id of the wanted {@link Park}
	 * @return the value of the MaxPreOrder<br>
	 *         -1 if failed
	 */
	public int getMaxPreOrder(String parkId) {
		Park p = getPark(parkId);
		if (p != null)
			return p.maxPreOrders;
		else
			return -1;
	}

	/**
	 * get MaxCapacity from {@link Park} with the given id
	 * 
	 * @param parkId the Id of the wanted {@link Park}
	 * @return the value of the MaxCapacity<br>
	 *         -1 if failed
	 */
	public int getMaxCapacity(String parkId) {
		Park p = getPark(parkId);
		if (p != null)
			return p.maxCapacity;
		else
			return -1;
	}

	/**
	 * get AVGvisitTime from {@link Park} with the given id
	 * 
	 * @param parkId the Id of the wanted {@link Park}
	 * @return the value of the AVGvisitTime<br>
	 *         -1 if failed
	 */
	public double getAVGvisitTime(String parkId) {
		Park p = getPark(parkId);
		if (p != null)
			return p.avgVisitTime;
		else
			return -1;
	}

	/**
	 * Set Attribute value for given {@link Park}
	 * 
	 * @param parkId    the Id of the wanted {@link Park}
	 * @param attribute the ParkAttribute To change
	 * @param val       the new value to save
	 * @return true if the update worked successfully<br>
	 *         false otherwise
	 */
	private boolean setValue(String parkId, ParkAttribute attribute, Number val) {
		PreparedStatement setValue = db
				.getPreparedStatement("UPDATE park SET " + attribute.toString() + " = ? WHERE parkId = ?");
		try {
			switch (attribute) {
			case AvgVisitTime:
				setValue.setDouble(1, val.doubleValue());
				break;
			case MaxCapacity:
			case MaxPreOrder:
				setValue.setInt(1, val.intValue());
				break;
			}
			setValue.setString(2, parkId);
			return setValue.executeUpdate() == 1;
		} catch (SQLException e) {
			DbController.printSQLException(e);
		}
		return false;
	}

	/**
	 * Add Value ChangeRequest to the DB
	 * 
	 * @param request the request to add
	 * @return true if succeeded to add<br>
	 *         false otherwise
	 */
	private boolean addValueChangeRequest(PendingValueChangeRequest request) {
		return db
				.sendUpdate("INSERT INTO valueChangeRequest(parkId,attributeName,requestedValue,currentValue) VALUES(\""
						+ request.parkId + "\",\"" + request.attName.toString() + "\"," + request.requestedValue + ","
						+ request.currentValue + ");");

	}

	/**
	 * delete Value ChangeRequest from the DB
	 * 
	 * @param request the request to delete
	 * @return true if succeeded to add<br>
	 *         false otherwise
	 */
	private boolean removeValueChangeRequest(PendingValueChangeRequest request) {
		return db.sendUpdate("DELETE FROM valueChangeRequest WHERE parkId=\"" + request.parkId
				+ "\" AND attributeName=\"" + request.attName.toString() + "\";");

	}

	/**
	 * update the numberOfCurrentVisitor in {@link Park}
	 * 
	 * @param parkId the Id of the wanted {@link Park}
	 * @param delta  the number of visitor to increase or reduce
	 * @return the number of new visitors in the {@link Park}
	 * @throws IllegalArgumentException if the newValue = oldValue + delta is<br>
	 *                                  lower than 0 or greater than the capacity
	 */
	public int updateNumberOfCurrentVisitor(String parkId, int delta) {
		Park p = getPark(parkId);
		if (p == null)
			return -1;
		PreparedStatement setValue = db
				.getPreparedStatement("UPDATE park SET currentNumOfVisitors = ? WHERE parkId = ?");
		int newVal = p.currentNumOfVisitors + delta;
		if (newVal < 0 || newVal > p.maxCapacity)
			throw new IllegalArgumentException("Num of current Visitor change request, current val "
					+ p.currentNumOfVisitors + " visitor to add " + delta);
		try {
			setValue.setInt(1, newVal);
			setValue.setString(2, parkId);
			if (setValue.executeUpdate() != 1)
				return -1;
			return newVal;
		} catch (SQLException e) {
			DbController.printSQLException(e);
		}
		return -1;
	}

	/**
	 * checks if number of visitors can get into the {@link Park}
	 * 
	 * @param parkId     the Id of the wanted {@link Park}
	 * @param numToCheck number of visitors that want to enter
	 * @return true if adding this many visitors not exceeding the capacity<br>
	 *         false otherwise
	 */
	public boolean checkAvailabilityForNumberOfVisitors(String parkId, int numToCheck) {
		Park p = getPark(parkId);
		if (p == null)
			return false;
		return (p.currentNumOfVisitors + numToCheck) <= p.maxCapacity;
	}

	/**
	 * Get the {@link Park} of the given ParkID
	 * 
	 * @param parkId the Id of the wanted {@link Park}
	 * @return the {@link Park} of the given ID<br>
	 *         null if failed
	 */
	protected Park getPark(String parkId) {
		try {

			ResultSet rs = db.sendQuery("SELECT * FROM park WHERE parkId =\"" + parkId + "\";");
			if (rs.next()) {
				return new Park(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5),
						rs.getDouble(6), rs.getInt(7), rs.getInt(8), rs.getInt(9));
			}
		} catch (SQLException e) {
			DbController.printSQLException(e);
		}
		return null;
	}

	@Override
	public String handleRequest(ServerRequest request) {
		if (request == null)
			return "Unknown request";
		switch (request.job) {
		case "approve change":
			PendingValueChangeRequest vcr = ServerRequest.gson.fromJson(request.data, PendingValueChangeRequest.class);
			if (vcr.attName == ParkAttribute.MaxPreOrder) {
				if (getMaxCapacity(vcr.parkId) < vcr.requestedValue)
					return "value error";
			}
			if (!removeValueChangeRequest(vcr))
				return "failed to delete";
			if (setValue(vcr.parkId, vcr.attName, vcr.requestedValue))
				return "cahnged successfully";
			else
				return "failed to setValue";
		case "decline change":
			PendingValueChangeRequest vcr2 = ServerRequest.gson.fromJson(request.data, PendingValueChangeRequest.class);
			if (!removeValueChangeRequest(vcr2))
				return "failed to delete";
			return "deleted successfully";
		case "add Value Change Request":
			PendingValueChangeRequest vcr3 = ServerRequest.gson.fromJson(request.data, PendingValueChangeRequest.class);
			if (addValueChangeRequest(vcr3))
				return "add successfully";
			return "failed to add";
		case "get pending change requests":
			PendingValueChangeRequest[] result = getAllValueChangeRequests();
			if (result == null)
				return "failed";
			return ServerRequest.gson.toJson(result, PendingValueChangeRequest[].class);
		case "get current parameter":
			Park p = getPark(request.data);
			if (p == null)
				return "park not exists";
			String response[] = { "" + p.maxCapacity, "" + p.maxPreOrders, "" + p.avgVisitTime };
			return ServerRequest.gson.toJson(response, String[].class);
		case "get number of visitor available":
			Park p2 = getPark(request.data);
			if (p2 == null)
				return "park not exists";
			return (p2.maxCapacity - p2.currentNumOfVisitors) + "";
		case "get number of current visitors":
			Park p3 = getPark(request.data);
			if (p3 == null)
				return "park not exists";
			return p3.currentNumOfVisitors + "";
		case "get all parks data":
			return ServerRequest.gson.toJson(getParksData(), ParkNameAndTimes[].class);
		}

		return null;

	}

	/**
	 * Get all the park value change requests
	 * 
	 * @return a list of all the {@link PendingValueChangeRequest}s
	 */
	private PendingValueChangeRequest[] getAllValueChangeRequests() {
		List<PendingValueChangeRequest> l = new ArrayList<>();
		ResultSet rs = db.sendQuery("SELECT * From valueChangeRequest");
		try {
			while (rs.next()) {
				l.add(new PendingValueChangeRequest(rs.getString(1), ParkAttribute.valueOf(rs.getString(2)),
						rs.getDouble(3), rs.getDouble(4)));
			}
			return l.toArray(new PendingValueChangeRequest[0]);
		} catch (SQLException e) {
			DbController.printSQLException(e);
		}
		return null;
	}

	/**
	 * return Array with all of the {@link ParkNameAndTimes}s
	 * 
	 * @return a list of {@link ParkNameAndTimes}s of all the {@link Park}s
	 */
	private ParkNameAndTimes[] getParksData() {
		List<ParkNameAndTimes> l = new ArrayList<>();
		ParkNameAndTimes[] res = {};
		ResultSet rs = db.sendQuery("SELECT parkId, parkName,openTime,closeTime From park;");
		try {
			while (rs.next()) {
				l.add(new ParkNameAndTimes(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
			}
		} catch (SQLException e) {
			DbController.printSQLException(e);
		}

		return l.toArray(res);
	}

}
