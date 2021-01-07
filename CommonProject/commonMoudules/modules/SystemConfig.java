package modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Class for configuration only for the presentation, if the configuration file not exist use defaults 
 */
public class SystemConfig {
	public int[] SendNotificationSendTime;
	public int[] SendNotificationCancelTime;
	public int[] RemoveFromWatingListTime;
	public long WaitingListTimer;

	/**
	 * configuration file name
	 */
	private static String ConfPath = "globals.config";
	
	/**
	 * system configuration, calculated from the {@link #get()} method
	 */
	public static SystemConfig configuration = get();

	private SystemConfig() {
	}

	/**
	 * get configuration for the system
	 * @return if configuration file {@link #ConfPath} exists return it else return  {@link #getDefault()}
	 */
	private static SystemConfig get() {
		File c = new File(ConfPath);
		if (!c.exists()) {
			return getDefault();
		}
		try {
			return ServerRequest.gson.fromJson(new InputStreamReader(new FileInputStream(ConfPath), "UTF-8"),
					SystemConfig.class);
		} catch (Exception e) {
			return getDefault();
		}
	}
	
/**
 * defaults of the configuration, when no file exists this is the values
 * @return {@link SystemConfig} defaults
 */
	private static SystemConfig getDefault() {
		SystemConfig ret = new SystemConfig();
		ret.SendNotificationSendTime = new int[] { 10, 00 };
		ret.SendNotificationCancelTime = new int[] { 12, 00 };
		ret.RemoveFromWatingListTime = new int[] { 23, 00 };
		ret.WaitingListTimer = 3600;//seconds
		return ret;

	}

}
