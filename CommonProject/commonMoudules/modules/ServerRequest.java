package modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** class for communication between client and server */
public class ServerRequest {

	// Serialize
	public static Gson gson = getGson();

	public Manager manager;
	public String job;
	public String data;

	/**
	 * Creates a {@link ServerRequest}
	 * 
	 * @param manager the controller type to handle the request.
	 * @param job     the task needed to be done by the controller.
	 * @param msg     the data needed for the job.
	 */
	public ServerRequest(Manager manager, String job, String msg) {
		this.manager = manager;
		this.job = job;
		this.data = msg;

	}

	public enum Manager {
		Subscriber, Worker, Discount, Park, Order, Entry, WaitingList, Report
	}

	/** serializer for objects */
	private static Gson getGson() {
		GsonBuilder gb = new GsonBuilder().setPrettyPrinting().serializeNulls();
		return gb.create();
	}

	/**
	 * creates a {@link ServerRequest} from Json data String
	 * 
	 * @param data the Json data to be transformed
	 * @return the result {@link ServerRequest}
	 */
	public static ServerRequest fromJson(String data) {
		return gson.fromJson(data, ServerRequest.class);
	}

	/**
	 * creates a Json data String from a {@link ServerRequest}
	 * 
	 * @param data the {@link ServerRequest} to be transformed
	 * @return the result Json data String
	 */
	public static String toJson(ServerRequest data) {
		return gson.toJson(data, ServerRequest.class);
	}
}
