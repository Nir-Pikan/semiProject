package clientGui;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import clientIO.clientController;
import clientLogic.OpenOrderTimes;
import entities.Order;
import entities.ParkEntry;
import entities.ParkNameAndTimes;
import entities.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
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
		isSubscriber(null);
		// set only relevant dates
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate tomorrow = LocalDate.now().plusDays(1);
						;
						setDisable(empty || item.compareTo(tomorrow) < 0);
					}

				};
			}

		};
		Date_DatePicker.setDayCellFactory(callB);

	}

	/**
	 * Creates and returns String array with working hours of the {@link Park}
	 * 
	 * @param parkDetails the {@link Park}'s {@link ParkNameAndTimes}
	 * @return String array with working hours
	 */
	private String[] CreateWorkingHours(ParkNameAndTimes parkDetails) {
		VisitHour_ComboBox.getItems().clear();
		int numberOfWorkingHours = parkDetails.closeTime - parkDetails.openTime;
		String[] res = new String[numberOfWorkingHours];
		for (int i = parkDetails.openTime; i < parkDetails.closeTime; i++) {
			res[i - parkDetails.openTime] = i + ":00";
		}
		return res;
	}

	/**
	 * When park is chosen set the visit hour comboBox<br>
	 * according to the {@link Park}'s working hours
	 * 
	 * @param event
	 */
	@FXML
	void parkWasChosen(ActionEvent event) {
		ParkNameAndTimes temp = null;
		for (int i = 0; i < parkNames.length; i++)
			if (parkNames[i].equals(Park_ComboBox.getValue()))
				temp = openingTimes.get(parkNames[i]);
		VisitHour_ComboBox.getItems().addAll(CreateWorkingHours(temp));
	}

	/**
	 * Check if all the fields are filled correctly
	 * 
	 * @return true if all the fields are filled correctly<br>
	 *         false otherwise
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
	 * @return true if field is not empty<br>
	 *         false otherwise
	 */
	private boolean CheckParkSelection() {
		if (Park_ComboBox.getSelectionModel().isEmpty()) {
			if (!Park_ComboBox.getStyleClass().contains("error"))
				Park_ComboBox.getStyleClass().add("error");
			ParkSelectionNote.setText("* Select park");
			return false;
		}
		Park_ComboBox.getStyleClass().remove("error");
		ParkSelectionNote.setText("*");
		return true;
	}

	/**
	 * Check if date was selected<br>
	 * Only relevant dates can be chosen, no need to check if the date is relevant
	 * 
	 * @return true if the field is not empty<br>
	 *         false otherwise
	 */
	private boolean CheckDateSelection() {
		if (Date_DatePicker.getValue() == null) {
			if (!Date_DatePicker.getStyleClass().contains("error"))
				Date_DatePicker.getStyleClass().add("error");
			DateSelecionNote.setText("* Select date");
			return false;
		}
		Date_DatePicker.getStyleClass().remove("error");
		DateSelecionNote.setText("*");
		return true;
	}

	/**
	 * Check if hour of a visit was selected<br>
	 * DONT CHECK IF THE HOUR IS NOT RELEVANT IF TODAY WAS CHOSEN
	 * 
	 * @return true if the field is not empty<br>
	 *         false otherwise
	 */
	private boolean CheckVisitorHour() {
		if (VisitHour_ComboBox.getValue() == null) {
			if (!VisitHour_ComboBox.getStyleClass().contains("error"))
				VisitHour_ComboBox.getStyleClass().add("error");
			VisitorHourNote.setText("* Select hour");
			return false;
		}
		VisitHour_ComboBox.getStyleClass().remove("error");
		VisitorHourNote.setText("*");
		return true;
	}

	/**
	 * Check if the email address is filled in appropriate format
	 * 
	 * @return true if email address is filled in appropriate format<br>
	 *         false otherwise
	 */
	private boolean CheckEmail() {
		String email = Email_textBox.getText();

		// if field is empty
		if (email.equals("")) {
			if (!Email_textBox.getStyleClass().contains("error"))
				Email_textBox.getStyleClass().add("error");
			EmailNote.setText("* Enter Email");
			return false;
		}

		String emailFormat = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$";

		// if field is not in wanted format
		if (!email.matches(emailFormat)) {
			if (!Email_textBox.getStyleClass().contains("error"))
				Email_textBox.getStyleClass().add("error");
			EmailNote.setText("* Wrong Format");
			return false;
		}

		Email_textBox.getStyleClass().remove("error");
		EmailNote.setText("*");
		return true;
	}

	/**
	 * Check if the phone number is filled in appropriate format
	 * 
	 * @return true if phone number is filled in appropriate format<br>
	 *         false otherwise
	 */
	private boolean CheckPhoneNumber() {
		String phoneNumber = Phone_textBox.getText();

		// if field is empty
		if (phoneNumber.equals("")) {
			if (!Phone_textBox.getStyleClass().contains("error"))
				Phone_textBox.getStyleClass().add("error");
			PhoneNote.setText("* Enter Number");
			return false;
		}

		// if field contains a character that is not a digit
		if (!phoneNumber.matches("([0-9])+")) {
			if (!Phone_textBox.getStyleClass().contains("error"))
				Phone_textBox.getStyleClass().add("error");
			PhoneNote.setText("* Worng Format(?)");
			Tooltip.install(PhoneNote, new Tooltip("Phone Number can only contain digits"));
			return false;
		}

		// if field contains more or less than 10 digits
		if (phoneNumber.length() != 10) {
			if (!Phone_textBox.getStyleClass().contains("error"))
				Phone_textBox.getStyleClass().add("error");
			PhoneNote.setText("* Worng Format(?)");
			Tooltip.install(PhoneNote, new Tooltip("Phone Number must be 10 digits long"));
			return false;
		}

		Phone_textBox.getStyleClass().remove("error");
		PhoneNote.setText("*");
		Tooltip.uninstall(PhoneNote, new Tooltip("Phone Number can only contain digits"));
		Tooltip.uninstall(PhoneNote, new Tooltip("Phone Number must be 10 digits long"));
		return true;
	}

	/**
	 * when Place Order button clicked checks if this {@link Order} can be
	 * booked<br>
	 * or check capacity of the park in case of casual entry by connecting with the
	 * server.<br>
	 * Get response from the server and show appropriate popUp window
	 */
	@FXML
	void PlaceOrder_Button_Clicked(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			if (spontaneous == true) {
				if (!checkParkCapacity(parkEntry.parkID)) {
					PopUp.showInformation("Not enough space", "Not enough space", "Not enough space in the park");
					return;
				}
				ord.email = Email_textBox.getText();
				ord.phone = Phone_textBox.getText();
				MoveToTheNextPage(ord, parkEntry);
				return;
			}
			ord = createOrderDetails();
			String response = clientController.client.sendRequestAndResponse(
					new ServerRequest(Manager.Order, "IsOrderAllowed", ServerRequest.gson.toJson(ord, Order.class)));
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
				Order newSelectedOrder = OpenOrderTimes.askForWaitingListAndShowOptions(ord);
				if (newSelectedOrder == null)
					return;
				else
					MoveToTheNextPage(newSelectedOrder, null);
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

	/**
	 * Check if {@link Park} is not full
	 * 
	 * @param park the {@link Park} to check
	 * @return true if park IS NOT full<br>
	 *         false if park IS full
	 */
	private boolean checkParkCapacity(String park) {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Park, "get number of visitor available", park));
		int availableSpace = ServerRequest.gson.fromJson(response, Integer.class);
		if (availableSpace < 1)
			return false;
		return true;
	}

	/**
	 * Send the {@link Order} and {@link ParkEntry} to OrderSummary window
	 * 
	 * @param ord   the {@link Order} to send
	 * @param entry the {@link ParkEntry} to send
	 */
	private void MoveToTheNextPage(Order ord, ParkEntry entry) {
		Navigator n = Navigator.instance();
		GuiController g = n.navigate("OrderSummary");
		((OrderSummaryController) g).addOrderDataToFields(ord, entry);
	}

	/**
	 * Create {@link Order} using the fields that were filled<br>
	 * by the user on the GUI
	 * 
	 * @return the {@link Order} that was created
	 */
	private Order createOrderDetails() {
		String parkName = Park_ComboBox.getValue();
		LocalDate date = Date_DatePicker.getValue();
		int visitHour = Integer.parseInt(VisitHour_ComboBox.getValue().split(":")[0]);
		Timestamp visitTime = Timestamp.valueOf(date.atTime(LocalTime.of(visitHour, 0)));
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis()); // get the current time
		int numberOfVisitors = 1; // by default
		int orderID = getNextOrderID();
		boolean isUsed = false; // by default
		Order.IdType type = Order.IdType.PRIVATE; // by default
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = getIdentificationString();
		int numberOfSubscribers = isSubscriber(ownerID);
		Order ord = new Order(parkName, numberOfVisitors, orderID, 100, email, phone, type, orderStatus, visitTime,
				timeOfOrder, isUsed, ownerID, numberOfSubscribers);
		ord.priceOfOrder = calcOrderPrice(ord);
		return ord;
	}

	/**
	 * Returns the ID String of a visitor from the clientController
	 * 
	 * @return the visitor's ID if field is not empty<br>
	 *         null otherwise
	 */
	private String getIdentificationString() {
		if (clientController.client.visitorID.getVal() != null)
			return clientController.client.visitorID.getVal().intern();
		if (clientController.client.logedInSubscriber.getVal() != null)
			return clientController.client.logedInSubscriber.getVal().personalID;
		return null;
	}

	/**
	 * Check if {@link Subscriber} by ID or by logged visitor in the
	 * ClientController<br>
	 * (one of them) and if subscriber is found<br>
	 * fill the email address and phone number in the GUI
	 * 
	 * @param id the ID to check
	 * @return 1 if it is a subscriber<br>
	 *         0 if not a subscriber
	 */
	private int isSubscriber(String id) {
		if (clientController.client.logedInSubscriber.getVal() != null) {
			Email_textBox.setText(clientController.client.logedInSubscriber.getVal().email);
			Phone_textBox.setText(clientController.client.logedInSubscriber.getVal().phone);
			return 1;
		}
		if (id == null)
			return 0;
		if (!id.contains("S"))
			id = "S" + id;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", id));
		if (!response.contains("was not found")) {
			Subscriber sub = ServerRequest.gson.fromJson(response, Subscriber.class);
			Email_textBox.setText(sub.email);
			Phone_textBox.setText(sub.phone);
			return 1;
		}
		return 0;
	}

	/**
	 * This method called in case of casual entry.<br>
	 * disables park, visit time and date fields.<br>
	 * Set park and visit time fields to current values
	 * 
	 * @param ownerID  the{@link Order}'s owner ID
	 * @param parkName the {@link Order}'s {@link Park} name
	 */
	public void setSpontaneous(String ownerID, String parkName) {
		spontaneous = true;
		Park_ComboBox.setValue(parkName);
		VisitHour_ComboBox.setValue(LocalTime.now().withSecond(0).withNano(0).toString());
		Date_DatePicker.setValue(LocalDate.now()); // get the date of today
		// disable the fields, visitor don't have the choice of park,date and time
		Park_ComboBox.setDisable(true);
		VisitHour_ComboBox.setDisable(true);
		Date_DatePicker.setDisable(true);
		parkEntry = createParkEntry(ownerID, parkName);
	}

	/**
	 * Creates {@link ParkEntry} using ownerID and parkID
	 * 
	 * @param ownerID the {@link ParkEntry}'s owner ID
	 * @param parkID  the {@link ParkEntry}'s {@link Park} ID
	 * @return the created {@link ParkEntry}
	 */
	private ParkEntry createParkEntry(String ownerID, String parkID) {
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis());
		int numberOfSubscribers = isSubscriber(ownerID);
		ParkEntry entry = new ParkEntry(ParkEntry.EntryType.Personal, ownerID, parkID, timeOfOrder, null, 1,
				numberOfSubscribers, true, 100);
		entry.priceOfOrder = calcEntryPrice(entry);
		return entry;
	}

	/**
	 * calculate the price of {@link Order}<br>
	 * using prices in DiscountController
	 * 
	 * @param order the {@link Order} to calculate price for
	 * @return the DiscountController's response
	 */
	protected static float calcOrderPrice(Order order) {
		String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Discount,
				"CalculatePriceForEntryByOrder", ServerRequest.gson.toJson(order, Order.class)));
		return ServerRequest.gson.fromJson(response, Float.class);
	}

	/**
	 * calculate the price of {@link ParkEntry} (casual)<br>
	 * using prices in DiscountController
	 * 
	 * @param entry the {@link ParkEntry} to calculate price for
	 * @return the DiscountController's response
	 */
	protected static float calcEntryPrice(ParkEntry entry) {
		String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Discount,
				"CalculatePriceForEntryCasual", ServerRequest.gson.toJson(entry, ParkEntry.class)));
		return ServerRequest.gson.fromJson(response, Float.class);
	}

	/**
	 * Ask DB for next {@link Order} number
	 * 
	 * @return the server's response
	 */
	private int getNextOrderID() {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "NextOrderID", null));
		return ServerRequest.gson.fromJson(response, Integer.class);
	}

}
