package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**class for popups with our css*/
public class PopUp extends Alert {

	/**
	 * create default Alert with styling
	 * @param type the type of the Alert
	 */
	public PopUp(AlertType type) {
		super(type);
		init();
	}

	/**
	 * create default Alert with styling
	 * @param type the type of the Alert
	 * @param content the content of the popup
	 * @param BtnTypes Buttons to show
	 */
	public PopUp(AlertType type, String content, ButtonType... BtnTypes) {
		super(type, content, BtnTypes);
		init();
		
	}

	/**set the styles to the popup*/
	private void init() {
		this.getDialogPane().getStylesheets().add(this.getClass().getResource("style.css").toString());
		((Stage) this.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UNDECORATED);
		((Stage) this.getDialogPane().getScene().getWindow()).setOnCloseRequest((event)->{
			event.consume();
		});
	}
	
	public static boolean ask(String title,String header,String body) {
		Alert alert = new PopUp(AlertType.CONFIRMATION,body,ButtonType.YES,ButtonType.NO);
		alert.setTitle(title);
		alert.setHeaderText(header);
		ButtonType returnVal = alert.showAndWait().get();
		if(returnVal == ButtonType.YES) {
			return true;
		}return false;
	}
}
