package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mail.MyMail;

public class EmailPageController {

	String email;
	String massageContent;
	MyMail mailService = new MyMail();

//	public void SendEmailAndSMS(String destinationMail, String massageContent, String mailSubject) {
//		mailService.SendEmailAndSMSPopup(destinationMail, massageContent, mailSubject);
//	}

    @FXML
    private TextField emailAddressTextField;

    @FXML
    private TextArea mailContentTextArea;

    @FXML
    private Button SendButton;

    @FXML
    private Button CloseButton;
    
    @FXML
    private Label emailSendAcknowledgeLabel;

    @FXML
    void sendButtonClicked(ActionEvent event) {
    	//mailService.SendEmailAndSMS("mirage164@gmail.com","0545500000" ,"This is the mail content", "The subject");

    }
    
    @FXML
    void closeButtonClicked(ActionEvent event) {
    	 // get a handle to the stage
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    

}