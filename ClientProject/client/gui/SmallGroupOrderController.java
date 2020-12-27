package gui;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import entities.Order;
import io.clientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the SmallGroupOrder page controller */
public class SmallGroupOrderController implements GuiController {

	private int visitorsCounter = 0;
	private List<String> visitorsIDArray = new ArrayList<String>();
	ObservableList<String> stringList;
	Order ord;

	@FXML
	private ComboBox<String> Park_ComboBox;

	@FXML
	private ComboBox<String> VisitHour_ComboBox;

	@FXML
	private TextField Email_textBox;

	@FXML
	private DatePicker Date_DatePicker;

	@FXML
	private Button PlaceOrder_Button;

	@FXML
	private TextField Phone_textBox;

	@FXML
	private ListView<String> listViewVisitors;

	@FXML
	private Button AddVisitor_Button;

	@FXML
	private Label ParkNote;

	@FXML
	private Label DateNote;

	@FXML
	private Label VisitorHourNote;

	@FXML
	private Label EmailNote;

	@FXML
	private Label PhoneNote;

	@FXML
	private Button RemoveVisitor_Button;

	@Override
	public void init() {
		Park_ComboBox.getItems().clear(); // for what? maybe not necessary
		Park_ComboBox.getItems().addAll("#1", "#2", "#3"); //TODO get the real names from DB (Roman)
		VisitHour_ComboBox.getItems().clear(); // for what? maybe not necessary
		// every visit is about 4 hours so: if the park works from 8:00 to 16:00 the
		// last enter time should be 12:00 ?
		VisitHour_ComboBox.getItems().addAll("8:00", "9:00", "10:00", "11:00", "12:00"); //TODO maybe will be changed (Roman)
		// CardTypeComboBox.getSelectionModel().select(0);

		// ===================== delete later ===========================
		Phone_textBox.setText("0545518526"); //TODO delete this later  (Roman)
		Email_textBox.setText("mirage164@gmail.com");
		// =============================================================
		stringList = FXCollections.observableArrayList(visitorsIDArray);
		listViewVisitors.setItems(stringList);
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
	// TODO Remove visitor button length problem

	/**
	 * Check if all the fields was filled correctly
	 * 
	 * @return return true if all the fields are correct, false otherwise
	 */
	private boolean CheckAllRequiredFields() {
		boolean res = true;
		res &= CheckParkSelection();
		res &= CheckDateSelection();
		res &= CheckVisitorHour();
		res &= CheckEmail();
		res &= CheckPhoneNumber();
		return res;
	}
	
	/**
	 * Check that park site was chosen
	 * 
	 * @return true if field is not empty, false otherwise
	 */
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

	/**
	 * Check if date was selected Only relevant dates are can be chosen, no need to
	 * check if the date is relevant
	 * 
	 * @return return true if the field is not empty, false otherwise
	 */
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

	/**
	 * Check if the email address is filled in appropriate form
	 * 
	 * @return true if email address was filled in appropriate form, false otherwise
	 */
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

	/**
	 * Check if the phone number is filled in appropriate form
	 * 
	 * @return return true if id phone number filled in appropriate form, false
	 *         otherwise
	 */
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

	// TODO Check also the hour if the date that was chosen is today
	/**
	 * Check if hour of a visit was selected DONT CHECK IF THE HOUR IS NOT RELEVANT
	 * IF TODAY WAS CHOSEN
	 * 
	 * @return
	 */
	private boolean CheckVisitorHour() {
		if (VisitHour_ComboBox.getValue() == null) {
			if (!VisitHour_ComboBox.getStyleClass().contains("error"))
				VisitHour_ComboBox.getStyleClass().add("error");
			VisitorHourNote.setText("* Please select hour");
			return false;
		}
		VisitHour_ComboBox.getStyleClass().remove("error");
		VisitorHourNote.setText("*");
		return true;
	}

/**
 * when Add Visitor button clicked the method checks if the ID is entered properly and add the ID to a ListView
 * @param event
 */
	@FXML
	void AddVisitor_Button_Clicked(ActionEvent event) { // could the visitor be for different parks? time? date ?
		String ordererId = PopUp.getUserInput("private Group Order", "enter Id of the orderer", "id or subscriberId :");

		if (!CheckID(ordererId)) {
			PopUp.showInformation("Please enter appropriate ID", "Please enter appropriate ID",
					"Please enter appropriate ID");
		} else if (visitorsIDArray.contains(ordererId) || visitorsIDArray.contains("S" + ordererId) || visitorsIDArray.contains(ordererId.substring(1,ordererId.length())) ) {
			PopUp.showInformation("This ID already is added", "This ID already is added", "This ID already is added");
		} else {
			visitorsIDArray.add(ordererId);
			System.out.println("Visitor Edded");
			visitorsCounter++;
			listViewVisitors.getItems().add("visitor #" + visitorsCounter + " " + "(" + ordererId + ")");
			PlaceOrder_Button.setDisable(false);
			RemoveVisitor_Button.setDisable(false);
		}
	}
/**
 * When Remove Visitor button clicked remove visitors ID from ListView
 * @param event
 */
	@FXML
	void RemoveVisitor_Button_Clicked(ActionEvent event) {
		int index = listViewVisitors.getSelectionModel().getSelectedIndex();
		if (index == -1) {
			System.out.println("NOTHING SELECTED!"); //TODO delete latter
		} else {
			stringList.remove(listViewVisitors.getSelectionModel().getSelectedItem());
			visitorsIDArray.remove(index);
			visitorsCounter--;
		}

	}
/**
 * Checks if the ID is from a type of 9 numbers or S and 9 numbers
 * @param ID
 * @return true if ID entered as expected, false otherwise
 */
	private boolean CheckID(String ID) {
		if ((!ID.matches("([0-9])+") || ID.length() != 9) && (!ID.matches("S([0-9])+") || ID.length() != 10)) {
			return false;
		}
		return true;
	}
/**
 * When Place Order button clicked the method checks if the Order can be booked 
 * @param event
 */
	@FXML
	void PlaceOrder_Button_Clicked(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			ord = createOrderDetails();

			String response = clientController.client.sendRequestAndResponse(
					new ServerRequest(Manager.Order, "IsOrderAllowed", ServerRequest.gson.toJson(ord, Order.class)));
			// TODO remove all the not necessary cases after integration (Roman)
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

	private void MoveToTheNextPage(Order ord) {
		Navigator n = Navigator.instance();
		GuiController g = n.navigate("OrderSummary");
		((OrderSummaryController) g).addOrderDataToFields(ord);
	}
/**
 * Creates Order entity by the data that was entered from GUI 
 * @return
 */
	private Order createOrderDetails() {
		String parkName = Park_ComboBox.getValue();
		LocalDate date = Date_DatePicker.getValue();
		int visitHour = Integer.parseInt(VisitHour_ComboBox.getValue().split(":")[0]);
		Timestamp visitTime = Timestamp.valueOf(date.atTime(LocalTime.of(visitHour, 0)));
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis()); // get the current time
		int numberOfVisitors = visitorsCounter;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "NextOrderID", null));
		int orderID = ServerRequest.gson.fromJson(response, Integer.class);
		int priceOfOrder = 100; // for now, need to be calculated by other controller
		boolean isUsed = false; // by default
		Order.IdType type = Order.IdType.PRIVATEGROUP;
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = "323533745"; // TODO the real ownerID will be provided from previous page (popUp)
		int numberOfSubscribers = NumberOfSubscribers(); // in a group order this is not relevant
		Order ord = new Order(parkName, numberOfVisitors, orderID, priceOfOrder, email, phone, type, orderStatus,
				visitTime, timeOfOrder, isUsed, ownerID, numberOfSubscribers);
		return ord;
	}
/**
 * For each ID that was added, checks if the ID is subscriber. (no matters if S was is a first char)
 * @return
 */
	private int NumberOfSubscribers() {
		int res = 0;
		String response = null;
		for (String i : visitorsIDArray) {
			if (!i.contains("S"))
				i = "S" + i; // check if this ID without S is also a subscriber
			response = clientController.client
					.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", i));
			if (!response.contains("was not found"))
				res++;
		}
		return res;
	}

	// TODO setSpontaneous need to be implemented but how??
	public void setSpontaneous(String ordererId) {
		// TODO Auto-generated method stub

	}

}
