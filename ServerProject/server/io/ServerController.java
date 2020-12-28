package io;


import java.io.IOException;
import java.sql.SQLException;

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

public class ServerController extends AbstractServer{

	public static int DEFAULT_SERVER_PORT = 5555;
	
	//controllers****************************************
	
	//private VisitController visit;
	private SubscriberController subscriber;
	private WorkerController worker;
	private MessageController messageC;
	private DiscountController discount;
	private ParkController park;
	private OrderController order;
	private EntryController entry;
	private WaitingListController waitingList;
	private ReportController report;
	
	
	/**controller to show user connection*/
	private static ServerGuiController gui;
	//end Of controllers*********************************
	
	
	/**start the server ,creates all of the controllers with their dependencies
	 * @throws SQLException */
	private ServerController(int port) throws SQLException {
		super(port);
		initControllers();
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ServerController startServer(int port,ServerGuiController gui) throws SQLException {
		ServerController.gui = gui;
		return new ServerController(port);
	}
	

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String parsed = (String)msg;
		String response = null;
				ServerRequest sr = ServerRequest.fromJson(parsed);
				switch(sr.manager) {
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
					//On login or logout change the data stored in the server
					if(sr.job.equals("LogOutWorker")) {
						client.setInfo("workerUsername", null);
					}else if(sr.job.equals("LogInWorker")) {
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
	
	/**called when client connects*/
	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("new client connected: "+client);
		//TODO prototype only code
		gui.setConnected(client.getInetAddress().getHostAddress(),client.getInetAddress().getHostName());
		//wait for the client to die and call clientDisconnected
		new Thread(()-> {
				while(client.isAlive()) {
					try {
						client.join();
					} catch (InterruptedException e) {}
				}
				clientDisconnected(client);
		}).start();
		
	}
	
	/**called when client disconnects*/
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		super.clientDisconnected(client);
		System.out.println("client disconnected: "+client);
		//Disconnect client
		Object obj = client.getInfo("workerUsername");
		if(obj!=null && obj instanceof String)
			worker.updateWorkerLogginDB((String)obj, false);
		//TODO prototype only code
		gui.setDisconnected();
	}
	
	
	/**
	   * This method overrides the one in the superclass.  Called
	   * when the server starts listening for connections.
	   */
	  protected void serverStarted()
	  {
	    System.out.println
	      ("Server listening for connections on port " + getPort());
	  }
	  
	  /**
	   * This method overrides the one in the superclass.  Called
	   * when the server stops listening for connections.
	   */
	  protected void serverStopped()
	  {
	    System.out.println
	      ("Server has stopped listening for connections.");
	  }
	  
	  
	/**creates all of the controllers with their dependencies
	 * @throws SQLException */
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
		order = new OrderController(park,messageC,subscriber,discount);
		entry = new EntryController(park,messageC,subscriber,discount);
		//
		waitingList = new WaitingListController(order,messageC);
		report = new ReportController(order,entry,park);
	}

	@Override
	protected void serverClosed() {
		System.out.println
	      ("Server has stopped");
	}
	
	
	
}
