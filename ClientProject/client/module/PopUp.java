package module;

import java.io.IOException;
import java.net.URL;

import gui.LoginController;
import gui.MainScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/** class for popups with our css */
public class PopUp extends Alert {

	/**
	 * create default Alert with styling
	 * 
	 * @param type the type of the Alert
	 */
	public PopUp(AlertType type) {
		super(type);
		init();
	}

	/**
	 * create default Alert with styling
	 * 
	 * @param type     the type of the Alert
	 * @param content  the content of the popup
	 * @param BtnTypes Buttons to show
	 */
	public PopUp(AlertType type, String content, ButtonType... BtnTypes) {
		super(type, content, BtnTypes);
		init();

	}

	/**
	 * create Alert with styling and custom content(Pane)
	 * 
	 * @param title   the title of the window
	 * @param content the content of the popup(javafx Pane)
	 */
	public PopUp(String title, Pane content) {
		super(AlertType.INFORMATION);
		init();
		this.getDialogPane().setContent(content);
	}

	/** set the styles to the popup */
	private void init() {
		this.getDialogPane().getStylesheets().add(LoginController.class.getResource("style.css").toString());
		((Stage) this.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UNDECORATED);
		((Stage) this.getDialogPane().getScene().getWindow()).setOnCloseRequest((event) -> {
			event.consume();
		});
		this.setResizable(true);

	}

	/**
	 * show an Alert with 'yes' and 'no' options. waits until the user answer, and
	 * return the answer
	 * 
	 * @param title  the title of the window(not shown in the head)
	 * @param header the text to show in the upper part of the alert
	 * @param body   the text to show in the lower part of the alert
	 * @return the response to the question
	 */
	public static boolean ask(String title, String header, String body) {
		Alert alert = new PopUp(AlertType.CONFIRMATION, body, ButtonType.YES, ButtonType.NO);
		alert.setTitle(title);
		alert.setHeaderText(header);
		ButtonBar buttonBar = (ButtonBar) alert.getDialogPane().lookup(".button-bar");
		buttonBar.setButtonOrder("N_L+Y_R");
		buttonBar.getButtons().forEach(b -> {
			Button bt = (Button) b;
			switch (bt.getText()) {
			case "Yes":
				bt.getStyleClass().add("btn_yes");
				break;
			case "No":
				bt.getStyleClass().add("btn_no");
				break;
			}
		});
		ButtonType returnVal = alert.showAndWait().get();
		if (returnVal == ButtonType.YES) {
			return true;
		}
		return false;
	}

	/**
	 * show an Alert with 'OK' option. not waiting until the user answer
	 * 
	 * @param title  the title of the window(not shown in the head)
	 * @param header the text to show in the upper part of the alert
	 * @param body   the text to show in the lower part of the alert
	 */
	public static void showInformation(String title, String header, String body) {
		Alert alert = new PopUp(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);
		alert.show();

	}

	/**
	 * show an Alert with 'OK' option and error logo. not waiting until the user
	 * answer
	 * 
	 * @param title  the title of the window(not shown in the head)
	 * @param header the text to show in the upper part of the alert
	 * @param body   the text to show in the lower part of the alert
	 */
	public static void showError(String title, String header, String body) {
		Alert alert = new PopUp(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);
		alert.show();

	}

	/**

	 * create Alert with styling and custom content(Pane) but with file name
	 * 
	 * @param title       the title of the window
	 * @param contentFxml the fxml file name of the content(javafx Pane)
	 * @return GuiController of the loaded screen
	 */
	public static GuiController showCostumContent(String title, String contentFxml) {
		URL url = MainScreenController.class.getResource(contentFxml);
		if (url == null)
			return null;
		FXMLLoader loader = new FXMLLoader(url);
		try {
			Alert a = new Alert(AlertType.NONE, "", ButtonType.CLOSE);
			a.getDialogPane().getStylesheets().add(LoginController.class.getResource("style.css").toString());
			a.setTitle(title);
			a.getDialogPane().setContent(loader.load());
			a.show();
			return loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * show an Alert with 'OK' option and textField. wait for answer, and return the
	 * typed Text
	 * 
	 * @param title  the title of the window(not shown in the head)
	 * @param header the text to show in the upper part of the alert
	 * @param body   the text to show in the lower part of the alert(next to the
	 *               text field)
	 * @return the text inputes by the user
	 */
	public static String getUserInput(String title, String header, String body) {
		Alert alert = new PopUp(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		TextField tf = new TextField();
		alert.getDialogPane().setContent(new HBox(10, new Label(body), tf));
		alert.showAndWait();
		return tf.getText();
	}
}
