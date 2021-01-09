package clientGui;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import clientIO.clientController;
import entities.Order;
import entities.Order.OrderStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	private TextField dateTxt;

	@FXML
	private TextField timeTxt;

	@FXML
	private TextField noOfSubscribersTxt;

	@FXML
	private Label lblWaitingList;

	/** approves the observed {@link Order} */
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

	/** cancels the observed {@link Order} */
	@FXML
	void cancelOrder(ActionEvent event) {
		if (!PopUp.ask("Cancel Order", "Cancel Order", "Are you sure you wand to cancel?"))
			return;
		switch (order.orderStatus) {
		case IDLE:
		case CONFIRMED:
			clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Order, "CancelOrderByOrderID",
					ServerRequest.gson.toJson(order.orderID, Integer.class)));
			PopUp.showInformation("Order Cancel", "Order Cancel", "order Canceled");
			Navigator.instance().clearHistory();
			return;
		case WAITINGLIST:
			clientController.client.sendRequestAndResponse(new ServerRequest(Manager.WaitingList, "cancelWaitingOrder",
					ServerRequest.gson.toJson(order.orderID, Integer.class)));
			PopUp.showInformation("Order Cancel", "Order Cancel", "order Canceled");
			Navigator.instance().clearHistory();
			return;
		default:
			break;
		}
	}

	/**
	 * puts data from {@link Order} into fields<br>
	 * using initFields()
	 * 
	 * @param order the {@link Order} to get data from
	 */
	public void addOrderDataToFields(Order order) {
		this.order = order;
		initFields(order);
	}

	/**
	 * initialize the window fields using the data from {@link Order}<br>
	 * 
	 * @param order the {@link Order} to get data from
	 */
	private void initFields(Order order) {
		approveBtn.setDisable(false);
		orderNoTxt.setText("Order #: " + String.valueOf(order.orderID));
		personIdTxt.setText(order.ownerID);
		parkNameTxt.setText(order.parkSite);
		timeTxt.setText(toTime(order.visitTime)); // added by (Roman)
		dateTxt.setText(toDate(order.visitTime.getTime())); // added by (Roman)
		orderTypeTxt.setText(order.type.toString());
		noOfVisitorsTxt.setText(String.valueOf(order.numberOfVisitors));
		noOfSubscribersTxt.setText(String.valueOf(order.numberOfSubscribers));
		emailTxt.setText(order.email);
		phoneTxt.setText(order.phone);
		priceTxt.setText(String.valueOf(order.priceOfOrder));
	}

	/**
	 * create a time String from Time stamp
	 * 
	 * @param stamp the Timestamp to get time from
	 * @return the time String
	 */
	private String toTime(Timestamp stamp) { // added by (Roman)
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(stamp);
	}

	/**
	 * create a date String from Time stamp
	 * 
	 * @param stamp the Timestamp to get date from
	 * @return the date String
	 */
	private String toDate(long timestamp) { // added by (Roman)
		Date date = new Date(timestamp);
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}

	@Override
	public void init() {
		String orderId = PopUp.getUserInput("Order details", "Show Order Details", "Order ID:");
		try {
			if (getOrder(Integer.parseInt(orderId)))
				return;
		} catch (NumberFormatException e) {
			PopUp.showError("Order Details", "Order Details", "Order Number is invalid, try again");
			throw new Navigator.NavigationInterruption();
		}
		PopUp.showError("Order Details", "Order Details", "Order not Found, try again");
		throw new Navigator.NavigationInterruption();// tell the navigator to stop the navigation
//		Navigator.instance().back();
	}

	/**
	 * get the {@link Order} from DB
	 * 
	 * @param orederIDint the wanted {@link Order}'s ID
	 * @return true if succeeded<br>
	 *         false otherwise
	 */
	private boolean getOrder(int orederIDint) {
		lblWaitingList.setVisible(false);
		String orderID = orederIDint + "";
		// check if normal order
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "GetOrderByID", orderID));
		if (!response.contains("not found")) {
			Order o = ServerRequest.gson.fromJson(response, Order.class);
			checkOrderOwner(o);
			if (o.visitTime.before(Timestamp.valueOf(LocalDateTime.now()))) {
				PopUp.showError("Show Order Details", "Order Details", "The Order visit time passed");
				throw new Navigator.NavigationInterruption();
			}
			addOrderDataToFields(o);
			if (o.orderStatus == OrderStatus.CANCEL || o.orderStatus == OrderStatus.SEMICANCELED) {
				cancelBtn.setDisable(true);
			} else {
				if (o.orderStatus == OrderStatus.IDLE
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
			lblWaitingList.setVisible(true);
			checkOrderOwner(o);
			addOrderDataToFields(o);
			if (o.orderStatus != OrderStatus.WAITINGLISTMASSAGESENT) {
				approveBtn.setDisable(true);
			} else {
				approveBtn.setDisable(false);
			}
			return true;
		}
		return false;
	}

	/**
	 * checks if the logged in user is the owner of wanted {@link Order}
	 * 
	 * @param o the wanted {@link Order}
	 */
	private void checkOrderOwner(Order o) {
		if (clientController.client.logedInSubscriber.getVal() != null) {
			if (!clientController.client.logedInSubscriber.getVal().subscriberID.equals('S' + o.ownerID)) {
				PopUp.showError("Show Order Details", "Order Details", "You can only see your own orders");
				throw new Navigator.NavigationInterruption();
			} else if (o.orderStatus == Order.OrderStatus.CANCEL) {
				PopUp.showError("Show Order Details", "Order Details", "This order was canceled");
				throw new Navigator.NavigationInterruption();
			}
		}
		if (clientController.client.visitorID.getVal() != null) {
			if (!clientController.client.visitorID.getVal().equals(o.ownerID)) {
				PopUp.showError("Show Order Details", "Order Details", "You can only see your own orders");
				throw new Navigator.NavigationInterruption();
			} else if (o.orderStatus == Order.OrderStatus.CANCEL) {
				PopUp.showError("Show Order Details", "Order Details", "This order was canceled");
				throw new Navigator.NavigationInterruption();
			}
		}

	}
}
