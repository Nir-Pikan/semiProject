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
	 * @param parkID               String
	 * @param parkName             String
	 * @param maxCapacity          int
	 * @param managerID            String
	 * @param maxPreOrders         int
	 * @param avgVisitTime         double
	 * @param currentNumOfVisitors int
	 * @param openTime             int
	 * @param closeTime            int
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
	 * @param parkID               String
	 * @param parkName             String
	 * @param maxCapacity          int
	 * @param managerID            String
	 * @param maxPreOrders         int
	 * @param currentNumOfVisitors int
	 * @param openTime             int
	 * @param closeTime            int
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
