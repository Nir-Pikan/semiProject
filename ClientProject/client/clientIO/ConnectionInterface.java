package clientIO;

import modules.ServerRequest;

public interface ConnectionInterface {

	/**
	 * when request is sent the client will wait for response <br>
	 * and the response will be returned
	 * 
	 * @param request the {@link ServerRequest} to be sent to server
	 * @return the response message
	 */
	String sendRequestAndResponse(ServerRequest request);

}