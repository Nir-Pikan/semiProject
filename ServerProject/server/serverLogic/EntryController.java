
package serverLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import db.DbController;
import db.IDbController;
import entities.Park;
import entities.ParkEntry;
import entities.ParkEntry.EntryType;
import modules.IController;
import modules.ServerRequest;

/** the ParkEntry controller class */
public class EntryController implements IController {
	private ParkController park;
	private MessageController messageC;
	private SubscriberController subscriber;
	private DiscountController discount;
	private IDbController dbController;

	/**
	 * creates the {@link EntryController}
	 * 
	 * @param park       instance of {@link ParkController}
	 * @param messageC   instance of {@link MessageController}
	 * @param subscriber instance of {@link SubscriberController}
	 * @param discount   instance of {@link DiscountController}
	 */
	public EntryController(ParkController park, MessageController messageC, SubscriberController subscriber,
			DiscountController discount) {
		this.park = park;
		this.messageC = messageC;
		this.subscriber = subscriber;
		this.discount = discount;
		dbController = DbController.getInstance();
		createTable();

	}

	/**
	 * Creates the DB table
	 */
	private void createTable() {
		boolean isCreated = dbController.createTable("parkEntry "
				+ "(entryType ENUM('Personal','Subscriber','Group','PrivateGroup') , "
				+ "personID varchar(20) NOT NULL , " + "parkID varchar(20) NULL , "
				+ "arriveTime TIMESTAMP(1) NOT NULL , " + "exitTime TIMESTAMP(1) NULL , "
				+ "numberOfVisitors  int NULL , numberOfSubscribers  int NULL , " + "isCasual TINYINT NULL ,"
				+ "priceOfOrder FLOAT NULL , " + "priceOfEntry FLOAT NULL , " + "primary key(personID , arriveTime));");
		if (isCreated) {
			System.out.println("Table parkEntry created successful");
		}
	}

	@Override
	public String handleRequest(ServerRequest request) {
		String job = request.job;
		String response = null;
		switch (job) {

		case "getEntriesByDate":
			Timestamp[] times = ServerRequest.gson.fromJson(request.data, Timestamp[].class);
			if (times == null || times.length != 2) {
				response = "Error: There is no 2 times search between ";
			} else if (times[0].getTime() > times[1].getTime()) {
				response = "Error: start time is later than end time ";
			} else {
				ParkEntry[] parkEntries = getEntitiesByDate(times[0], times[1]);
				if (parkEntries == null) {
					response = "Error: could not get entires from DB ";
				} else {
					response = ServerRequest.gson.toJson(parkEntries, ParkEntry[].class);
				}
			}
			break;

		case "AddNewEntry":
			ParkEntry newEntry = ServerRequest.gson.fromJson(request.data, ParkEntry.class);
			if (newEntry == null) {
				response = "Failed to add new Entry got Null";
			} else if (AddNewEntry(newEntry)) {
				response = "Entry was added successfully";
			} else {
				response = "Failed to add Entry";
			}
			break;

		case "updateExit":
			String personID = request.data;
			if (personID == null) {
				response = "Failed to update Entry exit got Null";
			} else if (updateExit(personID)) {
				response = "Entry exit was updated successfully ";
			} else {
				response = "Failed to update Entry exit";
			}
			break;

		default:
			response = "Error: No such job";

		}

		return response;
	}

	/**
	 * adds new {@link ParkEntry} to the DB table
	 * 
	 * @param newEntry            - includes all newEntry object fields
	 *                            <b>without:</b> <b> { arriveTime, exitTime, price
	 *                            }</b>
	 * @param numberOfSubscribers - is the number of the total number of visitors
	 *                            that is subscribers
	 * @return true if succeeded<br>
	 *         false otherwise
	 */
	private boolean AddNewEntry(ParkEntry newEntry) {
		if (dbController == null)
			dbController = DbController.getInstance();
		createTable();

		if (newEntry.numberOfSubscribers > newEntry.numberOfVisitors)
			return false;

		Park p = park.getPark(newEntry.parkID);
		if (newEntry.numberOfVisitors > (p.maxCapacity - p.currentNumOfVisitors))
			return false;

		PreparedStatement pstmt = dbController.getPreparedStatement("INSERT INTO parkEntry"
				+ "( entryType , personID , parkID , arriveTime , exitTime , numberOfVisitors , numberOfSubscribers , isCasual , priceOfOrder , priceOfEntry ) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");
		try {

			pstmt.setString(1, newEntry.entryType.toString());
			pstmt.setString(2, newEntry.personID);
			pstmt.setString(3, newEntry.parkID);
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			pstmt.setTimestamp(4, currentTime);
			pstmt.setTimestamp(5, null);
			pstmt.setInt(6, newEntry.numberOfVisitors);
			pstmt.setInt(7, newEntry.numberOfSubscribers);
			pstmt.setBoolean(8, newEntry.isCasual);
			boolean isGroup = newEntry.entryType == EntryType.Group ? true : false;
			if (newEntry.isCasual) {
				pstmt.setFloat(9, discount.CalculatePriceForEntryCasual(newEntry.numberOfVisitors,
						newEntry.numberOfSubscribers, isGroup));
				pstmt.setFloat(10, discount.CalculatePriceForEntryCasual(newEntry.numberOfVisitors,
						newEntry.numberOfSubscribers, isGroup));
			} else {
				pstmt.setFloat(9, discount.CalculatePriceForEntryByOrder(newEntry.numberOfVisitors,
						newEntry.numberOfSubscribers, isGroup, newEntry.arriveTime));
				pstmt.setFloat(10, discount.CalculatePriceForEntryByOrder(newEntry.numberOfVisitors,
						newEntry.numberOfSubscribers, isGroup, newEntry.arriveTime));
			}
			park.updateNumberOfCurrentVisitor(newEntry.parkID, newEntry.numberOfVisitors);

			return pstmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to execute update");
		}
		return false;

	}

	/**
	 * update the time of the person exits
	 * 
	 * @param personID ID of the person who left the park
	 * @return true if succeed<br>
	 *         false otherwise
	 */
	private boolean updateExit(String personID) {

		ResultSet rs = dbController
				.sendQuery("select * from parkEntry where personID = \"" + personID + "\" AND exitTime is null  ;");

		if (rs == null)
			return false;

		PreparedStatement pstmt = dbController
				.getPreparedStatement("UPDATE parkEntry SET exitTime = ? WHERE  personID = ? AND exitTime is NULL ;");
		try {
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			pstmt.setTimestamp(1, currentTime);
			pstmt.setString(2, personID);
			if (rs.next()) {

				String parkID = rs.getString(3);
				int numberOfVisitors = rs.getInt(6);

				park.updateNumberOfCurrentVisitor(parkID, -numberOfVisitors);
				rs.close();
				return pstmt.executeUpdate() == 1;

			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to execute update");
		}
		return false;
	}

	/**
	 * For given start date and finish date returns all the {@link ParkEntry} to the
	 * park
	 * 
	 * @param fromTime date from which you want to get the entries
	 * @param toTime   date till which you want to get the entries
	 * @return array of ParkEntry in the given time line
	 */
	private ParkEntry[] getEntitiesByDate(Timestamp fromTime, Timestamp toTime) {

		ArrayList<ParkEntry> resultList = new ArrayList<>();
		try {
			PreparedStatement pstmt = dbController
					.getPreparedStatement("select * from parkEntry  where timestamp(arriveTime) > timestamp( ? )  "
							+ " and  timestamp(arriveTime) < timestamp( ? ) ;");

			pstmt.setTimestamp(1, fromTime);
			pstmt.setTimestamp(2, toTime);
			ResultSet rs = pstmt.executeQuery();
			if (rs == null)
				return null;
			while (rs.next()) {

				resultList.add(new ParkEntry(EntryType.valueOf(rs.getString(1)), rs.getString(2), rs.getString(3),
						rs.getTimestamp(4), rs.getTimestamp(5), rs.getInt(6), rs.getInt(7), rs.getBoolean(8),
						rs.getFloat(9), rs.getFloat(10)));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("fail to recived the entries to the park");
			return null;
		}
		return resultList.toArray(new ParkEntry[] {});

	}

}
