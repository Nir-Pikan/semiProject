package gui;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import entities.Order;
import entities.ParkEntry;
import entities.ParkNameAndTimes;
import entities.Subscriber;
import io.clientController;
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
import logic.OpenOrderTimes;
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

	/**  //TODO edit ~nir~
	 * Initialize the GUI: limits the Date picker and set subscriber data if found //TODO
	 * edit ~nir~
	 * 
	 */
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
	/**  //TODO edit ~nir~
	 * Creates and returns String array with working hours of the park
	 * @param parkDetails
	 * @return
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
	/**  //TODO edit ~nir~
	 * When park was chosen set the visit hour comboBox to park working hours 
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

	/**  //TODO edit ~nir~
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

	/**  //TODO edit ~nir~
	 * Check that park site was chosen
	 * 
	 * @return true if field is not empty, false otherwise
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

	/**  //TODO edit ~nir~
	 * Check if date was selected Only relevant dates are can be chosen, no need to
	 * check if the date is relevant
	 * 
	 * @return return true if the field is not empty, false otherwise
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

	/**  //TODO edit ~nir~
	 * Check if hour of a visit was selected DONT CHECK IF THE HOUR IS NOT RELEVANT
	 * IF TODAY WAS CHOSEN
	 * 
	 * @return
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

	/**  //TODO edit ~nir~
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

	/**  //TODO edit ~nir~
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

	/**  //TODO edit ~nir~
	 * when Place Order button clicked checks if this order  can be booked or check capacity of the park in case of casual entry
	 * by connecting with the server. Get response from the server and show appropriate popUp window
	 * @param event
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
	/**  //TODO edit ~nir~
	 * Check if park is not full
	 * @param park
	 * @return
	 */
	private boolean checkParkCapacity(String park) {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Park, "get number of visitor available", park));
		int availableSpace = ServerRequest.gson.fromJson(response, Integer.class);
		if (availableSpace < 1)
			return false;
		return true;
	}
	
	/**  //TODO edit ~nir~
	 * Send the Order and Entry Entities to OrderSummary window
	 * @param ord (Order Entity)s
	 * @param parkEntry	
	 */
	private void MoveToTheNextPage(Order ord, ParkEntry entry) {
		Navigator n = Navigator.instance();
		GuiController g = n.navigate("OrderSummary");
		((OrderSummaryController) g).addOrderDataToFields(ord, entry);
	}

	/**  //TODO edit ~nir~
	 * Create Order Entity from the fields that was filled by the user on a GUI
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
	/**  //TODO edit ~nir~
	 * Returns the ID String of a visitor from the clientController
	 * @return
	 */
	private String getIdentificationString() {
		if (clientController.client.visitorID.getVal() != null)
			return clientController.client.visitorID.getVal().intern();
		if (clientController.client.logedInSubscriber.getVal() != null)
			return clientController.client.logedInSubscriber.getVal().personalID;
		return null;
	}

	/**  //TODO edit ~nir~
	 * Check if subscriber by ID or by logged visitor in the ClientController (one of them)
	 * and if subscriber found fill the email address and phone number in the GUI
	 * @param ID
	 * @return
	 */
	private int isSubscriber(String ID) {
		if (clientController.client.logedInSubscriber.getVal() != null) {
			Email_textBox.setText(clientController.client.logedInSubscriber.getVal().email);
			Phone_textBox.setText(clientController.client.logedInSubscriber.getVal().phone);
			return 1;
		}
		if(ID == null)
			return 0;
		if (!ID.contains("S"))
			ID = "S" + ID;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", ID));
		if (!response.contains("was not found")) {
			Subscriber sub = ServerRequest.gson.fromJson(response, Subscriber.class);
			Email_textBox.setText(sub.email);
			Phone_textBox.setText(sub.phone);
			return 1;
		}
		return 0;
	}
	/**  //TODO edit ~nir~
	 * This method called in a case of casual entry, disables park, visit time and date.
	 * Set park, visit time to current values
	 * @param ownerID
	 * @param parkName
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
	
	/** //TODO edit ~nir~
	 * Creates park entry ny ownerID and parkID
	 * @param ownerID
	 * @param parkID
	 * @return
	 */
	private ParkEntry createParkEntry(String ownerID, String parkID) {
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis());
		int numberOfSubscribers = isSubscriber(ownerID);
		ParkEntry entry = new ParkEntry(ParkEntry.EntryType.Personal, ownerID, parkID, timeOfOrder, null, 1,
				numberOfSubscribers, true, 100);
		entry.priceOfOrder = calcEntryPrice(entry);
		return entry;
	}
	/**  //TODO edit ~nir~
	 * return the price of Order by prices in DiscountController
	 * @param order
	 * @return
	 */
	protected static float calcOrderPrice(Order order) {
		String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Discount,
				"CalculatePriceForEntryByOrder", ServerRequest.gson.toJson(order, Order.class)));
		return ServerRequest.gson.fromJson(response, Float.class);
	}
	 
	/**  //TODO edit ~nir~
	 * return the price of Entry (casual) by prices in DiscountController
	 * @param order
	 * @return
	 */
	protected static float calcEntryPrice(ParkEntry entry) {
		String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Discount,
				"CalculatePriceForEntryCasual", ServerRequest.gson.toJson(entry, ParkEntry.class)));
		return ServerRequest.gson.fromJson(response, Float.class);
	}
	
	/**  //TODO edit ~nir~
	 * Ask DB for next Order number 
	 * @return
	 */
	private int getNextOrderID() {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "NextOrderID", null));
		return ServerRequest.gson.fromJson(response, Integer.class);
	}

}
