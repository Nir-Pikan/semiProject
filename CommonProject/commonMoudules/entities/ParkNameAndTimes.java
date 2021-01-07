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
	public int closeTime;

	/**
	 * Creates a {@link ParkNameAndTimes}
	 * 
	 * @param parkName  the park's name
	 * @param parkID    the park's ID
	 * @param openTime  the park's open time
	 * @param closeTime the park's closing time
	 */
	public ParkNameAndTimes(String parkName, String parkID, int openTime, int closeTime) {
		this.parkName = parkName;
		this.parkID = parkID;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

}
