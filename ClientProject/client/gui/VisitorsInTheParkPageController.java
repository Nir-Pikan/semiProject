package gui;

import entities.Subscriber;
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
	private TextField availableVisitorsAmountText;


	@FXML
	private TextField visitorsAmountText;

	@FXML
	private Button closeButton;

	@FXML
	void familyOrder(ActionEvent event) {
		String ordererId = PopUp.getUserInput("family/Group Order", "enter Id of the orderer", "id or subscriberId :");
		if (CheckID(ordererId)) {
		if (!ordererId.contains("S"))
			ordererId = "S" + ordererId;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", ordererId));
		if (response.contains("was not found")) {
			PopUp.showInformation("Please enter subscriber ID", "Please enter subscriber ID",
					"Please enter subscriber ID");
			return;
		}
		Subscriber s = ServerRequest.gson.fromJson(response, Subscriber.class);
			GroupOrderController g = (GroupOrderController) Navigator.instance().navigate("GroupOrder");
			g.setSpontaneous(ordererId, parkNumChoise.getValue());
		} else
			PopUp.showInformation("Please enter appropriate ID", "Please enter appropriate ID",
					"Please enter appropriate ID");
//		if (s.type == Type.SUBSCRIBER)TODO check what with this
//			g.setFamilyOrderOnly();

	}

	@FXML
	void privateGroupOrder(ActionEvent event) {
		String ordererId = PopUp.getUserInput("private Group Order", "enter Id of the orderer", "id or subscriberId :");
		if (CheckID(ordererId))
			((SmallGroupOrderController) Navigator.instance().navigate("SmallGroupOrder")).setSpontaneous(ordererId,
					parkNumChoise.getValue());
		else
			PopUp.showInformation("Please enter appropriate ID", "Please enter appropriate ID",
					"Please enter appropriate ID");
	}

	@FXML
	void regularOrder(ActionEvent event) {
		String ordererId = PopUp.getUserInput("regular Order", "enter Id of the orderer", "id or subscriberId :");
		if (CheckID(ordererId))
			((RegularOrderController) Navigator.instance().navigate("RegularOrder")).setSpontaneous(ordererId,
					parkNumChoise.getValue());
		else
			PopUp.showInformation("Please enter appropriate ID", "Please enter appropriate ID",
					"Please enter appropriate ID");
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
			availableVisitorsAmountText.setText(response);
			String response2 = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park,
					"get number of current visitors", parkNumChoise.getSelectionModel().getSelectedItem()));
			if (response2.equals("park not exists")) {
				PopUp.showError("current visitors", "failed to get Current visitor",
						"Park " + parkNumChoise.getSelectionModel().getSelectedItem() + " not exists");
				return;
			}
			visitorsAmountText.setText(response2);
		});
		if (!clientController.client.logedInWorker.getVal().getWorkerType().equals("departmentManager")) {
			parkNumChoise.getSelectionModel()
					.select(clientController.client.logedInWorker.getVal().getPermissions().GetParkID());
			parkNumChoise.setDisable(true);
		}
		;
	}

	public void setPark(String parkId) {
		parkNumChoise.setDisable(true);
		parkNumChoise.getSelectionModel().select(parkId);
	}

	/**
	 * Checks if the ID is from a type of 9 numbers or S and 9 numbers
	 * 
	 * @param ID
	 * @return true if ID entered as expected, false otherwise
	 */
	private boolean CheckID(String ID) {
		if ((!ID.matches("([0-9])+") || ID.length() != 9) && (!ID.matches("S([0-9])+") || ID.length() != 10)) {
			return false;
		}
		return true;
	}

}
