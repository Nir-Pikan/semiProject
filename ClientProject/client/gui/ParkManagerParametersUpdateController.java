package gui;

import entities.PendingValueChangeRequest;
import entities.PendingValueChangeRequest.ParkAttribute;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the ParkManagerParametersUpdate page controller */
public class ParkManagerParametersUpdateController implements GuiController {

	private String[] currentParameters;
	private String parkID;


	@FXML
	private Label labelParkManager;

	@FXML
	private Label labelParkName;

	@FXML
	private TextField textParkName;

	@FXML
	private Label labelcurrentOnChanged;

	@FXML
	private Label labelParkMaxCapacity;

	@FXML
	private TextField textParkMaxCapacity;

	@FXML
	private Label labelOnCapacityChanged;

	@FXML
	private Label labelParkMaxPreOrders;

	@FXML
	private TextField textParkMaxPreOrders;

	@FXML
	private Label labelOnPreOrdersChanged;

	@FXML
	private Label labelParkAVGtime;

	@FXML
	private TextField textParkAVGtime;

	@FXML
	private Label labelOnAVGchanged;

	@FXML
	private Button buttonSendRequest;

	/** when clicked send the update request */
	@FXML
	void sendRequest_OnClick(ActionEvent event) {
		String[] response = new String[3];
		PendingValueChangeRequest pvc;
		if (!validateParameters())
			return;
		if (Integer.parseInt(textParkMaxCapacity.getText()) < Integer.parseInt(textParkMaxPreOrders.getText())) {
			PopUp.showError("value error", "Max Capacity is lower than Max Pre-Order", "");
			return;
		}
		if (!textParkMaxCapacity.getText().equals(currentParameters[0])) {
			pvc = new PendingValueChangeRequest(this.parkID, ParkAttribute.MaxCapacity,
					Integer.parseInt(textParkMaxCapacity.getText()), Integer.parseInt(currentParameters[0]));
			response[0] = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park,
					"add Value Change Request", ServerRequest.gson.toJson(pvc, PendingValueChangeRequest.class)));

		}
		if (!textParkMaxPreOrders.getText().equals(currentParameters[1])) {
			pvc = new PendingValueChangeRequest(this.parkID, ParkAttribute.MaxPreOrder,
					Integer.parseInt(textParkMaxPreOrders.getText()), Integer.parseInt(currentParameters[1]));

			response[1] = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park,
					"add Value Change Request", ServerRequest.gson.toJson(pvc, PendingValueChangeRequest.class)));

		}
		if (!textParkAVGtime.getText().equals(currentParameters[2])) {
			pvc = new PendingValueChangeRequest(this.parkID, ParkAttribute.AvgVisitTime,
					Double.parseDouble(textParkAVGtime.getText()), Double.parseDouble(currentParameters[2]));

			response[2] = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park,
					"add Value Change Request", ServerRequest.gson.toJson(pvc, PendingValueChangeRequest.class)));
		}

		StringBuilder sb = new StringBuilder("");
		if (response[0] != null) {
			sb.append("Max Capacity Change: ");
			if (response[0].startsWith("failed")) {
				sb.append("failed, Request Exists\n");
			} else {
				sb.append("success\n");
			}
		}
		if (response[1] != null) {
			sb.append("Max Pre-Order Change: ");
			if (response[1].startsWith("failed")) {
				sb.append("failed, Request Exists\n");
			} else {
				sb.append("success\n");
			}
		}
		if (response[2] != null) {
			sb.append("Avg Visit time Change: ");
			if (response[2].startsWith("failed")) {
				sb.append("failed, Request Exists\n");
			} else {
				sb.append("success\n");
			}
		}
		if (!sb.toString().equals("")) {
			PopUp.showInformation("value change report", "Status of value change requests", sb.toString());
		}
		// Navigator.instance().clearHistory();
	}

	/**
	 * Validates all the parameters if in the right number format(int or double)
	 * 
	 * @return if all the parameters are in the right format (true) else (false)
	 */
	private boolean validateParameters() {
		boolean res = true;
		try {
			if (Integer.parseInt(textParkMaxCapacity.getText()) < 0)
				res = false;
			textParkMaxCapacity.getStyleClass().remove("error");
		} catch (NumberFormatException e) {
			textParkMaxCapacity.getStyleClass().add("error");
			res = false;
		}
		try {
			if (Integer.parseInt(textParkMaxPreOrders.getText()) < 0)
				res = false;
			textParkMaxPreOrders.getStyleClass().remove("error");
		} catch (NumberFormatException e) {
			textParkMaxPreOrders.getStyleClass().add("error");
			res = false;
		}
		try {
			if (Double.parseDouble(textParkAVGtime.getText()) < 0)
				res = false;
			textParkAVGtime.getStyleClass().remove("error");
		} catch (NumberFormatException e) {
			textParkAVGtime.getStyleClass().add("error");
			res = false;
		}

		return res;
	}

	@Override
	public void init() {
		
		setPark(clientController.client.logedInWorker.getVal().getPermissions().GetParkID());
	}

	public void setPark(String ParkId) {
		this.parkID = ParkId;
		textParkName.setText(ParkId);
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Park, "get current parameter", ParkId));
		if (response.equals("park not exists")) {
			PopUp.showError("park not exists", "park not exists", "Park manager for non existing park");
			return;
		}
		currentParameters = ServerRequest.gson.fromJson(response, String[].class);
		textParkMaxCapacity.setText(currentParameters[0]);
		textParkMaxPreOrders.setText(currentParameters[1]);
		textParkAVGtime.setText(currentParameters[2]);
		textParkMaxCapacity.textProperty().addListener((obs, oldVal, newval) -> {
			if (oldVal.equals(currentParameters[0])) {
				labelOnCapacityChanged.setText("Current Value: " + currentParameters[0]);
			}
			if (newval.equals(currentParameters[0])) {
				labelOnCapacityChanged.setText("");
			}
		});
		textParkMaxPreOrders.textProperty().addListener((obs, oldVal, newval) -> {
			if (oldVal.equals(currentParameters[1])) {
				labelOnPreOrdersChanged.setText("Current Value: " + currentParameters[1]);
			}
			if (newval.equals(currentParameters[1])) {
				labelOnPreOrdersChanged.setText("");
			}
		});
		textParkAVGtime.textProperty().addListener((obs, oldVal, newval) -> {
			if (oldVal.equals(currentParameters[2])) {
				labelOnAVGchanged.setText("Current Value: " + currentParameters[2]);
			}
			if (newval.equals(currentParameters[2])) {
				labelOnAVGchanged.setText("");
			}
		});
	}
}
