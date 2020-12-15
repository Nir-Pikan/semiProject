package gui;

import entities.PendingValueChangeRequest;
import entities.PendingValueChangeRequest.ParkAttribute;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import module.GuiController;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

public class DevisionManagerParksDetailsApproveController implements GuiController {
	@FXML
	private TableView<PendingValueChangeRequest> parksDetailsPageTableView;

	@FXML
	private TableColumn<PendingValueChangeRequest, String> parkNum;

	@FXML
	private TableColumn<PendingValueChangeRequest, ParkAttribute> attName;

	@FXML
	private TableColumn<PendingValueChangeRequest, Double> currentVal;

	@FXML
	private TableColumn<PendingValueChangeRequest, Double> requestedValue;

	@FXML
	void DeclineRequest(ActionEvent event) {
		if (parksDetailsPageTableView.getSelectionModel().isEmpty())
			return;
		String res =clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park,"decline change",
				ServerRequest.gson.toJson(parksDetailsPageTableView.getSelectionModel().getSelectedItem(),PendingValueChangeRequest.class)));
		if( res.startsWith("failed")) {
			PopUp.showError("decline change", "decline Failed", "try again");
		}else {
			PopUp.showInformation("decline change", "decline value change succeded", "");
			int index = parksDetailsPageTableView.getSelectionModel().getSelectedIndex();
			parksDetailsPageTableView.getItems().remove(index);
		}
	}

	@FXML
	void approveRequest(ActionEvent event) {
		if (parksDetailsPageTableView.getSelectionModel().isEmpty())
			return;
		
		String res =clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park,"approve change",
				ServerRequest.gson.toJson(parksDetailsPageTableView.getSelectionModel().getSelectedItem(),PendingValueChangeRequest.class)));
		if( res.startsWith("failed")) {
			PopUp.showError("apporve change", "apporve Failed", "try again");
		}else if(res.startsWith("value")) {
			PopUp.showError("apporve change", "MaxPreOrder is larger than current MaxCapacity", "try again");
		}else{
			PopUp.showInformation("apporve change", "apporve value change succeded", "");
			int index = parksDetailsPageTableView.getSelectionModel().getSelectedIndex();
			parksDetailsPageTableView.getItems().remove(index);
		}
	}

	@Override
	public void init() {
		parkNum.setCellValueFactory(new PropertyValueFactory<PendingValueChangeRequest, String>("parkId"));
		attName.setCellValueFactory(new PropertyValueFactory<PendingValueChangeRequest, ParkAttribute>("attName"));
		currentVal.setCellValueFactory(new PropertyValueFactory<PendingValueChangeRequest, Double>("currentValue"));
		requestedValue
				.setCellValueFactory(new PropertyValueFactory<PendingValueChangeRequest, Double>("reuestedValue"));

		
		String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Park, "get pending change requests", ""));
		if (response.equals("[]")) {
			PopUp.showInformation("value change request", "No Value Change Requests", "");
			return;
		}
		if (response.equals("failed")) {
			PopUp.showInformation("value change request", "error while getting value", "call tech support");
			return;
		}
		PendingValueChangeRequest[] arr = ServerRequest.gson.fromJson(response, PendingValueChangeRequest[].class);
		parksDetailsPageTableView.getItems().addAll(arr);
		parksDetailsPageTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

}
