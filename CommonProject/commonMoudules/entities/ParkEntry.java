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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arriveTime == null) ? 0 : arriveTime.hashCode());
		result = prime * result + ((entryType == null) ? 0 : entryType.hashCode());
		result = prime * result + ((exitTime == null) ? 0 : exitTime.hashCode());
		result = prime * result + (isCasual ? 1231 : 1237);
		result = prime * result + numberOfSubscribers;
		result = prime * result + numberOfVisitors;
		result = prime * result + ((parkID == null) ? 0 : parkID.hashCode());
		result = prime * result + ((personID == null) ? 0 : personID.hashCode());
		result = prime * result + Float.floatToIntBits(priceOfEntry);
		result = prime * result + Float.floatToIntBits(priceOfOrder);
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
		ParkEntry other = (ParkEntry) obj;
		if (arriveTime == null) {
			if (other.arriveTime != null)
				return false;
		} else if (!arriveTime.equals(other.arriveTime))
			return false;
		if (entryType != other.entryType)
			return false;
		if (exitTime == null) {
			if (other.exitTime != null)
				return false;
		} else if (!exitTime.equals(other.exitTime))
			return false;
		if (isCasual != other.isCasual)
			return false;
		if (numberOfSubscribers != other.numberOfSubscribers)
			return false;
		if (numberOfVisitors != other.numberOfVisitors)
			return false;
		if (parkID == null) {
			if (other.parkID != null)
				return false;
		} else if (!parkID.equals(other.parkID))
			return false;
		if (personID == null) {
			if (other.personID != null)
				return false;
		} else if (!personID.equals(other.personID))
			return false;
		if (Float.floatToIntBits(priceOfEntry) != Float.floatToIntBits(other.priceOfEntry))
			return false;
		if (Float.floatToIntBits(priceOfOrder) != Float.floatToIntBits(other.priceOfOrder))
			return false;
		return true;
	}

}
