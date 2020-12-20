package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import module.Navigator;

/** the MainScreen page controller */
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

	/** when clicking on the back button load previous page */
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
		case 2:// Visitor
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr[] = { "new Single Order", "new Private Group Order", "check Existing Order" };
			for (String s : arr) {
				menu.getChildren().add(new Button(s));
			}
			break;
		case 3:// subscriber
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr2[] = { "new Single Order", "new Private Group Order", "new Group Order",
					"check Existing Order" };
			for (String s : arr2) {
				menu.getChildren().add(new Button(s));
			}
			break;
		case 4:// worker
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr3[] = { "Subscriber registration", "check current visitors number" };
			for (String s : arr3) {
				menu.getChildren().add(new Button(s));
			}
			break;
		case 5:// parkManager
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr4[] = { "check current visitors number", "Generate Reports", "change Park Parameters" };
			for (String s : arr4) {
				menu.getChildren().add(new Button(s));
			}
			break;

		case 6:// devisionManager
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr5[] = { "check current visitors number", "Generate Reports", "Approve Park Parameters",
					"Approve discounts" };
			for (String s : arr5) {
				menu.getChildren().add(new Button(s));
			}
			break;
		}
		return body;

	}
}
