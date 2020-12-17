package io;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mocks.CardReaderSystem;

public class CardReaderController extends Application {

	public CardReaderController() {
		
	}
	
	// ============================ main is only for demonstration ====================
	// in the real this window will be opened with the main window at the beginning of the program
    public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CardReaderSystem.class.getResource("CardReader.fxml"));
			Pane root = loader.load();
			Scene scene = new Scene(root);
			CardReaderSystem controller = loader.getController();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Visitors entry Page");
			primaryStage.show(); 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	// ============================ main is only for demonstration ====================
	
	
	public void enterVisitor(String id) { // method that checks if visitor allowed to enter to the park
		//TODO checks if visitor allowed to enter to the park (Roman)
	}
	
	public void exitVisitor(String id) { // visitor exits the park
		//TODO the visitor exits the park update DB (Roman)
	}
	

}
