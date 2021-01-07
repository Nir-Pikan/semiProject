package entities;

import java.sql.Timestamp;

/**
 * a class containing the data of a single order
 */
public class Order {
	public enum IdType {
		PRIVATE, PRIVATEGROUP, GUIDE, FAMILY
	};

	public enum OrderStatus {
		CANCEL, SEMICANCELED, IDLE, CONFIRMED, WAITINGLIST, WAITINGLISTMASSAGESENT
	};

	public String parkSite; // Identification Number // not for now
	public int numberOfVisitors;
	public int orderID;
	public float priceOfOrder;
	public String email, phone;
	public IdType type;
	public OrderStatus orderStatus;
	public Timestamp visitTime;
	public Timestamp timeOfOrder;
	public boolean isUsed; // by default false
	public String ownerID;
	public int numberOfSubscribers;

	public Order() {
	}

	/**
	 * Creates a {@link Order}
	 * 
	 * @param parkSite            the park's ID
	 * @param numberOfVisitors    the order's amount of visitors
	 * @param orderID             the order's ID
	 * @param priceOfOrder        the order's price
	 * @param email               the orderer's email
	 * @param phone               the orderer's phone number
	 * @param type                the order's type
	 * @param orderStatus         the order's status
	 * @param visitTime           the order's arrive time and date
	 * @param timeOfOrder         the time and date order was made
	 * @param isUsed              true if order was used to enter the park or
	 *                            canceled, false otherwise
	 * @param ownerID             the orderer's ID
	 * @param numberOfSubscribers the order's amount of subscribers
	 */
	public Order(String parkSite, int numberOfVisitors, int orderID, float priceOfOrder, String email, String phone,
			IdType type, OrderStatus orderStatus, Timestamp visitTime, Timestamp timeOfOrder, boolean isUsed,
			String ownerID, int numberOfSubscribers) {
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
		this.ownerID = ownerID;
		this.numberOfSubscribers = numberOfSubscribers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderID != other.orderID)
			return false;
		return true;
	}

}
