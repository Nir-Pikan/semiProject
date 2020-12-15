package entities;

/**class for the data of one park.</br>
 * Park(String parkID, String parkName, int maxCapacity, String managerID, int maxPreOrders, double avgVisitTime,int currentNumOfVisitors, int openTime, int closeTime)*/
public class Park {
	
public String parkID;
public String parkName;
/**Max number of visitors in the park in any given moment*/
public int maxCapacity;
public String managerID;//TODO why?

/**Max number of visitors in all of the orders in a given time, must be less than maxCapacity*/
public int maxPreOrders;

/**Average time for visitor to be in the park*/
public double avgVisitTime = 4.0;

/**Number of visitors in this moment*/
public int currentNumOfVisitors;

public int openTime;

public int closeTime;



/**create park from the DB insert all fields
 * @param parkID
 * @param parkName
 * @param maxCapacity
 * @param managerID
 * @param maxPreOrders
 * @param avgVisitTime
 * @param currentNumOfVisitors
 * @param openTime
 * @param closeTime
 */
public Park(String parkID, String parkName, int maxCapacity, String managerID, int maxPreOrders, double avgVisitTime,
		int currentNumOfVisitors, int openTime, int closeTime) {
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



/**create new park(the Avarage VisitTime is 4 hours)
 * @param parkID
 * @param parkName
 * @param maxCapacity
 * @param managerID
 * @param maxPreOrders
 * @param currentNumOfVisitors
 * @param openTime
 * @param closeTime
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
