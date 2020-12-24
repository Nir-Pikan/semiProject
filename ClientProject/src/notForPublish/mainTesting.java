package notForPublish;

import gui.LoginController;
import gui.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import module.GuiController;
import module.Navigator;
import module.PopUp;

public class mainTesting extends Application{

	//TODO delete
	//Only for UI testing
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainScreenController.class.getResource("mainScreen.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			MainScreenController cont = loader.getController();
			primaryStage.setScene(scene);
			Pane p =cont.init();
			primaryStage.show(); 
			root.getStylesheets().add(LoginController.class.getResource("style.css").toString());
			Navigator.init(p);
			Navigator n = Navigator.instance();
//			n.navigate(null);
//			GuiController g = n.navigate("RegularOrder");
//			GuiController g = n.navigate("SmallGroupOrder");
//			GuiController g = n.navigate("GroupOrder");
//			GuiController g = n.navigate("OrderDetails");
			GuiController g = n.navigate("RegisterCommonDetails");
//			GuiController g = n.navigate("RegisterAddCreditCard");
//			GuiController g = n.navigate("RegisterSummary");
//			GuiController g = n.navigate("VisitorsInThePark");
//			GuiController g = n.navigate("NewTimeSelection");
//			GuiController g = n.navigate("ReportExport");
//			PopUp.showCostumContent("visitor report", "VisitorsReport.fxml");
//			PopUp.showCostumContent("usage report", "UsageReport.fxml");
//			PopUp.showCostumContent("income report", "IncomeReport.fxml");
//			PopUp.showCostumContent("entries report", "EntryReport.fxml");
//			PopUp.showCostumContent("cancels report", "CancelReport.fxml");
//			GuiController g = n.navigate("ParkManagerParametersUpdate");
//			GuiController g = n.navigate("ParkManagerChangesRequest");
//			GuiController g = n.navigate("AddDiscount");
//			GuiController g = n.navigate("DevisionManagerParksDetailsApprove.fxml");
//			GuiController g = n.navigate("DevisionManagerDiscount");
//			System.out.println(PopUp.ask("waiting list", "The date and time you choose is taken", "Do you want to enter the waiting list?"));
			PopUp.showInformation("", "", "1\n2\n3\n2\n3\n");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
