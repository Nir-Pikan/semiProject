package logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import db.DbController;
import entities.DiscountEntity;
import entities.Order;
import entities.Order.IdType;
import entities.ParkEntry;
import entities.ParkEntry.EntryType;
import modules.IController;
import modules.ServerRequest;

/**the discount controller class*/
public class DiscountController implements IController {

	private final static float FullPriceForPerson = 100;
	private final float orderPersonDiscountRegular = 0.85f, orderPersonalDiscountSubscriber = 0.8f,
			casualPersonDiscountRegular = 1f, casualPersonalDiscountSubscriber = 0.8f, orderGroupDiscount = 0.75f,
			inAdvancePayDiscount = 0.88f, orderGroupDiscountInstructor = 0f, casualGroupDiscount = 0.9f,
			casualGroupDiscountInstructor = 1f;
	private final String tableName = "discounts";

	DbController dbController;

	/** creates the {@link DiscountController} */
	public DiscountController() {
		dbController = DbController.getInstance();
		createTable();
	}

	/**
	 * Create Discount Table if not exist
	 * <p>
	 * table of {@link DiscountEntity}s
	 */
	private void createTable() {
		boolean isCreated = dbController.createTable(tableName + " ( discountID varchar(40),"
				+ " discountValue FLOAT NULL ," + " startDate TIMESTAMP(1) NULL ," + " endDate TIMESTAMP(1) NULL ,"
				+ " isApproved TINYINT NULL ," + " PRIMARY KEY (discountID)) ;");
		if (isCreated) {

			System.out.println("Table has been created");
		}
	}

	@Override
	public String handleRequest(ServerRequest request) {
		String job = request.job;
		String response = null;
		switch (job) {

		case "getAllDiscount":
			DiscountEntity[] discounts = getAllDiscount();
			response = ServerRequest.gson.toJson(discounts, DiscountEntity[].class);
			break;
		case "CalculatePriceForEntryByOrder": // to orderController
			 Order order =ServerRequest.gson.fromJson(request.data, Order.class);
			 if(order==null) {
				 response = "Failed to Calculate Price of Order got Null";
			 }
			 else {
				float price = CalculatePriceForEntryByOrder(order.numberOfVisitors,order.numberOfSubscribers,order.type==IdType.GUIDE,order.timeOfOrder);
				response=ServerRequest.gson.toJson(price, Float.class);
			 }
			break;
		case "CalculatePriceForEntryCasual":// to entryController
			ParkEntry entry = ServerRequest.gson.fromJson(request.data, ParkEntry.class);
			if(entry == null)
				response = "Failed to Calculate Price of Entry got Null";
			else {
				float price = CalculatePriceForEntryCasual(entry.numberOfVisitors,entry.numberOfSubscribers,entry.entryType == EntryType.Group ? true : false);
				response=ServerRequest.gson.toJson(price, Float.class);
			}
			break;

		case "AddNewDiscount":
			DiscountEntity newDiscount = ServerRequest.gson.fromJson(request.data, DiscountEntity.class);
			if (newDiscount == null) {
				response = "Failed to add Discount got Null";
			} else if (AddNewDiscount(newDiscount)) {
				response = "Discount was added successfully";
			} else {
				response = "Failed to add Discount";
			}
			break;

		case "ApproveDiscount":
			String discountId = request.data;

			if (discountId == null) {
				response = "Failed to update Discount got Null";
			} else if (ApproveDiscount(discountId)) {
				response = "Discount was updated successfully";
			} else {
				response = "Failed to update Discount";
			}
			break;

		case "DeleteDiscount":
			String discountID = request.data;
			if (discountID == null) {
				response = "Failed to delete Discount got Null";
			} else if (DeleteDiscount(discountID)) {
				response = "Discount was delete successfully";
			} else {
				response = "Failed to delete Discount";
			}
			break;

		default:
			response = "Error: No such job";

		}

		return response;
	}

	/**
	 * Receive all Discounts which not approved yet
	 * 
	 * @return array type DiscountEntity with isApprove==false
	 */
	public DiscountEntity[] getAllDiscount() {

		ResultSet rs = dbController.sendQuery("select * from " + tableName + " WHERE isApproved = false;");
		if (rs == null)
			return null;
		ArrayList<DiscountEntity> resultList = new ArrayList<>();
		try {
			while (rs.next()) {

				resultList.add(new DiscountEntity(rs.getString(1), rs.getFloat(2), rs.getTimestamp(3),
						rs.getTimestamp(4), rs.getBoolean(5)));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList.toArray(new DiscountEntity[] {});

	}

	/**
	 * Update discount "isApproved" to be true by discount id
	 * 
	 * @param discountID the discount we want to approve
	 * @return did the method succeeded
	 */
	public boolean ApproveDiscount(String discountID) {
		PreparedStatement pstmt = dbController
				.getPreparedStatement("UPDATE " + tableName + " SET isApproved = true WHERE ( discountID = ? );");
		try {
			pstmt.setString(1, discountID);
			return pstmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to execute update");
		}
		return false;

	}

	/**
	 * Calculate the price of the order in advance <strong>
	 * <p>
	 * Rules: <br>
	 * numberOfSubscribers >= 1 <br>
	 * TotalVisitors >= numberOfSubscribers <br>
	 * numberOfSubscribers counted inside TotalVisitors <br>
	 * The instructor counted inside TotalVisitors if Group </strong>
	 * 
	 * @param TotalVisitors       all the visitors !including! the instructor and
	 *                            the subscribers (if has)
	 * @param numberOfSubscribers the number of subscriber from total amount of
	 *                            visitors
	 * @param isGroup             to implement the appropriate discounts
	 * @param orderTime           for adding additional discounts if exists
	 * @return return the price after discounts
	 */
	public float CalculatePriceForEntryByOrder(int TotalVisitors, int numberOfSubscribers, boolean isGroup,
			Timestamp orderTime) {
		float inovice;

		if (isGroup) {
			// first calculate the price for the group without the instructor
		//	inovice = FullPriceForPerson * (TotalVisitors - 1) * orderGroupDiscount * inAdvancePayDiscount;
			inovice = FullPriceForPerson * (TotalVisitors - 1) * orderGroupDiscount; // (Roman) no payment in advance 
			// add the price for the instructor
			inovice += FullPriceForPerson * orderGroupDiscount * orderGroupDiscountInstructor;

		} else {

			// first calculate the sum for the non subscribers
			float nonSubscribers = FullPriceForPerson * (TotalVisitors - numberOfSubscribers)
					* orderPersonDiscountRegular;

			// now add the sum for the subscribers
			float subscribes = FullPriceForPerson * numberOfSubscribers * orderPersonDiscountRegular
					* orderPersonalDiscountSubscriber;

			inovice = nonSubscribers + subscribes;
		}

		inovice = ApplyDiscount(inovice, orderTime);

		return inovice;
	}

	/**
	 * Calculate the price of the order in advance <strong>
	 * <p>
	 * Rules: <br>
	 * numberOfSubscribers >= 1 <br>
	 * TotalVisitors >= numberOfSubscribers <br>
	 * numberOfSubscribers counted inside TotalVisitors <br>
	 * The instructor counted inside TotalVisitors if Group </strong>
	 * 
	 * @param TotalVisitors       all the visitors !including! the instructor and
	 *                            the subscribers (if has)
	 * @param numberOfSubscribers the number of subscriber from total amount of
	 *                            visitors
	 * @param isGroup             to implement the appropriate discounts
	 * @return return the price after discounts
	 */
	public float CalculatePriceForEntryCasual(int TotalVisitors, int numberOfSubscribers, boolean isGroup) {
		float inovice;

		if (isGroup) {
			// first calculate the price for the group without the instructor
			inovice = FullPriceForPerson * (TotalVisitors - 1) * casualGroupDiscount;
			// add the price for the instructor
			inovice += FullPriceForPerson * casualGroupDiscount * casualGroupDiscountInstructor;

		} else {

			// first calculate the sum for the non subscribers
			float nonSubscribers = FullPriceForPerson * (TotalVisitors - numberOfSubscribers)
					* casualPersonDiscountRegular;

			// now add the sum for the subscribers
			float subscribes = FullPriceForPerson * numberOfSubscribers * casualPersonDiscountRegular
					* casualPersonalDiscountSubscriber;

			inovice = nonSubscribers + subscribes;
		}
		Timestamp orderTime = new Timestamp(System.currentTimeMillis());
		inovice = ApplyDiscount(inovice, orderTime);

		return inovice;
	}

	/**
	 * Adding new Discount for dataBase
	 * 
	 * @param newDiscount the new discount which you want to add
	 * @return did the method succeeded
	 */
	public boolean AddNewDiscount(DiscountEntity newDiscount) {

		PreparedStatement pstmt = dbController.getPreparedStatement("INSERT INTO " + tableName
				+ "( discountID , discountValue , startDate , endDate , isApproved ) VALUES ( ? , ? , ? , ? , ? );");
		try {
			pstmt.setString(1, newDiscount.DiscountID);
			pstmt.setFloat(2, newDiscount.DiscountValue);
			pstmt.setTimestamp(3, newDiscount.StartTime);
			pstmt.setTimestamp(4, newDiscount.EndTime);
			pstmt.setBoolean(5, newDiscount.IsApproved);

			return pstmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to execute update");
		}

		return false;

	}

	/**
	 * Search and Delete discount by id if available
	 * 
	 * @param discount the discount which you want to delete
	 * @return did the method succeeded
	 */
	public boolean DeleteDiscount(String discountID) {

		PreparedStatement pstmt = dbController
				.getPreparedStatement("DELETE FROM " + tableName + " WHERE ( discountID = ? );");
		try {
			pstmt.setString(1, discountID);
			return pstmt.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to execute update");
		}
		return false;
	}

	/**
	 * Search and apply maximal discount if there is for time of the order
	 * 
	 * @param currentPrice the price before applying discount
	 * @param orderTime    time of the order for checking if there is discount for
	 *                     the time of order
	 * @return if succeeded will return the final price after applying discount
	 */
	private float ApplyDiscount(float currentPrice, Timestamp orderTime) {

		float newPrice=0;

		PreparedStatement pstmt = dbController.getPreparedStatement(
				"SELECT MAX(discountValue) FROM " + tableName + "  where timestamp(startDate) <= timestamp(?)"
						+ " and  timestamp(endDate)>=timestamp( ? ) and isApproved=true ;");

		try {
			pstmt.setTimestamp(1, orderTime);
			pstmt.setTimestamp(2, orderTime);
			ResultSet res = pstmt.executeQuery();
			if (res == null) {
				return currentPrice;
			}
			float discountApply =0;
			if(res.next()) {
				discountApply = res.getFloat(1);// discountApply should be between 0.00 to 1.00				
			}
			if (discountApply < 0 || discountApply > 1)
				return currentPrice;

			discountApply = 1 - discountApply;
			newPrice = discountApply * currentPrice;
			return newPrice;

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println(pstmt.toString());
			System.out.println("Failed to execute query");
			return currentPrice;
		}
	}

}
