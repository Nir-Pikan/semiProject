package gui;

import java.time.LocalDate;
import java.time.LocalTime;

import entities.Order;
import entities.Order.OrderStatus;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

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

	@FXML
	void ApproveOrder(ActionEvent event) {
		switch (order.orderStatus) {
		case IDLE:
			order.orderStatus = OrderStatus.CONFIRMED;
			String response = clientController.client.sendRequestAndResponse(
					new ServerRequest(Manager.Order, "UpdateOrder", ServerRequest.gson.toJson(order, Order.class)));
			if (response.contains("Failed")) {
				PopUp.showError("Order Approve", "Order Approve", "failed to approve order");
			} else {
				PopUp.showInformation("Order Approve", "Order Approve", "order approved");
				getOrder(order.orderID);
			}
			return;
		case WAITINGLISTMASSAGESENT:
			clientController.client.sendRequestAndResponse(
					new ServerRequest(Manager.WaitingList, "acceptWaitingOrder", String.valueOf(order.orderID)));
			PopUp.showInformation("Order Approve", "Order Approve", "order approved");
			getOrder(order.orderID);
			return;
		default:
			PopUp.showError("Order Approve", "Order Approve", "cannot approve order");
		}

	}

	@FXML
	void cancelOrder(ActionEvent event) {
		PopUp p = new PopUp(AlertType.CONFIRMATION, "Are you sure you wand to cancel?", ButtonType.CANCEL,
				ButtonType.CLOSE);
		p.setTitle("Cancel Order");
		p.setHeaderText("Cancel Order");
		if (p.showAndWait().get() != ButtonType.CANCEL)
			return;
		switch (order.orderStatus) {
		case IDLE:
			clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Order, "CancelOrderByOrderID",
					ServerRequest.gson.toJson(order, Order.class)));
			PopUp.showInformation("Order Cancel", "Order Cancel", "order Canceled");
			Navigator.instance().clearHistory();
			return;
		case WAITINGLISTMASSAGESENT:
			clientController.client.sendRequestAndResponse(new ServerRequest(Manager.WaitingList, "cancelWaitingOrder",
					ServerRequest.gson.toJson(order, Order.class)));
			PopUp.showInformation("Order Cancel", "Order Cancel", "order Canceled");
			Navigator.instance().clearHistory();
			return;
		default:
			break;
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
		noOfSubscribersTxt.setText(String.valueOf(order.numberOfSubscribers));
		emailTxt.setText(order.email);
		phoneTxt.setText(order.phone);
		priceTxt.setText(String.valueOf(order.priceOfOrder));
	}

	@Override
	public void init() {
		String orderId = PopUp.getUserInput("Order details", "Show Order Details", "Order ID:");
		try {
		if (getOrder(Integer.parseInt(orderId)))
			return;
		}catch(NumberFormatException e) {
			PopUp.showError("Order Details", "Order Details", "Order Number is invalid, try again");
			throw new Navigator.NavigationInterruption();
		}
		PopUp.showError("Order Details", "Order Details", "Order not Found, try again");
		Navigator.instance().back();
		throw new Navigator.NavigationInterruption();// tell the navigator to stop the navigation
	}

	private boolean getOrder(int orederIDint) {
		String orderID = orederIDint + "";
		// check if normal order
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "GetOrderByID", orderID));
		if (!response.contains("not found")) {
			Order o = ServerRequest.gson.fromJson(response, Order.class);
			checkOrderOwner(o);
			addOrderDataToFields(o);
			if (o.orderStatus == OrderStatus.CANCEL || o.orderStatus == OrderStatus.SEMICANCELED) {
				cancelBtn.setDisable(true);
			} else {
				if (o.orderStatus != OrderStatus.CONFIRMED
						&& o.visitTime.toLocalDateTime().toLocalDate().equals(LocalDate.now().plusDays(1))
						&& LocalTime.now().isAfter(LocalTime.of(10, 00))
						&& LocalTime.now().isBefore(LocalTime.of(12, 00))) {
					approveBtn.setDisable(false);
				} else {
					approveBtn.setDisable(true);
				}
			}
			return true;
		}

		// check if waitingList order
		String response2 = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.WaitingList, "GetOrderByID", orderID));
		if (!response2.contains("not found")) {
			Order o = ServerRequest.gson.fromJson(response2, Order.class);
			addOrderDataToFields(o);
			if (o.orderStatus != OrderStatus.WAITINGLISTMASSAGESENT) {
				approveBtn.setDisable(false);
			} else {
				approveBtn.setDisable(true);
			}
			return true;
		}
		return false;
	}

	private void checkOrderOwner(Order o) {
		if (clientController.client.logedInSunscriber.getVal() != null) {
			if (!clientController.client.logedInSunscriber.getVal().subscriberID.equals(o.ownerID)) {
				PopUp.showError("Show Order Details", "Order Details", "You can see only your own order");
				throw new Navigator.NavigationInterruption();
			}
		}
		if (clientController.client.visitorID.getVal() != null) {
			if (!clientController.client.visitorID.getVal().equals(o.ownerID)) {
				PopUp.showError("Show Order Details", "Order Details", "You can see only your own order");
				throw new Navigator.NavigationInterruption();
			}
		}

	}
}
