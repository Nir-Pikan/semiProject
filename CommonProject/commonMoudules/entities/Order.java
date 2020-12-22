package entities;

import java.sql.Timestamp;

public class Order {
	public enum IdType {
		PRIVATE, PRIVATEGROUP, GUIDE, FAMILY
	};
	public enum OrderStatus {
		CANCEL,IDLE,CONFIRMED,WAITINGLIST,WAITINGLISTMASSAGESENT
	};
	// Identification Number // not for now
	public String parkSite;
	public int numberOfVisitors;
	public int orderID = 1; // change letter serial number of the order
	public float priceOfOrder; // should be calculated
	public String email, phone;
	public IdType type;
	public OrderStatus orderStatus;
	public Timestamp visitTime;
	public Timestamp timeOfOrder;
	public boolean isUsed; // by default false
	
	public Order(String parkSite, int numberOfVisitors, int orderID, float priceOfOrder, String email, String phone,
			IdType type, OrderStatus orderStatus, Timestamp visitTime, Timestamp timeOfOrder, boolean isUsed) {
		super();
		this.parkSite = parkSite;
		this.numberOfVisitors = numberOfVisitors;
		this.orderID = orderID;
		this.priceOfOrder = priceOfOrder;
		this.email = email;
		this.phone = phone;
		this.type = type;
		this.orderStatus = orderStatus;
		this.visitTime = visitTime;
		this.timeOfOrder = timeOfOrder;
		this.isUsed = isUsed;
	}

	

}
