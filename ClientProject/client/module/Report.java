package module;

import java.sql.Timestamp;

public interface Report {
	public void initReport(String parkName, String parkID, Timestamp[] reportDate);
	
}
