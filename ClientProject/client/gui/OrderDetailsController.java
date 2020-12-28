package gui;

import entities.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

/** the OrderDetails page controller */
public class OrderDetailsController implements GuiController {

	private Order order; // to initialize the fields

	@FXML
	private Button approveBtn;

	@FXML
	private Button cancelBtn;

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

	/*   maybe someone will use park of this code, if no delete this. This park writed accidentally here its OrderSummary
	@FXML
	void ApproveOrder(ActionEvent event) {
		String response = clientController.client.sendRequestAndResponse(
				new ServerRequest(Manager.Order, "AddNewOrder", ServerRequest.gson.toJson(order, Order.class)));
		switch (response) {
		case "Order was added successfully":
			PopUp.showInformation("Order placed success", "Order placed success",
					"Order placed successfully!\n" + "Your Order ID is:\n" + order.orderID);
			// get a handle to the stage
		    Stage stage = (Stage) approveBtn.getScene().getWindow();
		    // do what you have to do
		    stage.close();
			break;
		case "Order already exists":
			PopUp.showInformation("Order already exists", "Order already exists", "Order already exists");
			break;
		case "No more orders allowed in this time":
			PopUp.showInformation("No more orders allowed in this time", "No more orders allowed in this time",
					"No more orders allowed in this time");
			break;
		}
	}

	@FXML
	void cancelOrder(ActionEvent event) {
		Navigator n = Navigator.instance();
		if (order.type == Order.IdType.PRIVATE) {
			GuiController g = n.navigate("RegularOrder");
			System.out.println(order.ownerID);
			((RegularOrderController) g).addOrderDataToFields(order);
		}else if(order.type == Order.IdType.GUIDE || order.type == Order.IdType.FAMILY) {
			GuiController g = n.navigate("GroupOrder");
			System.out.println(order.ownerID);
			((GroupOrderController) g).addOrderDataToFields(order);
		}
	}

	public void addOrderDataToFields(Order order) {
		this.order = order;
		initFields(order);
	}

	private void initFields(Order order) {
		approveBtn.setDisable(false);
		orderNoTxt.setText("Order #: " + String.valueOf(order.orderID));
		personIdTxt.setText(order.ownerID);
		parkNameTxt.setText(order.parkSite);
		orderTypeTxt.setText(order.type.toString());
		noOfVisitorsTxt.setText(String.valueOf(order.numberOfVisitors));
		noOfSubscribersTxt.setText("0"); // change later
		emailTxt.setText(order.email);
		phoneTxt.setText(order.phone);
		priceTxt.setText(String.valueOf(order.priceOfOrder));
	}
*/
}
