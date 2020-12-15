package entities;

public class ParkNameAndTimes {

	public String parkName;
	public String parkID;
	public int openTime;
	public int endTime;
	
	/**create Entry for Parks
	 * @param parkName
	 * @param parkID
	 * @param openTime
	 * @param endTime
	 */
	public ParkNameAndTimes(String parkName, String parkID, int openTime, int endTime) {
		this.parkName = parkName;
		this.parkID = parkID;
		this.openTime = openTime;
		this.endTime = endTime;
	}
	
	
}
