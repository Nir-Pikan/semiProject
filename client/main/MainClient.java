package main;

import java.io.IOException;

import io.clientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainClient extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		/*try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PrototypeClientGuiController.class.getResource("PrototypeClient.fxml"));
			GridPane root = loader.load();
			Scene scene = new Scene(root);
			PrototypeClientGuiController controller = loader.getController();
			primaryStage.setScene(scene);
			primaryStage.setTitle("client");
			primaryStage.show(); 
		} catch(Exception e) {
			e.printStackTrace();
		}*/
	}

	public static void main(String[] args) {
		try {
			clientController cc = new clientController(clientController.DEFAULT_SERVER_HOST, clientController.DEFAULT_SERVER_PORT);
			launch(args);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}

}
