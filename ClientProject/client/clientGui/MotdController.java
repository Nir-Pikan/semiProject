package clientGui;

import clientIO.clientController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import modules.MovingMessage;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** a class for the moving message board on home page */
public class MotdController implements GuiController {

	@FXML
	private ScrollPane scroll;

	@Override
	public void init() {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Message, "getMotd", null));
		MovingMessage mm = new MovingMessage(scroll);
		mm.setMessages(ServerRequest.gson.fromJson(response, String[].class));
	}
}
