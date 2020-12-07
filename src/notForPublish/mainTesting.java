package notForPublish;

import gui.GuiController;
import gui.LoginController;
import gui.MainScreenController;
import gui.Navigator;
import gui.PopUp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class mainTesting extends Application{

	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainScreenController.class.getResource("mainScreen.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			MainScreenController cont = loader.getController();
			primaryStage.setScene(scene);
			Pane p =cont.InitMockup(3);
			primaryStage.show(); 
			root.getStylesheets().add(LoginController.class.getResource("style.css").toString());
			Navigator.init(p);
			Navigator n = Navigator.instance();
			GuiController g = n.navigate("login");
			//System.out.println(PopUp.ask("waiting list", "The date and time you choose is taken", "Do you want to enter the waiting list?"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
