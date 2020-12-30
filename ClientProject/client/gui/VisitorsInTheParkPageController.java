package gui;

import entities.Subscriber;
import entities.Subscriber.Type;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the VisitorsInTheParkPage page controller */
public class VisitorsInTheParkPageController implements GuiController {


	@FXML
	private ChoiceBox<String> parkNumChoise;

	@FXML
	private TextField visitorsAmountText;

	@FXML
	private Button closeButton;

	@FXML
	void familyOrder(ActionEvent event) {
		String ordererId = PopUp.getUserInput("family/Group Order", "enter Id of the orderer", "id or subscriberId :");
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", ordererId));
		Subscriber s = ServerRequest.gson.fromJson(response, Subscriber.class);
		GroupOrderController g = (GroupOrderController) Navigator.instance().navigate("GroupOrder");
		g.setSpontaneous(ordererId);
		if (s.type == Type.SUBSCRIBER)
			g.setFamilyOrderOnly();

	}

	@FXML
	void privateGroupOrder(ActionEvent event) {
		String ordererId = PopUp.getUserInput("private Group Order", "enter Id of the orderer", "id or subscriberId :");
		((SmallGroupOrderController) Navigator.instance().navigate("SmallGroupOrder")).setSpontaneous(ordererId);
	}

	@FXML
	void regularOrder(ActionEvent event) {
		String ordererId = PopUp.getUserInput("regular Order", "enter Id of the orderer", "id or subscriberId :");

		((RegularOrderController) Navigator.instance().navigate("RegularOrder")).setSpontaneous(ordererId);
	}


	@Override
	public void init() {
		parkNumChoise.getItems().addAll(clientController.client.parkNames);
		parkNumChoise.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park,
					"get number of visitor available", parkNumChoise.getSelectionModel().getSelectedItem()));
			if (response.equals("park not exists")) {
				PopUp.showError("available visitors", "failed to get Available visitor",
						"Park " + parkNumChoise.getSelectionModel().getSelectedItem() + " not exists");
				return;
			}
			visitorsAmountText.setText(response);
		});
		if(!clientController.client.logedInWorker.getVal().getWorkerType().equals("departmentManager")) {
			parkNumChoise.getSelectionModel().select(clientController.client.logedInWorker.getVal().getPermissions().GetParkID());
			parkNumChoise.setDisable(true);
		};
	}

	public void setPark(String parkId) {
		parkNumChoise.setDisable(true);
		parkNumChoise.getSelectionModel().select(parkId);
	}

}
