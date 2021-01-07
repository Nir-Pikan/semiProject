package modules;

public interface IController {

	/**
	 * Handle request for this controller
	 * 
	 * @param request
	 * @return the response to the client
	 */
	public String handleRequest(ServerRequest request);
}
