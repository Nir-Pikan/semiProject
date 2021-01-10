package clientMain;

import java.io.IOException;

import clientGui.LoginController;
import clientGui.MainScreenController;
import clientGui.Navigator;
import clientIO.UserInterface;
import clientIO.clientController;
import clientMocks.CardReaderMock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainClient extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		try 
		{
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
			root.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
			Navigator.init(p);
			Navigator.instance();
			new CardReaderMock(new Stage());//TODO put in comment
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
