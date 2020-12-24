package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import module.GuiController;

/** the Login page controller */
public class LoginController implements GuiController {

	@FXML
	private GridPane visitorLoginForm;

	@FXML
	private Button btnUserLogin;

	@FXML
	private TextField txtId;

	@FXML
	private GridPane workerLoginForm;

	@FXML
	private Button btnWorkerLogin;

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private CheckBox cbUserWorker;

	@FXML
	void UserLogin(ActionEvent event) {

	}

	/** when clicking on the user/worker check box hide/reveal wanted fields */
	@FXML
	void UserWorkerCheckBox(ActionEvent event) {
		if (cbUserWorker.isSelected()) {
			visitorLoginForm.setManaged(false);
			visitorLoginForm.setVisible(false);
			workerLoginForm.setManaged(true);
			workerLoginForm.setVisible(true);
		} else {
			visitorLoginForm.setManaged(true);
			visitorLoginForm.setVisible(true);
			workerLoginForm.setManaged(false);
			workerLoginForm.setVisible(false);
		}
	}

	@FXML
	void WorkerLogin(ActionEvent event) {

	}

	public void init() {
		workerLoginForm.setManaged(false);
		workerLoginForm.setVisible(false);

	}

}
