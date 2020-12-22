package logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entities.DiscountEntity;
import entities.Order;
import entities.Subscriber;
import entities.Order.IdType;
import entities.Order.OrderStatus;
import entities.Subscriber.Type;
import io.DbController;
import modules.IController;
import modules.ServerRequest;

public class OrderController implements IController {

	private ParkController park;
	private MessageController messageC; // TODO send a mail if order placed
	private SubscriberController subscriber;
	private DiscountController discount;

	private DbController dbController;

	public OrderController(ParkController park, MessageController messageC, SubscriberController subscriber,
			DiscountController discount) {
		this.park = park;
		this.messageC = messageC;
		this.subscriber = subscriber;
		this.discount = discount;
		dbController = DbController.getInstance();
		createTable();
		// TODO Auto-generated constructor stub
	}

	/** Create Order table in DB if not exists */
	private void createTable() {

		boolean isCreated = dbController.createTable("orders(parkSite varchar(20),numberOfVisitor int,orderID int,"
				+ "priceOfOrder FLOAT, email varchar(20),phone varchar(20), IdType ENUM('PRIVATE','PRIVATEGROUP','GUIDE','FAMILY'),"
				+ "orderStatus ENUM('CANCEL','IDLE','CONFIRMED','WAITINGLIST','WAITINGLISTMASSAGESENT'),"
				+ "visitTime TIMESTAMP(1), timeOfOrder TIMESTAMP(1), isUsed BOOLEAN, primary key(orderID));");
		if (isCreated)
			System.out.println("Table has been created");
	}

	@Override
	public String handleRequest(ServerRequest request) {
		String job = request.job;
		String response = null;
		Order ord;
		Timestamp startAndEnd[] = new Timestamp[2];
		switch (job) {

		case "GetOrderByID":
			Integer orderID = ServerRequest.gson.fromJson(request.data, Integer.class);
			ord = GetOrderByID(orderID);
			if (ord == null)
				response = "Order was not found";

			else
				response = ServerRequest.gson.toJson(ord);
			break;

		case "AddNewOrder":
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
			// try to add order
			if (AddNewOrder(ord))
				response = "Order was added successfully";
			else
				response = "Failed to add Order";
			break;
		case "NextOrderID":
			Integer nextOrderID = NextOrderID();
			response = ServerRequest.gson.toJson(nextOrderID);
			break;
		case "GetOrderListForDate":
			startAndEnd = ServerRequest.gson.fromJson(request.data, Timestamp[].class); // get start and end time from String
			Order[] orders = GetOrderListForDate(startAndEnd[0],startAndEnd[1]); // get the orders
			response = ServerRequest.gson.toJson(orders, Order[].class);
			break;
		default:
			response = "Error: No such job";
		}
		return response;
	}

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
						res.getTimestamp(9), res.getTimestamp(10), res.getBoolean(11));
			}
			res.close();
			return o;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Ask DB to add a new subscriber to the Subscribers table
	 * 
	 * @param sub Subscriber to add
	 * @return boolean did the function succeed
	 */
	private boolean AddNewOrder(Order ord) {

		// add the subscriber to the subscribers table
		PreparedStatement ps = dbController
				.getPreparedStatement("INSERT INTO orders VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
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

			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Ask DB to add a new subscriber to the Subscribers table
	 * 
	 * @param sub Subscriber to add
	 * @return boolean did the function succeed
	 * @throws SQLException
	 */
	private boolean IsOrderAllowed(Order ord) {
		// int muxPreOrder = park.getMaxPreOrder(ord.parkSite); //real method
		int muxPreOrder = 4; // for test only
		int resInt = 10000; // to be sure that by default we don't have place in the park, stupid......
		// Timestamp from = ord.visitTime.getHours();
		Timestamp threeHoursBefor = SubtractThreeHours(ord.visitTime);
		Timestamp fourHoursAfter = AddFourHours(ord.visitTime);
		try {
			ResultSet ps = dbController.sendQuery( // count the number of orders 3 hours before and 4 hours after
					"SELECT COUNT(parkSite)\r\n" + " FROM orders \r\n" + " WHERE visitTime > \"" + threeHoursBefor
							+ "\" && visitTime < \"" + fourHoursAfter + "\" && parkSite = \"" + ord.parkSite + "\";");
			if (ps.next())
				resInt = ps.getInt(1);
			ps.close();
			if (resInt < muxPreOrder)
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	// maybe better to make one function that depends on a average visit time of
	// each park ??????????????????????
	private Timestamp SubtractThreeHours(Timestamp stamp) {
		long current = stamp.getTime();
		long substracted = current - 180 * 60 * 1000;
		return new Timestamp(substracted);
	}

	private Timestamp AddFourHours(Timestamp stamp) {
		long current = stamp.getTime();
		long increased = current + 240 * 60 * 1000;
		return new Timestamp(increased);
	}

	/**
	 * Ask DB to add a new subscriber to the Subscribers table
	 * 
	 * @param sub Subscriber to add
	 * @return boolean did the function succeed
	 */
	private void InItMassagerReminder() {

	}

	// TODO GetOrderListForDate() ask Michael he did something very similar
	// TODO GetAvailableSlots() real shit do in later
	// TODO InItMassagerReminder() weird stuff
	// TODO CancelOrder()
	/*
	 * TODO UpdateOrder() friend of CanselOrder() !!!! it's easier to delete the old
	 * one and add a "new" after change
	 */
	// TODO GetOrderByID() done

	public Order[] GetOrderListForDate(Timestamp start, Timestamp end) {
		ResultSet res = dbController.sendQuery(
				"SELECT * FROM orders WHERE visitTime > \"" + start + "\" && visitTime < \"" + end + "\"\r\n" + "");
		if (res == null)
			return null;
		ArrayList<Order> resultList = new ArrayList<>();
		try {
			while (res.next()) {
				resultList.add(new Order(res.getString(1), res.getInt(2), res.getInt(3), res.getFloat(4),
						res.getString(5), res.getString(6), IdType.valueOf(res.getString(7)),
						OrderStatus.valueOf(res.getString(8)), res.getTimestamp(9), res.getTimestamp(10),
						res.getBoolean(11)));
			}

			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList.toArray(new Order[] {});
	}


}
