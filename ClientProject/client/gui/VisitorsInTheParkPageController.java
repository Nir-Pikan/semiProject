package gui;

import entities.Subscriber;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
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

	/** create a casual family order */
	@FXML
	void familyOrder(ActionEvent event) {
		if (!checkParkCapacity(parkNumChoise.getValue().toString())) {
			PopUp.showInformation("Not enough space", "Not enough space", "Not enough space in the park");
			return;
		}
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

	}

	/** create a casual private group order */
	@FXML
	void privateGroupOrder(ActionEvent event) {
		if (!checkParkCapacity(parkNumChoise.getValue().toString())) {
			PopUp.showInformation("Not enough space", "Not enough space", "Not enough space in the park");
			return;
		}
		String ordererId = PopUp.getUserInput("private Group Order", "enter Id of the orderer", "id or subscriberId :");
		if (CheckID(ordererId))
			((SmallGroupOrderController) Navigator.instance().navigate("SmallGroupOrder")).setSpontaneous(ordererId,
					parkNumChoise.getValue());
		else
			PopUp.showInformation("Please enter appropriate ID", "Please enter appropriate ID",
					"Please enter appropriate ID");
	}

	/** create a casual regular order */
	@FXML
	void regularOrder(ActionEvent event) {
		if (!checkParkCapacity(parkNumChoise.getValue().toString())) {
			PopUp.showInformation("Not enough space", "Not enough space", "Not enough space in the park");
			return;
		}
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
		parkNumChoise.getSelectionModel().select(0);
	}

	/**
	 * sets the park field
	 * 
	 * @param parkId the chosen {@link Park}'s id
	 */
	public void setPark(String parkId) {
		parkNumChoise.setDisable(true);
		parkNumChoise.getSelectionModel().select(parkId);
	}

	/**
	 * Checks if the ID is a 9 digits number<br>
	 * or S + 9 digits number
	 * 
	 * @param id the id to check
	 * @return true if ID is in right format<br>
	 *         false otherwise
	 */
	private boolean CheckID(String id) {
		if ((!id.matches("([0-9])+") || id.length() != 9) && (!id.matches("S([0-9])+") || id.length() != 10)) {
			return false;
		}
		return true;
	}

	/**
	 * check if there is free space in {@link Park}
	 * 
	 * @param park the wanted {@link Park}'s ID
	 * @return true if the is free space<br>
	 *         false if park is full
	 */
	private boolean checkParkCapacity(String park) {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Park, "get number of visitor available", park));
		int availableSpace = ServerRequest.gson.fromJson(response, Integer.class);
		if (availableSpace < 1)
			return false;
		return true;
	}

}
