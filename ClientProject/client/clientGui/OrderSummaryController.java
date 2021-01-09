package clientGui;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import clientIO.clientController;
import entities.Order;
import entities.ParkEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	private TextField timeTxt;

	@FXML
	private TextField dateTxt;

	@FXML
	private Label orderNoTxt;

	@FXML
	private TextField noOfSubscribersTxt;

	/** approves the observed {@link Order} */
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
		Navigator.instance().clearHistory(); // return to the main window
	}

	/**
	 * puts data from {@link Order} into fields<br>
	 * and {@link ParkEntry}<br>
	 * using initFieldsByEntry() or initFieldsByOrder()
	 * 
	 * @param order the {@link Order} to get data from
	 */
	public void addOrderDataToFields(Order order, ParkEntry entry) {
		this.order = order;
		this.entry = entry;
		if (entry != null)
			initFieldsByEntry(order, entry);
		else
			initFieldsByOrder(order);
	}

	/**
	 * initialize the window fields using the data from {@link Order}<br>
	 * 
	 * @param order the {@link Order} to get data from
	 */
	private void initFieldsByOrder(Order order) {
		approveBtn.setDisable(false);
		orderNoTxt.setText("Order #: " + String.valueOf(order.orderID));
		personIdTxt.setText(order.ownerID);
		parkNameTxt.setText(order.parkSite);
		timeTxt.setText(toTime(order.visitTime));
		dateTxt.setText(toDate(order.visitTime.getTime()));
		orderTypeTxt.setText(order.type.toString());
		noOfVisitorsTxt.setText(String.valueOf(order.numberOfVisitors));
		noOfSubscribersTxt.setText(String.valueOf(order.numberOfSubscribers));
		emailTxt.setText(order.email);
		phoneTxt.setText(order.phone);
		priceTxt.setText(String.valueOf(order.priceOfOrder));
	}

	/**
	 * initialize the window fields using the data from {@link Order}<br>
	 * and {@link ParkEntry}
	 * 
	 * @param order the {@link Order} to get data from
	 * @param entry the {@link ParkEntry} to get data from
	 */
	private void initFieldsByEntry(Order order, ParkEntry entry) {
		approveBtn.setDisable(false);
		personIdTxt.setText(entry.personID);
		parkNameTxt.setText(entry.parkID);
		timeTxt.setText(toTime(entry.arriveTime));
		dateTxt.setText(toDate(entry.arriveTime.getTime()));
		orderTypeTxt.setText(entry.entryType.toString());
		noOfVisitorsTxt.setText(String.valueOf(entry.numberOfVisitors));
		noOfSubscribersTxt.setText(String.valueOf(entry.numberOfSubscribers));
		emailTxt.setText(order.email);
		phoneTxt.setText(order.phone);
		priceTxt.setText(String.valueOf(entry.priceOfOrder));
	}

	/**
	 * create a time String from Time stamp
	 * 
	 * @param stamp the Timestamp to get time from
	 * @return the time String
	 */
	private String toTime(Timestamp stamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(stamp);
	}

	/**
	 * create a date String from Time stamp
	 * 
	 * @param stamp the Timestamp to get date from
	 * @return the date String
	 */
	private String toDate(long timestamp) {
		Date date = new Date(timestamp);
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}

}
