package main;

import java.io.IOException;

import entities.Subscriber;
import entities.Subscriber.Type;
import gui.LoginController;
import gui.MainScreenController;
import gui.ParkManagerParametersUpdateController;
import io.clientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import module.GuiController;
import module.Navigator;

public class MainClient extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainScreenController.class.getResource("mainScreen.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			MainScreenController cont = loader.getController();
			primaryStage.setScene(scene);
			Pane p =cont.init();
			primaryStage.show(); 
			primaryStage.setOnCloseRequest((event)->{
				try {
					clientController.client.closeConnection();
				} catch (IOException e) {}
				System.exit(0);
				
				});
			root.getStylesheets().add(LoginController.class.getResource("style.css").toString());
			Navigator.init(p);
			Navigator n = Navigator.instance();
			GuiController g = n.navigate("RegularOrder");
			//((ParkManagerParametersUpdateController) g).setPark("1");
		} catch(Exception e) {
			e.printStackTrace();
		}
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
