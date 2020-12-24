package entities;

/** a class for park manager value change requests */
public class PendingValueChangeRequest {

	public String parkId;
	public ParkAttribute attName;
	public double requestedValue;
	public double currentValue;

	/**
	 * Creates a {@link PendingValueChangeRequest}
	 * 
	 * @param parkId         the park ID to be updated
	 * @param attName        the Attribute to be changed
	 * @param requestedValue the new attribute value
	 * @param currentValue   the old attribute value
	 */
	public PendingValueChangeRequest(String parkId, ParkAttribute attName, double requestedValue, double currentValue) {
		this.parkId = parkId;
		this.attName = attName;
		this.requestedValue = requestedValue;
		this.currentValue = currentValue;
	}

	public String getParkId() {
		return parkId;
	}

	public ParkAttribute getAttName() {
		return attName;
	}

	public double getReuestedValue() {
		return requestedValue;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public static enum ParkAttribute {
		MaxCapacity, MaxPreOrder, AvgVisitTime
	}
}
