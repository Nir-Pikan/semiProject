package modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**class for communication between client and server*/
public class ServerRequest {

	//Serialize
	public static Gson gson = getGson();
	
	
	public Manager manager;
	public String job;
	public String data;
	
	
	public ServerRequest(Manager manager,String job,String msg) {
		this.manager=manager;
		this.job = job;
		this.data = msg;
	}
	
	public enum Manager{
		Visitors
	}
	
	/**serializer for objects*/
	private static Gson getGson() {
		GsonBuilder gb = new GsonBuilder().setPrettyPrinting().serializeNulls();
		return gb.create();
	}
	
	public static ServerRequest fromJson(String data) {
		return gson.fromJson(data, ServerRequest.class);
	}
	
	public static String toJson(ServerRequest data) {
		return gson.toJson(data, ServerRequest.class);
	}
}
