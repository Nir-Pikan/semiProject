package clientIO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import entities.ParkNameAndTimes;
import entities.Subscriber;
import entities.Worker;
import javafx.application.Platform;
import modules.Property;
import modules.ServerRequest;
import modules.ServerRequest.Manager;
import ocsf.client.AbstractClient;
import util.EventNotifier;

/** a class representing the client controller */
public class clientController extends AbstractClient {

	/** Default connection Port Number */
	public static int DEFAULT_SERVER_PORT = 5555;
	/** Default connection Server Host */
	public static String DEFAULT_SERVER_HOST = "localhost";

	// connection response parameters
	private static boolean awaitResponse = false;
	private static String response = "";

	// instance of the connection for the client
	public static clientController client = null;
	private static Thread con;

	// common data
	public Map<String, ParkNameAndTimes> openingTimes;
	public String[] parkNames;
	public Property<Worker> logedInWorker = new Property<>();
	public Property<Subscriber> logedInSubscriber = new Property<>();
	public Property<String> visitorID = new Property<>();
	public EventNotifier observable = new EventNotifier();

	/**
	 * creates a {@link clientController}
	 * 
	 * @param host the server's host name.
	 * @param port the port number.
	 */
	public clientController(String host, int port) throws IOException {
		super(host, port);

		this.openConnection();
		client = this;

		// save the names and opening hours for all of the parks

		String response = sendRequestAndResponse(new ServerRequest(Manager.Park, "get all parks data", ""));
		ParkNameAndTimes[] tmp = ServerRequest.gson.fromJson(response, ParkNameAndTimes[].class);
		openingTimes = new HashMap<String, ParkNameAndTimes>();
		for (ParkNameAndTimes p : tmp)
			openingTimes.put(p.parkID, p);
		parkNames = new String[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			parkNames[i] = tmp[i].parkID;// ParkID for key
		}
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		response = (String) msg;
		System.out.println("message received : " + response);
		awaitResponse = false;
	}

	/**
	 * when request is sent the client will wait for response <br>
	 * and the response will be returned
	 * 
	 * @param request the {@link ServerRequest} to be sent to server
	 * @return the response message
	 */
	public String sendRequestAndResponse(ServerRequest request) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(ServerRequest.toJson(request));
			// wait for response
			while (awaitResponse && this.isConnected()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not send message to server: Terminating client." + e);
		}
		String msg = response;
		response = "";
		return msg;
	}

	/**
	 * when connection closed, logOut
	 */
	@Override
	protected void connectionEstablished() {
		con = Thread.currentThread();
		new Thread(() -> {
			while (con.isAlive()) {
				try {
					con.join();
				} catch (InterruptedException e) {
				}
			}
			Platform.runLater(() -> {
				observable.emit(SERVER_CLOSED);
			});
		}).start();
	}

	/**
	*Event identifier for server close
	*/
	public static final String SERVER_CLOSED = "_SERVER_CLOSED";

}
