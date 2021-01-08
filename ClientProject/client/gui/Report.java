package gui;

import java.sql.Timestamp;

/** interface for all reports */
public interface Report {
	public void initReport(String parkName, String parkID, Timestamp[] reportDate);

}
