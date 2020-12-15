package entities;

/**class for park manager value change requests*/
public class PendingValueChangeRequest {

	public String parkId;
	public ParkAttribute attName;
	public double reuestedValue;
	public double currentValue;
	
	/**
	 * @param parkId
	 * @param attName
	 * @param reuestedValue
	 * @param currentValue
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


	public static enum ParkAttribute{
		MaxCapacity,MaxPreOrder,AvgVisitTime
	}
}
