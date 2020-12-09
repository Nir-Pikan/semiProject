package main;

import java.io.IOException;

import gui.LoginController;
import gui.MainScreenController;
import io.clientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
			Pane p =cont.InitMockup(1);
			primaryStage.show(); 
			root.getStylesheets().add(LoginController.class.getResource("style.css").toString());
			Navigator.init(p);
			Navigator n = Navigator.instance();
			//GuiController g = n.navigate("login");
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
