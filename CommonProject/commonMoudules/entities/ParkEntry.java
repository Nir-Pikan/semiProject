package entities;

import java.sql.Timestamp;

/** a class containing the data of a single park entry */
public class ParkEntry {

	public EntryType entryType;
	public String personID;
	public String parkID;
	public Timestamp arriveTime;
	public Timestamp exitTime;
	public int numberOfVisitors;
	public boolean isCasual;
	public float priceOfOrder;
	public float priceOfEntry;
	public int numberOfSubscribers;

	/**
	 * <b>Do not use</b>
	 * <p>
	 * <b>For usage of DB only by receiving data</b>
	 */
	public ParkEntry(EntryType entryType, String personID, String parkID, Timestamp arriveTime, Timestamp exitTime,
			int numberOfVisitors, int numberOfSubscribers, boolean isCasual, float priceOfOrder, float priceOfEntry) {
		this.entryType = entryType;
		this.personID = personID;
		this.parkID = parkID;
		this.arriveTime = arriveTime;
		this.exitTime = exitTime;
		this.numberOfVisitors = numberOfVisitors;
		this.numberOfSubscribers = numberOfSubscribers;
		this.isCasual = isCasual;
		this.priceOfOrder = priceOfOrder;
		this.priceOfEntry = priceOfEntry;
	}

	/**
	 * Constructor for adding new parkEntry
	 * 
	 * @param entryType           can be {Personal, Subscriber, Group, PrivateGroup}
	 * @param personID            the ID of person who made the reservation
	 * @param parkID              ID of the park
	 * @param arriveTime          entry time to the park
	 * @param exitTime            exit time from the park
	 * @param numberOfVisitors    number of total visitors
	 * @param numberOfSubscribers number of subscribers of total visitors
	 * @param isCasual            true only if its a casual visit (and not a
	 *                            registered order)
	 * @param price               the price of the entry
	 */
	public ParkEntry(EntryType entryType, String personID, String parkID, Timestamp arriveTime, Timestamp exitTime,
			int numberOfVisitors, int numberOfSubscribers, boolean isCasual, float price) {
		this.entryType = entryType;
		this.personID = personID;
		this.parkID = parkID;
		this.arriveTime = arriveTime;
		this.exitTime = exitTime;
		this.numberOfVisitors = numberOfVisitors;
		this.numberOfSubscribers = numberOfSubscribers;
		this.isCasual = isCasual;
		if (isCasual) {
			this.priceOfEntry = price;
			this.priceOfOrder = 0;
		} else {

			this.priceOfEntry = 0;
			this.priceOfOrder = 0;
		}

	}

	public static enum EntryType {
		Personal, Subscriber, Group, PrivateGroup
	}

}
