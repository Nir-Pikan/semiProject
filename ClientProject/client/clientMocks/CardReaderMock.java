package clientMocks;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/** the gui class for the {@link CardReaderSystem} */
public class CardReaderMock {

	/** start the {@link CardReaderMock} */
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

}
