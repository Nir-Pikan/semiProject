package entities;

/**
 * a class containing some of a {@link Park}'s fields.
 * <p>
 * Purpose: avoid sharing the entire park's info
 */
public class ParkNameAndTimes {

	public String parkName;
	public String parkID;
	public int openTime;
	public int endTime;

	/**
	 * Creates a {@link ParkNameAndTimes}
	 * 
	 * @param parkName String
	 * @param parkID   String
	 * @param openTime int
	 * @param endTime  int
	 */
	public ParkNameAndTimes(String parkName, String parkID, int openTime, int endTime) {
		this.parkName = parkName;
		this.parkID = parkID;
		this.openTime = openTime;
		this.endTime = endTime;
	}

}
