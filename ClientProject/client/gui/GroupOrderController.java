package gui;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import entities.Order;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the GroupOrder page controller */
public class GroupOrderController implements GuiController {

	Order ord;

	@FXML
	private ComboBox<String> Park_ComboBox;

	@FXML
	private ComboBox<String> VisitHour_ComboBox;

	@FXML
	private ComboBox<String> NumberOfVisitors_ComboBox;

	@FXML
	private TextField Email_textBox;

	@FXML
	private DatePicker Date_DatePicker;

	@FXML
	private CheckBox FamilyIndicator_checkBox;

	@FXML
	private Button PlaceOrder_Button;

	@FXML
	private TextField Phone_textBox;

	@FXML
	private Label PhoneNote;

	@FXML
	private Label FamilyOrderNote;

	@FXML
	private Label EmailNote;

	@FXML
	private Label NumberOfVisitorsNote;

	@FXML
	private Label VisitHourNote;

	@FXML
	private Label DateNote;

	@FXML
	private Label ParkNote;

	@Override
	public void init() {
		Park_ComboBox.getItems().clear(); // for what? maybe not necessary
		Park_ComboBox.getItems().addAll("#1", "#2", "#3");
		VisitHour_ComboBox.getItems().clear(); // for what? maybe not necessary
		// every visit is about 4 hours so: if the park works from 8:00 to 16:00 the
		// last enter time should be 12:00 ?
		VisitHour_ComboBox.getItems().addAll("8:00", "9:00", "10:00", "11:00","12:00");
		// CardTypeComboBox.getSelectionModel().select(0);
		NumberOfVisitors_ComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
				"14", "15");
		PlaceOrder_Button.setDisable(false); // why disabling the button by default??

		// ===================== delete later ===========================
		Phone_textBox.setText("0545518526");
		Email_textBox.setText("mirage164@gmail.com");
		// =============================================================

		// set only relevant dates
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate today = LocalDate.now();
						setDisable(empty || item.compareTo(today) < 0);
					}

				};
			}

		};
		Date_DatePicker.setDayCellFactory(callB);
	}

	private boolean CheckAllRequiredFields() {
		boolean res = true;
		res &= CheckParkSelection();
		res &= CheckDateSelection();
		res &= CheckVisitorHour();
		res &= CheckEmail();
		res &= CheckPhoneNumber();
		res &= CheckNumberOfVisitors();
		return res;
	}

	private boolean CheckParkSelection() {
		if (Park_ComboBox.getSelectionModel().isEmpty()) {
			if (!Park_ComboBox.getStyleClass().contains("error"))
				Park_ComboBox.getStyleClass().add("error");
			ParkNote.setText("please choose park");
			return false;
		}
		Park_ComboBox.getStyleClass().remove("error");
		ParkNote.setText("*");
		return true;
	}

	private boolean CheckDateSelection() {
		if (Date_DatePicker.getValue() == null) {
			if (!Date_DatePicker.getStyleClass().contains("error"))
				Date_DatePicker.getStyleClass().add("error");
			DateNote.setText("* Please select date");
			return false;
		}
		Date_DatePicker.getStyleClass().remove("error");
		DateNote.setText("*");
		return true;
	}

	private boolean CheckVisitorHour() {
		if (VisitHour_ComboBox.getValue() == null) {
			if (!VisitHour_ComboBox.getStyleClass().contains("error"))
				VisitHour_ComboBox.getStyleClass().add("error");
			VisitHourNote.setText("* Please select hour");
			return false;
		}
		VisitHour_ComboBox.getStyleClass().remove("error");
		VisitHourNote.setText("*");
		return true;
	}

	private boolean CheckNumberOfVisitors() {
		if (NumberOfVisitors_ComboBox.getValue() == null) {
			if (!NumberOfVisitors_ComboBox.getStyleClass().contains("error"))
				NumberOfVisitors_ComboBox.getStyleClass().add("error");
			NumberOfVisitorsNote.setText("* Please select number of visitors");
			return false;
		}
		NumberOfVisitors_ComboBox.getStyleClass().remove("error");
		NumberOfVisitorsNote.setText("*");
		return true;
	}

	private boolean CheckEmail() {
		String email = Email_textBox.getText();

		// if field is empty
		if (email.equals("")) {
			if (!Email_textBox.getStyleClass().contains("error"))
				Email_textBox.getStyleClass().add("error");
			EmailNote.setText("* Please enter Email");
			return false;
		}

		String emailFormat = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$";

		// if field is not in wanted format
		if (!email.matches(emailFormat)) {
			if (!Email_textBox.getStyleClass().contains("error"))
				Email_textBox.getStyleClass().add("error");
			EmailNote.setText("* Email must be _@_._");
			return false;
		}

		Email_textBox.getStyleClass().remove("error");
		EmailNote.setText("*");
		return true;
	}

	private boolean CheckPhoneNumber() {
		String phoneNumber = Phone_textBox.getText();

		// if field is empty
		if (phoneNumber.equals("")) {
			if (!Phone_textBox.getStyleClass().contains("error"))
				Phone_textBox.getStyleClass().add("error");
			PhoneNote.setText("* Please enter Phone Number");
			return false;
		}

		// if field contains a character that is not a digit
		if (!phoneNumber.matches("([0-9])+")) {
			if (!Phone_textBox.getStyleClass().contains("error"))
				Phone_textBox.getStyleClass().add("error");
			PhoneNote.setText("* Phone Number can only contain digits");
			return false;
		}

		// if field contains more or less than 10 digits
		if (phoneNumber.length() != 10) {
			if (!Phone_textBox.getStyleClass().contains("error"))
				Phone_textBox.getStyleClass().add("error");
			PhoneNote.setText("* Phone Number must be 10 digits long");
			return false;
		}

		Phone_textBox.getStyleClass().remove("error");
		PhoneNote.setText("*");
		return true;
	}

	@FXML
	void PlaceOrder_Button_Clicked(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			ord = createOrderDetails();

			String response = clientController.client.sendRequestAndResponse(
					new ServerRequest(Manager.Order, "IsOrderAllowed", ServerRequest.gson.toJson(ord, Order.class)));
			//TODO remove all the not necessary cases after integration (Roman)
			switch (response) {
			case "Order was added successfully":
				PopUp.showInformation("Order placed success", "Order placed success",
						"Order placed successfully!\n" + "Your Order ID is:\n" + ord.orderID);
				break;

			case "Order was not found":
				PopUp.showInformation("Order was not found", "Order was not found", "No such order exists");
				break;

			case "Failed to add Order":
				PopUp.showInformation("Order failed", "Order failed", "Order faild!");
				break;

			case "Order already exists":
				PopUp.showInformation("Order already exists", "Order already exists", "Order already exists");
				break;
			case "Owner with this ID is not found":
				PopUp.showInformation("Owner not exists", "Owner not exists!", "Owner with this ID not exists");
				break;
			case "Failed to cancel an order":
				PopUp.showInformation("Failed to cancel an order", "Failed to cancel an order",
						"Failed to cancel an order");
				break;
			case "No more orders allowed in this time":
				PopUp.showInformation("No more orders allowed in this time", "No more orders allowed in this time",
						"No more orders allowed in this time");
				break;
			case "Order Canceled":
				PopUp.showInformation("Order Canceled", "Order Canceled", "Order Canceled");
				break;
			case "Order seted as used":
				PopUp.showInformation("Order seted as used", "Order seted as used", "Order seted as used");
				break;
			case "Failed to set order as used":
				PopUp.showInformation("Failed to set order as used", "Failed to set order as used",
						"Failed to set order as used");
				break;
			case "Order updated":
				PopUp.showInformation("Order updated", "Order updated", "Order updated");
				break;
			case "Failed to update order":
				PopUp.showInformation("Failed to update order", "Failed to update order", "Failed to update order");
				break;
			case "Order can be placed":
				PopUp.showInformation("Order can be placed", "Order can be placed", "Order can be placed");
				MoveToTheNextPage(ord);
				break;
			case "Error: No such job":
				PopUp.showInformation("Unexpected error", "Unexpected error!",
						"server returned an unexpected response");
			}
		}

	}

// what to do with that??????????????????????????????????
	public void setSpontaneous(String ordererId) {
		// TODO Auto-generated method stub

	}

	public void setFamilyOrderOnly() {
		// TODO Auto-generated method stub

	}

	private Order createOrderDetails() {
		String parkName = Park_ComboBox.getValue();
		LocalDate date = Date_DatePicker.getValue();
		int visitHour = Integer.parseInt(VisitHour_ComboBox.getValue().split(":")[0]);
		Timestamp visitTime = Timestamp.valueOf(date.atTime(LocalTime.of(visitHour, 0)));
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis()); // get the current time
		int numberOfVisitors = Integer.parseInt(NumberOfVisitors_ComboBox.getValue());
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "NextOrderID", null));
		int orderID = ServerRequest.gson.fromJson(response, Integer.class);
		int priceOfOrder = 100; // for now, need to be calculated by other controller
		boolean isUsed = false; // by default
		Order.IdType type = familyOrGuide();
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = "323533745"; // TODO the real ownerID will be provided from previous page (popUp)
		int numberOfSubscribers = 0; // in a group order this is not relevant
		Order ord = new Order(parkName, numberOfVisitors, orderID, priceOfOrder, email, phone, type, orderStatus,
				visitTime, timeOfOrder, isUsed, ownerID,numberOfSubscribers);
		return ord;
	}

	private Order.IdType familyOrGuide() {
		if (FamilyIndicator_checkBox.isSelected())
			return Order.IdType.FAMILY;
		return Order.IdType.GUIDE;
	}

	private void MoveToTheNextPage(Order ord) {
		Navigator n = Navigator.instance();
		GuiController g = n.navigate("OrderSummary");
		((OrderSummaryController) g).addOrderDataToFields(ord);
	}
	
	public void addOrderDataToFields(Order order) {
		ord = order;
		initFields(ord);
	}

	private void initFields(Order order) {
		Park_ComboBox.setValue(order.parkSite);
		Date_DatePicker.setValue(order.visitTime.toLocalDateTime().toLocalDate());
		VisitHour_ComboBox.setValue(order.visitTime.toLocalDateTime().getHour() + ":" + order.visitTime.toLocalDateTime().getMinute() + "0");
		NumberOfVisitors_ComboBox.setValue(order.numberOfVisitors + "");
		FamilyIndicator_checkBox.setSelected(order.type == Order.IdType.FAMILY ? true : false);
		Email_textBox.setText(order.email);
		Phone_textBox.setText(order.phone);
	}
	

}
