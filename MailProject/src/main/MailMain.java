package main;

import gui.EmailPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mail.MyMail;



public class MailMain extends Application  {
	
//	public static void main(String[] args) {
//		EmailPageController emailPageController = new EmailPageController();
//		emailPageController.SendEmailAndSMS("mirage164@gmail.com","0540000000", "This is a remainder about your visit in the park", "Visit Remainder");
//		
//
//	}
	
	
	MyMail mailService = new MyMail();

	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(EmailPageController.class.getResource("SendEmailPage.fxml"));
			Pane root = loader.load();
			Scene scene = new Scene(root);
			EmailPageController controller = loader.getController();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Send Mail Page");
			primaryStage.show(); 
			EmailPageController emailPageController = new EmailPageController();
			mailService.SendEmailAndSMS("mirage164@gmail.com","0540000000", "This is the mail content", "The subject");

			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
