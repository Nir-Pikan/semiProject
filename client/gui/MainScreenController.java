package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainScreenController {

	@FXML
	private Button btnReturn;

	@FXML
	private VBox menu;

	@FXML
	private Pane body;

	@FXML
	private Label greetingMsg;

	@FXML
	private Button Login;

	@FXML
	void back(ActionEvent event) {
		Navigator.instance().back();
	}

	@FXML
	void logInOut(ActionEvent event) {

	}

	// TODO remove this
	public Pane InitMockup(int caseNum) {
		switch (caseNum) {
		case 1:
			greetingMsg.setText("Hello guest");
			Login.setVisible(false);
			Login.setManaged(false);
			break;
		case 2:// subscriber
			greetingMsg.setText("Hello Jhon Doe");
			Login.setText("LogOut");
			break;

		case 3:// worker
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr[] = { "Subscriber registration", "check current visitors number" };
			for (String s : arr) {
				menu.getChildren().add(new Button(s));
			}
			break;
		}
		return body;

	}
}
