package common;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox menu;

    @FXML
    private Pane body;

    @FXML
    private Label greetingMsg;

    @FXML
    private Button Login;
    
    @FXML
    void logInOut(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert menu != null : "fx:id=\"menu\" was not injected: check your FXML file 'mainScreen.fxml'.";
        assert body != null : "fx:id=\"body\" was not injected: check your FXML file 'mainScreen.fxml'.";
        assert greetingMsg != null : "fx:id=\"greetingMsg\" was not injected: check your FXML file 'mainScreen.fxml'.";
        assert Login != null : "fx:id=\"Login\" was not injected: check your FXML file 'mainScreen.fxml'.";
    }
    
    //TODO remove this
    public Pane InitMockup(int caseNum) {
    	switch(caseNum) {
    	case 1:
    		greetingMsg.setText("Hello guest");
        	break;
    	case 2://subscriber
    		greetingMsg.setText("Hello Jhon Doe");
    		Login.setText("LogOut");
        	break;
    		
    	case 3://worker
    		greetingMsg.setText("Hello Jane Doe");
    		Login.setText("LogOut");
    		String arr[] = {"Subscriber registration","check current visitors number"};
    		for(String s : arr) {
    			menu.getChildren().add(new Button(s));
    		}
        	break;
    	}
    	return body;
    	
    }
}
