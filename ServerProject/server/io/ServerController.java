package io;

import java.io.IOException;
import java.sql.SQLException;

import entities.Permission;
import entities.Permissions;
import entities.Worker;
import gui.ServerGuiController;
import logic.DiscountController;
import logic.EntryController;
import logic.MessageController;
import logic.OrderController;
import logic.ParkController;
import logic.ReportController;
import logic.SubscriberController;
import logic.WaitingListController;
import logic.WorkerController;
import modules.ServerRequest;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ServerController extends AbstractServer {

	public static int DEFAULT_SERVER_PORT = 5555;

	// controllers****************************************

	// private VisitController visit;
	private SubscriberController subscriber;
	private WorkerController worker;
	private MessageController messageC;
	private DiscountController discount;
	private ParkController park;
	private OrderController order;
	private EntryController entry;
	private WaitingListController waitingList;
	private ReportController report;

	/** controller to show user connection */
	private static ServerGuiController gui;
	// end Of controllers*********************************

	/**
	 * start the server ,creates all of the controllers with their dependencies
	 * 
	 * @throws SQLException
	 */
	private ServerController(int port) throws SQLException {
		super(port);
		initControllers();
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ServerController startServer(int port, ServerGuiController gui) throws SQLException {
		ServerController.gui = gui;
		return new ServerController(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String parsed = (String) msg;
		String response = null;
		ServerRequest sr = ServerRequest.fromJson(parsed);
		switch (sr.manager) {
		case Discount:
			response = discount.handleRequest(sr);
			break;
		case Entry:
			response = entry.handleRequest(sr);
			break;
		case Order:
			response = order.handleRequest(sr);
			break;
		case Park:
			response = park.handleRequest(sr);
			break;
		case Report:
			response = report.handleRequest(sr);
			break;
		case Subscriber:
			response = subscriber.handleRequest(sr);
			break;
		case WaitingList:
			response = waitingList.handleRequest(sr);
			break;
		case Worker:
			// On login or logout change the data stored in the server
			if (sr.job.equals("LogOutWorker")) {
				client.setInfo("workerUsername", null);
			} else if (sr.job.equals("LogInWorker")) {
				client.setInfo("workerUsername", ServerRequest.gson.fromJson(sr.data, String[].class)[0]);
			}
			response = worker.handleRequest(sr);
			break;
		default:
			response = "Unsuported";
			break;
		}
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** called when client connects */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("new client connected: " + client);
		// TODO prototype only code
		gui.setConnected(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName());
		// wait for the client to die and call clientDisconnected
		new Thread(() -> {
			while (client.isAlive()) {
				try {
					client.join();
				} catch (InterruptedException e) {
				}
			}
			clientDisconnected(client);
		}).start();

	}

	/** called when client disconnects */
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		super.clientDisconnected(client);
		System.out.println("client disconnected: " + client);
		// Disconnect client
		Object obj = client.getInfo("workerUsername");
		if (obj != null && obj instanceof String)
			worker.updateWorkerLogginDB((String) obj, false);
		// TODO prototype only code
		gui.setDisconnected();
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * creates all of the controllers with their dependencies
	 * 
	 * @throws SQLException
	 */
	private void initControllers() throws SQLException {
		DbController.init();
		//
		gui.dbConnected();
		park = new ParkController();
		subscriber = new SubscriberController();
		worker = new WorkerController();
		messageC = new MessageController();
		discount = new DiscountController();
		//
		order = new OrderController(park, messageC, subscriber, discount);
		entry = new EntryController(park, messageC, subscriber, discount);
		//
		waitingList = new WaitingListController(order, messageC);
		report = new ReportController(order, entry, park);

// ================ Create Permissions for workers
// =================================================================
		Permissions depManager = new Permissions("department");
		Permissions parkSilverManager = new Permissions("Silver");
		Permissions parkSilverWorker = new Permissions("Silver");
		Permissions parkGoldManager = new Permissions("Gold");
		Permissions parkGoldWorker = new Permissions("Gold");
		Permissions parkPlatinumManager = new Permissions("Platinum");
		Permissions parkPlatinumWorker = new Permissions("Platinum");

		Permission reg = new Permission("Registration");
		Permission visitorsView = new Permission("VistitorsView");
		Permission reportExport = new Permission("ReportExport");
		Permission editParameters = new Permission("EditParameters");

		depManager.AddPermission(reg);
		depManager.AddPermission(visitorsView);
		depManager.AddPermission(reportExport);
//		depManager.AddPermission(editParameters);
		depManager.AddPermission(new Permission("ApproveParameters"));
		depManager.AddPermission(new Permission("ApproveDiscounts"));

		parkSilverManager.AddPermission(reg);
		parkSilverManager.AddPermission(visitorsView);
		parkSilverManager.AddPermission(reportExport);
		parkSilverManager.AddPermission(editParameters);

		parkGoldManager.AddPermission(reg);
		parkGoldManager.AddPermission(visitorsView);
		parkGoldManager.AddPermission(reportExport);
		parkGoldManager.AddPermission(editParameters);

		parkPlatinumManager.AddPermission(reg);
		parkPlatinumManager.AddPermission(visitorsView);
		parkPlatinumManager.AddPermission(reportExport);
		parkPlatinumManager.AddPermission(editParameters);

		parkSilverWorker.AddPermission(reg);
		parkSilverWorker.AddPermission(visitorsView);

		parkGoldWorker.AddPermission(reg);
		parkGoldWorker.AddPermission(visitorsView);

		parkPlatinumWorker.AddPermission(reg);
		parkPlatinumWorker.AddPermission(visitorsView);
// =================================================================================================================

// ================ Create Workers for DB ==========================================================================
		Worker departmentManager = new Worker("depManager", "Nir", "Pikan", "000000001", "nir@gmail.com",
				"departmentManager", "Aa123456", false, depManager);
		Worker silverManager = new Worker("silverManager", "Roman", "Kozak", "000000002", "roman@gmail.com",
				"parkManager", "Aa123456", false, parkSilverManager);
		Worker goldManager = new Worker("goldManager", "Or", "Man", "000000003", "or@gmail.com", "parkManager",
				"Aa123456", false, parkGoldManager);
		Worker platinumManager = new Worker("platinumManager", "Michael", "Gindin", "000000004", "michael@gmail.com",
				"parkManager", "Aa123456", false, parkPlatinumManager);
		Worker silverWorker = new Worker("silverWorker", "Aviv", "Vanunu", "000000005", "aviv@gmail.com", "parkWorker",
				"Aa123456", false, parkSilverWorker);
		Worker goldWorker = new Worker("goldWorker", "Giorno", "Giovanna", "000000006", "giorno@gmail.com",
				"parkWorker", "Aa123456", false, parkGoldWorker);
		Worker platinumWorker = new Worker("platinumWorker", "Jotaro", "Kujo", "000000007", "jotaro@gmail.com",
				"parkWorker", "Aa123456", false, parkPlatinumWorker);

		worker.AddWorker(departmentManager);
		worker.AddWorker(silverManager);
		worker.AddWorker(goldManager);
		worker.AddWorker(platinumManager);
		worker.AddWorker(silverWorker);
		worker.AddWorker(goldWorker);
		worker.AddWorker(platinumWorker);
// =================================================================================================================
	}

	@Override
	protected void serverClosed() {
		System.out.println("Server has stopped");
	}

}
