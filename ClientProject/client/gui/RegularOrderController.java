package gui;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import entities.Order;
import entities.Park;
import entities.ParkEntry;
import entities.ParkNameAndTimes;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import logic.OpenOrderTimes;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the RegularOrder page controller */
public class RegularOrderController implements GuiController {

	public Map<String, ParkNameAndTimes> openingTimes = clientController.client.openingTimes;
	public String[] parkNames = clientController.client.parkNames;
	private Order ord = new Order();
	private ParkEntry parkEntry;
	private boolean spontaneous = false;
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
	private Label ParkSelectionNote;

	@FXML
	private Label DateSelecionNote;

	@FXML
	private Label VisitorHourNote;

	@FXML
	private Label EmailNote;

	@FXML
	private Label PhoneNote;

	@Override
	public void init() {
		Park_ComboBox.getItems().clear();
		VisitHour_ComboBox.getItems().clear();
		// parkEntry = null; // to be sure
		Park_ComboBox.getItems().addAll(parkNames);
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

	private String[] CreateWorkingHours(ParkNameAndTimes parkDetails) {
		VisitHour_ComboBox.getItems().clear();
		int numberOfWorkingHours = parkDetails.closeTime - parkDetails.openTime;
		String[] res = new String[numberOfWorkingHours];
		for (int i = parkDetails.openTime; i < parkDetails.closeTime; i++) {
			res[i - parkDetails.openTime] = i + ":00";
		}
		return res;
	}

	@FXML
	void parkWasChosen(ActionEvent event) {
		ParkNameAndTimes temp = null;
		for (int i = 0; i < parkNames.length; i++)
			if (parkNames[i].equals(Park_ComboBox.getValue()))
				temp = openingTimes.get(parkNames[i]);
		VisitHour_ComboBox.getItems().addAll(CreateWorkingHours(temp));
	}

	// TODO error messages length visual

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
			ParkSelectionNote.setText("* Please choose park");
			return false;
		}
		Park_ComboBox.getStyleClass().remove("error");
		ParkSelectionNote.setText("*");
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
			DateSelecionNote.setText("* Please select date");
			return false;
		}
		Date_DatePicker.getStyleClass().remove("error");
		DateSelecionNote.setText("*");
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

	/**
	 * Listener that responsible for PlaceOrder button
	 * 
	 * @param event that send a request of new order to the DB through
	 *              OrderController and get the answer from OrderController
	 */

	// in a case you want to see the order that been initialized
//		System.out.println(o.parkSite);
//		System.out.println(o.numberOfVisitors);
//		System.out.println(o.orderID);
//		System.out.println(o.priceOfOrder);
//		System.out.println(o.email);
//		System.out.println(o.phone);
//		System.out.println(o.type);
//		System.out.println(o.orderStatus);
//		System.out.println(o.visitTime);
//		System.out.println(o.timeOfOrder);
//		System.out.println(o.isUsed);

//================  examples of some functions i used to test the methods from OrderController  =====================

//		String startTimeAndEndTimeAndParkSite[] = new String[3];
//		startTimeAndEndTimeAndParkSite[0] = ServerRequest.gson.toJson(start, Timestamp.class);
//		startTimeAndEndTimeAndParkSite[1] = ServerRequest.gson.toJson(end, Timestamp.class);
//		startTimeAndEndTimeAndParkSite[2] = parkSite;
//		String response = clientController.client
//				.sendRequestAndResponse(new ServerRequest(Manager.Order, "GetAllListOfOrders", null));
//		Order[] res = ServerRequest.gson.fromJson(response, Order[].class);

//		String response = clientController.client.sendRequestAndResponse(
//				new ServerRequest(Manager.Order, "AddNewOrder", ServerRequest.gson.toJson(o, Order.class)));
//		String response = clientController.client.sendRequestAndResponse(
//				new ServerRequest(Manager.Order, "CancelOrderByOrderID", ServerRequest.gson.toJson(1, Integer.class)));

	// write into the DB
//		String response = clientController.client.sendRequestAndResponse(
//				new ServerRequest(Manager.Order, "AddNewOrder", ServerRequest.gson.toJson(o, Order.class)));

	/**
	 * when Place Order button clicked checks if this order can be booked
	 * 
	 * @param event
	 */
	@FXML
	void PlaceOrder_Button_Clicked(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			if (spontaneous == true) {
				ord = createSpontaneousOrderDetails(ord.ownerID,ord.parkSite);
				parkEntry = createParkEntry(ord);
				MoveToTheNextPage(ord, parkEntry);
				return;
			} 
				ord = createOrderDetails();
//			String response = clientController.client.sendRequestAndResponse(
//			new ServerRequest(Manager.Order, "SetOrderToIsUsed", ServerRequest.gson.toJson(11, Integer.class)));
				String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Order,
						"IsOrderAllowed", ServerRequest.gson.toJson(ord, Order.class)));
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
//					PopUp.showInformation("No more orders allowed in this time", "No more orders allowed in this time",
//							"No more orders allowed in this time"); // another options will be displayed and offer to waiting list
					Order newSelectedOrder  = OpenOrderTimes.askForWaitingListAndShowOptions(ord);
					if(newSelectedOrder == null)
						return;
					else
						MoveToTheNextPage(newSelectedOrder,null);
//						 response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Order,
//								"IsOrderAllowed", ServerRequest.gson.toJson(newSelectedOrder, Order.class)));
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
					MoveToTheNextPage(ord, null);
					break;
				case "Error: No such job":
					PopUp.showInformation("Unexpected error", "Unexpected error!",
							"server returned an unexpected response");
				}
		}

	}

	private void MoveToTheNextPage(Order ord, ParkEntry entry) {
		Navigator n = Navigator.instance();
		GuiController g = n.navigate("OrderSummary");
		((OrderSummaryController) g).addOrderDataToFields(ord, entry);
	}


//	public void addOrderDataToFields(Order order) {
//		ord = order;
//		initFields(ord);
//	}
//
//	/**
//	 * initialize fields by Order entity
//	 * 
//	 * @param order
//	 */
//	private void initFields(Order order) {
//		Park_ComboBox.setValue(order.parkSite);
//		Date_DatePicker.setValue(order.visitTime.toLocalDateTime().toLocalDate());
//		VisitHour_ComboBox.setValue(order.visitTime.getHours() + ":" + order.visitTime.getMinutes() + "0");
//		Email_textBox.setText(order.email);
//		Phone_textBox.setText(order.phone);
//	}


	/**
	 * Create Order Entity from the fields that was willed by the user on a GUI
	 * 
	 * @return return the Order Entity that was created
	 */
	private Order createOrderDetails() {
		String parkName = Park_ComboBox.getValue();
		LocalDate date = Date_DatePicker.getValue();
		int visitHour = Integer.parseInt(VisitHour_ComboBox.getValue().split(":")[0]);
		Timestamp visitTime = Timestamp.valueOf(date.atTime(LocalTime.of(visitHour, 0)));
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis()); // get the current time
		int numberOfVisitors = 1; // by default
		int orderID = getNextOrderID();
		float priceOfOrder = 100;
		boolean isUsed = false; // by default
		Order.IdType type = Order.IdType.PRIVATE; // by default
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = getIdentificationString();
		int numberOfSubscribers = isSubscriber();
		Order ord = new Order(parkName, numberOfVisitors, orderID, priceOfOrder, email, phone, type, orderStatus,
				visitTime, timeOfOrder, isUsed, ownerID, numberOfSubscribers);
		return ord;
	}
	

	private String getIdentificationString() {
		if (clientController.client.visitorID.getVal() != null)
			return clientController.client.visitorID.getVal().intern();
		if (clientController.client.logedInSunscriber.getVal() != null)
			return clientController.client.logedInSunscriber.getVal().personalID;
//		if(clientController.client.logedInWorker.getVal() != null)
//			return clientController.client.logedInWorker.getVal().getWorkerID();
		return null;
	}
 
	private int checkIfSubscriberInDB(String id) {
		if (!id.contains("S"))
			id = "S" + id;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", id));
		if (!response.contains("was not found"))
			return 1;
		return 0;
	}
	private int isSubscriber() {
		if (clientController.client.logedInSunscriber.getVal() != null)
			return 1;
		return 0;
	}

	public void setSpontaneous(String ownerID, String parkName) {
		spontaneous = true;
		Park_ComboBox.setValue(parkName);
		VisitHour_ComboBox.setValue(LocalTime.now().withSecond(0).withNano(0).toString()); 
		Date_DatePicker.setValue(LocalDate.now()); // get the date of today
		// disable the fields, visitor don't have the choice of park,date and time
		Park_ComboBox.setDisable(true);
		VisitHour_ComboBox.setDisable(true);
		Date_DatePicker.setDisable(true);
		ord.ownerID = ownerID;
		ord.parkSite = parkName;
	}

	private ParkEntry createParkEntry(Order spOrder) {
		ParkEntry entry = new ParkEntry(ParkEntry.EntryType.Personal, spOrder.ownerID, spOrder.parkSite,
				spOrder.visitTime, null, spOrder.numberOfVisitors, spOrder.numberOfSubscribers, true,
				spOrder.priceOfOrder);
		return entry;
	}


	private Order createSpontaneousOrderDetails(String ownerID, String parkName) {
		int orderID = getNextOrderID();
		int priceOfOrder = 100;
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.IdType type = Order.IdType.PRIVATE; // by default
		Order.OrderStatus orderStatus = Order.OrderStatus.CONFIRMED;
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis());
		boolean isUsed = true;
		int numberOfSubscribers = checkIfSubscriberInDB(ownerID);
		Order ord = new Order(parkName, 1, orderID, priceOfOrder, email, phone, type, orderStatus, timeOfOrder,
				timeOfOrder, isUsed, ownerID, numberOfSubscribers);
		return ord;
	}

	private int getNextOrderID() {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "NextOrderID", null));
		return ServerRequest.gson.fromJson(response, Integer.class);
	}

}
