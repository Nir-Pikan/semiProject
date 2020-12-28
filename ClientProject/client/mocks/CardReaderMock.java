package mocks;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CardReaderMock  {

	public CardReaderMock(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CardReaderSystem.class.getResource("CardReaderSystem.fxml"));
			Pane root = loader.load();
			Scene scene = new Scene(root);
			CardReaderSystem controller = loader.getController();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Visitors entry Page");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ============================ main is only for demonstration
		// ====================
		// in the real this window will be opened with the main window at the beginning
		// of the program
		
	}
