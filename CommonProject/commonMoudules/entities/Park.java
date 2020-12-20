package entities;

/**
 * A class containing the data of a single park.
 * <p>
 */
public class Park {

	public String parkID;
	public String parkName;
	/** Max number of visitors in the park in any given moment */
	public int maxCapacity;
	public String managerID;// TODO why?

	/**
	 * Max number of visitors in all of the orders in a given time, must be less
	 * than maxCapacity
	 */
	public int maxPreOrders;

	/** Average time for visitor to be in the park */
	public double avgVisitTime = 4.0;

	/** Number of visitors in this moment */
	public int currentNumOfVisitors;

	public int openTime;

	public int closeTime;

	/**
	 * creates a {@link Park} using all fields from the DB.
	 * 
	 * @param parkID               the park's ID
	 * @param parkName             the park's name
	 * @param maxCapacity          the park's max visitor capacity
	 * @param managerID            the park manager's ID
	 * @param maxPreOrders         the maximum amount of preorders allowed
	 * @param avgVisitTime         visitors average visit time
	 * @param currentNumOfVisitors amount of visitors currently at the park
	 * @param openTime             the park's open time
	 * @param closeTime            the park's close time
	 */
	public Park(String parkID, String parkName, int maxCapacity, String managerID, int maxPreOrders,
			double avgVisitTime, int currentNumOfVisitors, int openTime, int closeTime) {
		this.parkID = parkID;
		this.parkName = parkName;
		this.maxCapacity = maxCapacity;
		this.managerID = managerID;
		this.maxPreOrders = maxPreOrders;
		this.avgVisitTime = avgVisitTime;
		this.currentNumOfVisitors = currentNumOfVisitors;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

	/**
	 * creates a new {@link Park}.
	 * <p>
	 * (the Average VisitTime is 4 hours)
	 * 
	 * @param parkID               the park's ID
	 * @param parkName             the park's name
	 * @param maxCapacity          the park's max visitor capacity
	 * @param managerID            the park manager's ID
	 * @param maxPreOrders         the maximum amount of preorders allowed
	 * @param currentNumOfVisitors amount of visitors currently at the park
	 * @param openTime             the park's open time
	 * @param closeTime            the park's close time
	 */
	public Park(String parkID, String parkName, int maxCapacity, String managerID, int maxPreOrders,
			int currentNumOfVisitors, int openTime, int closeTime) {
		this.parkID = parkID;
		this.parkName = parkName;
		this.maxCapacity = maxCapacity;
		this.managerID = managerID;
		this.maxPreOrders = maxPreOrders;
		this.currentNumOfVisitors = currentNumOfVisitors;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

}
