package io;

import java.io.IOException;

import modules.ServerRequest;
import ocsf.client.AbstractClient;

public class clientController extends AbstractClient {

	
	public static int DEFAULT_SERVER_PORT = 5555;
	public static String DEFAULT_SERVER_HOST = "localhost";
	
	
	private static boolean awaitResponse = false;
	public static String response = "";
	public static clientController client = null;
	
	public clientController(String host, int port) throws IOException {
		super(host, port);
		
			this.openConnection();
			client = this;
		
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("message received");
		response = (String)msg;
		awaitResponse = false;
	}
	
	/**when request sent the client will wait for response and the respone will be in the public static String result*/
	public void sendRequest(ServerRequest request) {
		try
	    {
	    	openConnection();//in order to send more than one message
	       	awaitResponse = true;
	    	sendToServer(ServerRequest.toJson(request));
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	      System.out.println("Could not send message to server: Terminating client."+ e);
	    }
	}

	/**get respone from server, reset to \"\" in the end
	 * @return the response from the server*/
	public static String consumeResponse() {
		String msg = response;
		response = "";
		return msg;
	}
}
