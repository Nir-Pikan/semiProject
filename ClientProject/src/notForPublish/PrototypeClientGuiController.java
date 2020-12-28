package notForPublish;

import java.io.IOException;

import entities.Visitor;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import modules.ServerRequest;

public class PrototypeClientGuiController {

	@FXML
    private TextField id;

    @FXML
    private TextField email;

    @FXML
    private TextField sName;

    @FXML
    private TextField fName;

    @FXML
    private TextField phone;
    
	@FXML
	void UpdateVisitor(ActionEvent event) {
		String strId = id.getText();
		String newEmail = email.getText();
		//ServerRequest sr = new ServerRequest(Manager.Visitors, "changeVisitorEmail", strId + " " +newEmail);
		//clientController.client.sendRequest(sr);
		String respons = clientController.consumeResponse();
		if (respons.equals("error")) {
			System.out.println("error in changing Email");
			return;
		}
	}

	@FXML
	void exit(ActionEvent event) {
try {                    
	clientController.client.closeConnection();
	System.exit(0);
} catch (IOException e) {
	e.printStackTrace();
}

	}

	@FXML
	void getVisitor(ActionEvent event) {
		String strId = id.getText();
		//ServerRequest sr = new ServerRequest(Manager.Visitors, "getVisitor", strId);
		//sclientController.client.sendRequest(sr);
		String respons = clientController.consumeResponse();
		if (respons.equals("not Found")|| respons.equals("")) {
			fName.setText("not Found");
			sName.setText("");
			email.setText("");
			phone.setText("");
			return;
		}
		Visitor v = ServerRequest.gson.fromJson(respons, Visitor.class);
		fName.setText(v.firstName);
		sName.setText(v.surname);
		email.setText(v.email);
		phone.setText(v.phone);
	}

}
