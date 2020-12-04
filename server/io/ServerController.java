package io;


import java.io.IOException;
import java.sql.SQLException;

import gui.ServerGuiController;
import logic.VisitController;
import modules.ServerRequest;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ServerController extends AbstractServer{

	public static int DEFAULT_SERVER_PORT = 5555;
	
	//controllers****************************************
	
	private DbController db;//TODO check if needed here
	private VisitController visit;
	
	
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
				case Visitors:
					response = visit.handelRequest(sr);
					break;
				default:
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
		gui.dbConnected();
		visit = new VisitController();
	}

	@Override
	protected void serverClosed() {
		System.out.println
	      ("Server has stopped");
	}
	
	
	
}
