package entities;

/** a class for park manager value change requests */
public class PendingValueChangeRequest {

	public String parkId;
	public ParkAttribute attName;
	public double reuestedValue;
	public double currentValue;

	/**
	 * Creates a {@link PendingValueChangeRequest}
	 * 
	 * @param parkId        String
	 * @param attName       ParkAttribute{MaxCapacity,MaxPreOrder,AvgVisitTime}
	 * @param reuestedValue double
	 * @param currentValue  double
	 */
	public PendingValueChangeRequest(String parkId, ParkAttribute attName, double reuestedValue, double currentValue) {
		this.parkId = parkId;
		this.attName = attName;
		this.reuestedValue = reuestedValue;
		this.currentValue = currentValue;
	}

	public String getParkId() {
		return parkId;
	}

	public ParkAttribute getAttName() {
		return attName;
	}

	public double getReuestedValue() {
		return reuestedValue;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public static enum ParkAttribute {
		MaxCapacity, MaxPreOrder, AvgVisitTime
	}
}
