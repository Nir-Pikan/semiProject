package gui;

import entities.Order;
import entities.ParkEntry;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

public class OrderSummaryController implements GuiController {

	private Order order; // to initialize the fields
	private ParkEntry entry; // for spontaneous visitors

	@FXML
	private Button approveBtn;

	@FXML
	private TextField personIdTxt;

	@FXML
	private TextField parkNameTxt;

	@FXML
	private TextField orderTypeTxt;

	@FXML
	private TextField noOfVisitorsTxt;

	@FXML
	private TextField emailTxt;

	@FXML
	private TextField phoneTxt;

	@FXML
	private TextField priceTxt;

	@FXML
	private Label orderNoTxt;

	@FXML
	private TextField noOfSubscribersTxt;

	@FXML
	void ApproveOrder(ActionEvent event) {
		if (entry == null) { // if entry = null its regular order
			String response = clientController.client.sendRequestAndResponse(
					new ServerRequest(Manager.Order, "AddNewOrder", ServerRequest.gson.toJson(order, Order.class)));
			switch (response) {
			case "Order was added successfully":
				PopUp.showInformation("Order placed success", "Order placed success",
						"Order placed successfully!\n" + "Your Order ID is:\n" + order.orderID);
				break;
			case "Order already exists":
				PopUp.showInformation("Order already exists", "Order already exists", "Order already exists");
				break;
			case "No more orders allowed in this time":
				PopUp.showInformation("No more orders allowed in this time", "No more orders allowed in this time",
						"No more orders allowed in this time");
				break;
			}
		} else { // if entry != null its spontaneous visit
			String response = clientController.client.sendRequestAndResponse(
					new ServerRequest(Manager.Entry, "AddNewEntry", ServerRequest.gson.toJson(entry, ParkEntry.class)));
			switch (response) {
			case "Failed to add new Entry got Null":
				PopUp.showInformation("Failed to add new Entry got Null", "Failed to add new Entry got Null",
						"Failed to add new Entry got Null");
				break;

			case "Entry was added successfully":
				PopUp.showInformation("OEntry was added successfully", "Entry was added successfully",
						"Entry was added successfully");
				break;
			case "Failed to add Entry":
				PopUp.showInformation("Failed to add Entry", "Failed to add Entry", "Failed to add Entry");
			default:
				PopUp.showInformation("Unexpected Error", "Unexpected Error", "Unexpected Error");
			}
		}
		Navigator.instance().clearHistory();
	}

	public void addOrderDataToFields(Order order, ParkEntry entry) {
		this.order = order;
		this.entry = entry;
		initFields(order, entry);
	}

	private void initFields(Order order, ParkEntry entry) {
		approveBtn.setDisable(false);
		if (entry == null)
			orderNoTxt.setText("Order #: " + String.valueOf(order.orderID));
		personIdTxt.setText(order.ownerID);
		parkNameTxt.setText(order.parkSite);
		orderTypeTxt.setText(order.type.toString());
		noOfVisitorsTxt.setText(String.valueOf(order.numberOfVisitors));
		noOfSubscribersTxt.setText(String.valueOf(order.numberOfSubscribers));
		emailTxt.setText(order.email);
		phoneTxt.setText(order.phone);
		float price = calcOrderPrice(order);
		order.priceOfOrder = price;
		
		priceTxt.setText(String.valueOf(price));
	}

	private float calcOrderPrice(Order order) {
		String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Discount,
				"CalculatePriceForEntryByOrder", ServerRequest.gson.toJson(order, Order.class)));
		return ServerRequest.gson.fromJson(response, Float.class);
	}

}
