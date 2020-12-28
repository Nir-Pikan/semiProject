package logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import entities.Order;
import entities.Order.IdType;
import entities.Order.OrderStatus;
import io.DbController;
import modules.IController;
import modules.ObservableList;
import modules.ServerRequest;

public class OrderController implements IController {

	private int openingHour = 8; // take the data from DB
	private int closeHour = 16; // take the data from DB
	private int AVGvisitTime = 4; // take the data from DB
	private ParkController park;
	private MessageController messageC; 
	private SubscriberController subscriber;
	private DiscountController discount;
	
	private DbController dbController;
	
	public ObservableList<Order> canceled; // list of canceled Orders

	public OrderController(ParkController park, MessageController messageC, SubscriberController subscriber,
			DiscountController discount) {
		this.park = park;
		this.messageC = messageC;
		this.subscriber = subscriber;
		this.discount = discount;
		dbController = DbController.getInstance();
		createTable();
		canceled = new ObservableList<Order>();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create Order table in DB if not exists
	 */
	private void createTable() {
		// please consider that type (enum value) name was changed from IDtype to type,
		// hope no bug is will be found
		boolean isCreated = dbController.createTable("orders(parkSite varchar(20),numberOfVisitors int,orderID int,"
				+ "priceOfOrder FLOAT, email varchar(20),phone varchar(20), type ENUM('PRIVATE','PRIVATEGROUP','GUIDE','FAMILY'),"
				+ "orderStatus ENUM('CANCEL','IDLE','CONFIRMED','WAITINGLIST','WAITINGLISTMASSAGESENT'),"
				+ "visitTime TIMESTAMP(1), timeOfOrder TIMESTAMP(1), isUsed BOOLEAN,ownerID varchar(20),"
				+ "numberOfSubscribers int, primary key(orderID));");
		if (isCreated)
			System.out.println("Table has been created");
	}

	/**<pre>
	 * GetOrderByID - returns an Order by ID if the Order exists in the DB
	 * 
	 * AddNewOrder - add a new order to the DB .
	 * IsOrderAllowed - checks if order can be booked(is park is not full) my checking the number of orders that exists
	 * in a DB for that park in this time range.
	 * 
	 * NextOrderID - generates the orderID of a next order.
	 * 
	 * GetOrderListForDate - returns Orders in that range of time in specific park.
	 * 
	 * GetAllListOfOrders - returns all the orders from DB.
	 * 
	 * GetOrderByVisitorID - get Order by ID of an owner (ownerID).
	 * 
	 * CancelOrderByOrderID - set isUsed field of an Order entity to true and OrderStatus enum to CANCEL.
	 * 
	 * SetOrderToIsUsed - set isUsed field of an Order entity to true.
	 * 
	 * UpdateOrder - Updates all the fields of Order entity with the same orderID,
	 * how to use: get the Order from DB change all fields you want BUT NOT THE orderID! and send the new Order to this function
	 * PLEASE use this function ONLY if realy necessary. Don't use it for example to set status of Order to used.
	 * </pre>
	 */
	@Override
	public String handleRequest(ServerRequest request) {
		String job = request.job;
		String response = null;
		Order ord;
		Order[] orders;
		String startTimeAndEndTimeAndParkSite[] = new String[3];
		Integer orderID;
		Integer nextOrderID;
		Timestamp start;
		Timestamp end;
		String parkSite;
		boolean answer;
		switch (job) {

		case "GetOrderByID":
			orderID = ServerRequest.gson.fromJson(request.data, Integer.class);
			ord = GetOrderByID(orderID);
			if (ord == null)
				response = "Order was not found";

			else
				response = ServerRequest.gson.toJson(ord);
			break;

		case "AddNewOrder":
			ord = ServerRequest.gson.fromJson(request.data, Order.class);

			// first check if order already exists
			if (GetOrderByID(ord.orderID) != null) { // TODO this checking is duplicated, but better not to delete in my
														// opinion (Roman)
				response = "Order already exists";
				break;
			}
			// second check if Order Allowed
			if (!IsOrderAllowed(ord)) { // TODO this checking is duplicated, but better not to delete in my opinion
										// (Roman)
				response = "No more orders allowed in this time";
				break;
			}
			// try to add order
			if (AddNewOrder(ord)) // now this part is duplicated line 103
				response = "Order was added successfully";
			else
				response = "Failed to add Order";
			break;
		case "IsOrderAllowed":
			ord = ServerRequest.gson.fromJson(request.data, Order.class);
			// first check if order already exists
			if (GetOrderByID(ord.orderID) != null) {
				response = "Order already exists";
				break;
			}
			// second check if Order Allowed
			if (!IsOrderAllowed(ord)) {
				response = "No more orders allowed in this time";
				break;
			}
			response = "Order can be placed"; // not realy necessary
			break;
		case "NextOrderID":
			nextOrderID = NextOrderID();
			response = ServerRequest.gson.toJson(nextOrderID);
			break;
		case "GetOrderListForDate":
			startTimeAndEndTimeAndParkSite = ServerRequest.gson.fromJson(request.data, String[].class);
			start = ServerRequest.gson.fromJson(startTimeAndEndTimeAndParkSite[0], Timestamp.class);
			end = ServerRequest.gson.fromJson(startTimeAndEndTimeAndParkSite[1], Timestamp.class);
			parkSite = startTimeAndEndTimeAndParkSite[2];
			orders = GetOrderListForDate(start, end, parkSite); // get the orders
			response = ServerRequest.gson.toJson(orders, Order[].class);
			break;
		case "GetAllListOfOrders":
			Order[] allOrders = GetAllListOfOrders();
			response = ServerRequest.gson.toJson(allOrders, Order[].class);
			break;
		case "GetOrderByVisitorID":
			String ownerID = ServerRequest.gson.fromJson(request.data, String.class);
			orders = GetOrdersByVisitorID(ownerID);
			if (orders == null)
				response = "Owner with this ID is not found";
			else
				response = ServerRequest.gson.toJson(orders, Order[].class);
			break;
		case "CancelOrderByOrderID": ///////////////////////////////////////////////////////////// not sure if
										///////////////////////////////////////////////////////////// needed
			orderID = ServerRequest.gson.fromJson(request.data, Integer.class);
			answer = CancelOrderByOrderID(orderID);
			if (answer)
				// response = ServerRequest.gson.toJson(answer);
				response = "Order Canceled";
			else
				response = "Failed to cancel an order";
			break;
		case "SetOrderToIsUsed": ///////////////////////////////////////////////////////////// not shore if
									///////////////////////////////////////////////////////////// needed
			orderID = ServerRequest.gson.fromJson(request.data, Integer.class);
			answer = SetOrderToIsUsed(orderID);
			if (answer)
				// response = ServerRequest.gson.toJson(answer);
				response = "Order seted as used";
			else
				response = "Failed to set order as used";
			break;
		case "UpdateOrder":
			ord = ServerRequest.gson.fromJson(request.data, Order.class);
			answer = UpdateOrder(ord);
			if (answer)
				// response = ServerRequest.gson.toJson(answer);
				response = "Order updated";
			else
				response = "Failed to update order";
			break;
		default:
			response = "Error: No such job";
		}
		return response;
	}

	/**
	 * method that returns Order from DB by Order ID
	 * 
	 * @param orderID
	 * @return Order if order exists in the DB, null otherwise
	 */
	private Order GetOrderByID(int orderID) {
		String statment = "SELECT * FROM orders WHERE orderID = \"" + orderID + "\";";
		ResultSet res = dbController.sendQuery(statment);

		if (res == null)
			return null;

		Order o = null;
		try {
			if (res.next()) {
				// Print out the values
				o = new Order(res.getString(1), res.getInt(2), res.getInt(3), res.getFloat(4), res.getString(5),
						res.getString(6), IdType.valueOf(res.getString(7)), OrderStatus.valueOf(res.getString(8)),
						res.getTimestamp(9), res.getTimestamp(10), res.getBoolean(11), res.getString(12),
						res.getInt(13));
			}
			res.close();
			return o;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Ask DB to add a new Order to the orders table
	 * 
	 * @param Order to write in the DB
	 * @return boolean did the function succeed or not
	 */
	public boolean AddNewOrder(Order ord) {
		PreparedStatement ps = dbController
				.getPreparedStatement("INSERT INTO orders VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		try {
			ps.setString(1, ord.parkSite);
			ps.setInt(2, ord.numberOfVisitors);
			ps.setLong(3, ord.orderID);
			ps.setFloat(4, ord.priceOfOrder);
			ps.setString(5, ord.email);
			ps.setString(6, ord.phone);
			ps.setString(7, ord.type.toString()); // can be stored as enum ?
			ps.setString(8, ord.orderStatus.toString()); // can be stored as enum ?
			ps.setTimestamp(9, ord.visitTime);
			ps.setTimestamp(10, ord.timeOfOrder);
			ps.setBoolean(11, ord.isUsed);
			ps.setString(12, ord.ownerID);
			ps.setInt(13, ord.numberOfSubscribers);

			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Check if the order is allowed by checking the amount of order in DB in orders
	 * date and in range depends on AVG visit time in the park
	 * 
	 * @param ord The Order entity that will be added to the DB
	 * @return true if the order placed, false if the park is full in this range of
	 *         time
	 */
	// TODO check
	private boolean IsOrderAllowed(Order ord) {
		// int muxPreOrder = park.getMaxPreOrder(ord.parkSite); //real method
		int muxPreOrder = 4; // for test only
		int resInt = 10000; // to be sure that by default we don't have place in the park, stupid......
		Timestamp threeHoursBefor = addTimeInHours(ord.visitTime, -(AVGvisitTime - 1)); // calculate 4 hours after visit
																						// // time
		Timestamp fourHoursAfter = addTimeInHours(ord.visitTime, AVGvisitTime); // calculate 3 hours before
		try {
			ResultSet ps = dbController.sendQuery( // count the number of orders 3 hours before and 4 hours after
					"SELECT SUM(numberOfVisitors)" + " FROM orders " + " WHERE visitTime >= \"" + threeHoursBefor
							+ "\" && visitTime <= \"" + fourHoursAfter + "\" && parkSite = \"" + ord.parkSite
							+ "\" && orderStatus <> \"CANCEL\";"); // TODO test this (Roman)
			if (ps.next())
				resInt = ps.getInt(1);
			ps.close();
			if (resInt + ord.numberOfVisitors <= muxPreOrder)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Method only for waiting list
	 * 
	 * @param order
	 * @param numberOfVisitorsCanceled
	 * @return
	 */
	public boolean IsOrderAllowedWaitingList(Order order, int numberOfVisitorsCanceled) {
		// int muxPreOrder = park.getMaxPreOrder(ord.parkSite); //real method
		int muxPreOrder = 4; // for test only
		int resInt = 10000; // to be sure that by default we don't have place in the park, stupid......
		Timestamp threeHoursBefor = addTimeInHours(order.visitTime, -(AVGvisitTime - 1)); // calculate 4 hours after
																							// visit // time
		Timestamp fourHoursAfter = addTimeInHours(order.visitTime, AVGvisitTime); // calculate 3 hours before
		try {
			ResultSet ps = dbController.sendQuery( // count the number of orders 3 hours before and 4 hours after
					"SELECT SUM(numberOfVisitors)" + " FROM orders " + " WHERE visitTime >= \"" + threeHoursBefor
							+ "\" && visitTime <= \"" + fourHoursAfter + "\" && parkSite = \"" + order.parkSite
							+ "\" && orderStatus <> \"CANCEL\";"); // TODO test this (Roman)
			if (ps.next())
				resInt = ps.getInt(1);
			ps.close();
			if (resInt + order.numberOfVisitors - numberOfVisitorsCanceled <= muxPreOrder)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int NextOrderID() {
		int res = 1; // in a case of empty table
		try {
			ResultSet ps = dbController.sendQuery("SELECT MAX(orderID) FROM orders");
			if (ps.next())
				res = ps.getInt(1) + 1; // + for next ID in the DB
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	// TODO test and use this (Roman)
	private void InItMassagerReminder(Order order) {
		messageC.SendEmailAndSMS(order.email, order.phone, genereteMessage(order), "GoNature Remainder");
	}

	private String genereteMessage(Order order) {
		return "Your booking indformation:\n" +
	"OrderId: " + order.orderID +"\n" +
				"visit time: " + order.visitTime.getDate() + " " + order.visitTime.getTime() + "\n" +
				"number of visitors: " + order.numberOfVisitors + "\n" +
				"Price: "+ order.priceOfOrder + 
				"Thank for using GoNature";
	}
	/**
	 * Method that return all the Orders entity from DB that in a range of time (
	 * start < visit time < end ) and date and from chosen park site
	 * 
	 * @param start
	 * @param end
	 * @param parkSite
	 * @return array of orders in that range of time
	 */
	public Order[] GetOrderListForDate(Timestamp start, Timestamp end, String parkSite) {
		ResultSet res = dbController.sendQuery("SELECT *\r\n" + " FROM orders \r\n" + " WHERE visitTime >= \"" + start
				+ "\" && visitTime <= \"" + end + "\" && parkSite = \"" + parkSite + "\";");

		if (res == null)
			return null;
		ArrayList<Order> resultList = new ArrayList<>();
		try {
			while (res.next()) {
				resultList.add(new Order(res.getString(1), res.getInt(2), res.getInt(3), res.getFloat(4),
						res.getString(5), res.getString(6), IdType.valueOf(res.getString(7)),
						OrderStatus.valueOf(res.getString(8)), res.getTimestamp(9), res.getTimestamp(10),
						res.getBoolean(11), res.getString(12), res.getInt(13)));
			}

			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList.toArray(new Order[] {});
	}

	// maybe will be needed at least for testing
	/**
	 * Returns all the Orders from all the parks
	 * 
	 * @return
	 */
	public Order[] GetAllListOfOrders() { //TODO maybe not necessary (Roman)
		ResultSet res = dbController.sendQuery("SELECT * FROM orders");
		if (res == null)
			return null;
		ArrayList<Order> resultList = new ArrayList<>();
		try {
			while (res.next()) {
				resultList.add(new Order(res.getString(1), res.getInt(2), res.getInt(3), res.getFloat(4),
						res.getString(5), res.getString(6), IdType.valueOf(res.getString(7)),
						OrderStatus.valueOf(res.getString(8)), res.getTimestamp(9), res.getTimestamp(10),
						res.getBoolean(11), res.getString(12), res.getInt(13)));
			}

			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList.toArray(new Order[] {});
	}

	/**
	 * Methods returns all the orders that was made by ownerID from all the parks
	 * 
	 * @param ID
	 * @return Array of Orders
	 */
	// TODO should we add more conditions?
	public Order[] GetOrdersByVisitorID(String ID) {
		ResultSet res = dbController.sendQuery("SELECT * FROM orders WHERE ownerID =" + ID + "");
		if (res == null)
			return null;
		ArrayList<Order> resultList = new ArrayList<>();
		try {
			while (res.next())
				resultList.add(new Order(res.getString(1), res.getInt(2), res.getInt(3), res.getFloat(4),
						res.getString(5), res.getString(6), IdType.valueOf(res.getString(7)),
						OrderStatus.valueOf(res.getString(8)), res.getTimestamp(9), res.getTimestamp(10),
						res.getBoolean(11), res.getString(12), res.getInt(13)));

			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList.toArray(new Order[] {});

	}

	/**
	 * This method set the orderStatus with orderID to CANCEL
	 * 
	 * @param orderID
	 * @return true if order canceled, false otherwise
	 */
	public boolean CancelOrderByOrderID(int orderID) {
		canceled.add(GetOrderByID(orderID));//add order to canceled list
		PreparedStatement pstmt = dbController
				.getPreparedStatement("UPDATE orders SET orderStatus = \"CANCEL\", isUsed = true WHERE orderID = ?;");
		try {
			pstmt.setInt(1, orderID);
			return pstmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to execute update");
		}
		return false;
	}

	/**
	 * Set Order with this orderID to used status
	 * 
	 * @param orderID
	 * @return true if order changed to used, false otherwise
	 */
	public boolean SetOrderToIsUsed(int orderID) {
		PreparedStatement pstmt = dbController
				.getPreparedStatement("UPDATE orders SET isUsed = true WHERE orderID = ?;");
		try {
			pstmt.setInt(1, orderID);
			return pstmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to set order like used");
		}
		return false;
	}

	/**
	 * Update the order with the same orderID DO NOT CAHNGE orderID
	 * !!!!!!!!!!!!!!!!!!!!!
	 * 
	 * @param ord
	 * @return true if the order was updated, false otherwise
	 */
	public boolean UpdateOrder(Order ord) {
		PreparedStatement ps = dbController.getPreparedStatement(
				"UPDATE orders SET isUsed = ?, parkSite = ? , numberOfVisitors = ?, priceOfOrder = ?,"
						+ " email = ?, phone = ?,type = ?, orderStatus = ?, visitTime = ?, timeOfOrder = ?,\r\n"
						+ "ownerID = ?,numberOfSubscribers = ?, WHERE orderID = ?");
		try {
			ps.setBoolean(1, ord.isUsed);
			ps.setString(2, ord.parkSite);
			ps.setInt(3, ord.numberOfVisitors);
			ps.setFloat(4, ord.priceOfOrder);
			ps.setString(5, ord.email);
			ps.setString(6, ord.phone);
			ps.setString(7, ord.type.toString()); // can be stored as enum ?
			ps.setString(8, ord.orderStatus.toString()); // can be stored as enum ?
			ps.setTimestamp(9, ord.visitTime);
			ps.setTimestamp(10, ord.timeOfOrder);
			ps.setString(11, ord.ownerID);
			ps.setLong(12, ord.orderID);
			ps.setInt(13, ord.numberOfSubscribers);
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Fail to update an order");
		}
		return false;
	}

	/**
	 * delete the order from DB
	 * 
	 * @param orderID
	 * @return true if Order was found and deleted, false otherwise
	 */
	// TODO check if needed
	public boolean deleteOrder(int orderID) {
		PreparedStatement pstmt = dbController.getPreparedStatement("DELETE FROM orders WHERE orderID = ?;");
		try {
			pstmt.setInt(1, orderID);
			return pstmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to delete order");
		}
		return false;
	}

	/**
	 * Change the Timestamp hour +/-
	 * 
	 * @param stamp
	 * @param hours
	 * @return
	 */
	private Timestamp addTimeInHours(Timestamp stamp, int hours) {
		long tempTimeLong = stamp.getTime();
		Timestamp temp = new Timestamp(tempTimeLong);
		int tempHours = stamp.getHours() + hours;
		if (tempHours < openingHour)
			temp.setHours(openingHour);
		else if (tempHours > closeHour)
			temp.setHours(closeHour);
		else
			temp.setHours(stamp.getHours() + hours);
		return temp;
	}

}
