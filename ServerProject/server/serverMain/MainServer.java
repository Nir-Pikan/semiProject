package serverMain;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import serverGui.ServerGuiController;

/**
 * main class to run the server
 */
public class MainServer extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ServerGuiController.class.getResource("baseGui.fxml"));
			GridPane root = loader.load();
			Scene scene = new Scene(root);
			ServerGuiController controller = loader.getController();
			primaryStage.setScene(scene);
			primaryStage.setTitle("server");
			primaryStage.show();

			// prevent window close

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					if (ServerGuiController.isServerRunning == true) {// if the server is running do not close window
						event.consume();
					} else {
						System.exit(0);
					}

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
