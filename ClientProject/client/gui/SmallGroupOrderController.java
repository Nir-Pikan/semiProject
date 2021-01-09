package gui;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Order;
import entities.ParkEntry;
import entities.ParkNameAndTimes;
import entities.Subscriber;
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
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import logic.OpenOrderTimes;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the SmallGroupOrder page controller */
public class SmallGroupOrderController implements GuiController {

	public Map<String, ParkNameAndTimes> openingTimes = clientController.client.openingTimes;
	public String[] parkNames = clientController.client.parkNames;

	private int visitorsCounter = 1;
	private List<String> visitorsIDArray = new ArrayList<String>();
	ObservableList<String> stringList;
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
		Park_ComboBox.getItems().clear();
		VisitHour_ComboBox.getItems().clear();
		Park_ComboBox.getItems().addAll(parkNames);
		String IdentificationID = getIdentificationString();
		initSubscriberData(IdentificationID);
		stringList = FXCollections.observableArrayList(visitorsIDArray);
		listViewVisitors.setItems(stringList);
		if (IdentificationID != null) {
			listViewVisitors.getItems().add("visitor #" + visitorsCounter + " " + "(" + IdentificationID + ")");
			visitorsIDArray.add(IdentificationID);
		}

		// set only relevant dates
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate tomorrow = LocalDate.now().plusDays(1);
						setDisable(empty || item.compareTo(tomorrow) < 0);
					}

				};
			}

		};
		Date_DatePicker.setDayCellFactory(callB);
	}

	/**
	 * Create and return array of working hours by {@link Park}'s working hours
	 * 
	 * @param parkDetails the {@link Park}'s {@link ParkNameAndTimes}
	 * @return array of {@link Park}'s working hours
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
	 * When {@link Park} is chosen initialize the working hours of this {@link Park}
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
	 * Check if all the fields were filled correctly
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
	 * @return true if field is not empty<br>
	 *         false otherwise
	 */
	private boolean CheckParkSelection() {
		if (Park_ComboBox.getSelectionModel().isEmpty()) {
			if (!Park_ComboBox.getStyleClass().contains("error"))
				Park_ComboBox.getStyleClass().add("error");
			ParkNote.setText("* Select park");
			return false;
		}
		Park_ComboBox.getStyleClass().remove("error");
		ParkNote.setText("*");
		return true;
	}

	/**
	 * Check if date was selected<br>
	 * Only relevant dates can be chosen<br>
	 * no need to check if the date is relevant
	 * 
	 * @return return true if the field is not empty<br>
	 *         false otherwise
	 */
	private boolean CheckDateSelection() {
		if (Date_DatePicker.getValue() == null) {
			if (!Date_DatePicker.getStyleClass().contains("error"))
				Date_DatePicker.getStyleClass().add("error");
			DateNote.setText("* Select date");
			return false;
		}
		Date_DatePicker.getStyleClass().remove("error");
		DateNote.setText("*");
		return true;
	}

	/**
	 * Check if the email address is filled in appropriate format
	 * 
	 * @return true if email address was filled in appropriate format<br>
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
	 * @return return true if id phone number filled in appropriate format<br>
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
	 * Check if hour of visit was selected<br>
	 * DONT CHECK IF THE HOUR IS NOT RELEVANT IF TODAY WAS CHOSEN
	 * 
	 * @return true if hour was selected<br>
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
	 * When Add Visitor button is clicked<br>
	 * the method checks if the ID is entered properly<br>
	 * and adds the ID to a ListView.<br>
	 * The ID of the owner cannot be deleted.
	 */
	@FXML
	void AddVisitor_Button_Clicked(ActionEvent event) { // could the visitor be for different parks? time? date ?
		String ordererId = PopUp.getUserInput("Private Group Order", "Enter ID of the visitor", "ID or Subscriber ID");

		if (!CheckID(ordererId)) {
			PopUp.showInformation("Please enter appropriate ID", "Please enter appropriate ID",
					"Please enter appropriate ID");
		} else if (visitorsIDArray.contains(ordererId) || visitorsIDArray.contains("S" + ordererId)
				|| visitorsIDArray.contains(ordererId.substring(1, ordererId.length()))) {
			PopUp.showInformation("This ID already is added", "This ID already is added", "This ID already is added");
		} else {
			visitorsIDArray.add(ordererId);
			PopUp.showInformation("Visitor Added", "Visitor Added", "Visitor Added");
			visitorsCounter++;
			listViewVisitors.getItems().add("visitor #" + visitorsCounter + " " + "(" + ordererId + ")");
			PlaceOrder_Button.setDisable(false);
			RemoveVisitor_Button.setDisable(false);
		}
	}

	/**
	 * When Remove Visitor button is clicked<br>
	 * remove visitors ID from ListView<br>
	 * The ID of the owner cannot be deleted.
	 */
	@FXML
	void RemoveVisitor_Button_Clicked(ActionEvent event) {
		int index = listViewVisitors.getSelectionModel().getSelectedIndex();
		if (index == -1 || index == 0) {
			PopUp.showInformation("Visitor remove", "Visitor remove", "Please select one of the additional visitor");
			return;
		}
		if (listViewVisitors.getItems().size() == 2) {
			PlaceOrder_Button.setDisable(true);
			RemoveVisitor_Button.setDisable(true);
		}
		stringList.remove(listViewVisitors.getSelectionModel().getSelectedItem());
		visitorsIDArray.remove(index);
		visitorsCounter--;
	}

	/**
	 * Checks if the ID is a 9 digits number or<br>
	 * S + 9 digits number
	 * 
	 * @param id the ID to be checked
	 * @return true if ID is in right format<br>
	 *         false otherwise
	 */
	private boolean CheckID(String id) {
		if ((!id.matches("([0-9])+") || id.length() != 9) && (!id.matches("S([0-9])+") || id.length() != 10)) {
			return false;
		}
		return true;
	}

	/**
	 * When Place Order button is clicked<br>
	 * the method checks if the {@link Order} can be booked or<br>
	 * casual entry is available by contacting with the server.<br>
	 * Shows appropriate popUp window according to the answer of the server
	 */
	@FXML
	void PlaceOrder_Button_Clicked(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			if (spontaneous == true) {
				parkEntry.numberOfVisitors = visitorsCounter;
				if (!checkParkCapacity(parkEntry.parkID, parkEntry.numberOfVisitors)) {
					PopUp.showInformation("Not enough space", "Not enough space", "Not enough space in the park");
					return;
				}
				parkEntry.numberOfSubscribers = NumberOfSubscribers();
				parkEntry.priceOfOrder = RegularOrderController.calcEntryPrice(parkEntry);
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
				MoveToTheNextPage(ord, null); // if its preorder entry = null
				break;
			case "Error: No such job":
				PopUp.showInformation("Unexpected error", "Unexpected error!",
						"server returned an unexpected response");
			}
		}

	}

	/**
	 * Check if there is enough space in a {@link Park} for casual entry
	 * 
	 * @param park        the {@link Park} id to check
	 * @param visitorsNum the amounts of visitors to check for
	 * @return true if there is enough free space<br>
	 *         false otherwise
	 */
	private boolean checkParkCapacity(String park, int visitorsNum) {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Park, "get number of visitor available", park));
		int availableSpace = ServerRequest.gson.fromJson(response, Integer.class);
		if (visitorsNum > availableSpace)
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
	 * Creates {@link Order} using the data that was entered from GUI
	 * 
	 * @return the created {@link Order}
	 */
	private Order createOrderDetails() {
		String parkName = Park_ComboBox.getValue();
		LocalDate date = Date_DatePicker.getValue();
		int visitHour = Integer.parseInt(VisitHour_ComboBox.getValue().split(":")[0]);
		Timestamp visitTime = Timestamp.valueOf(date.atTime(LocalTime.of(visitHour, 0)));
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis()); // get the current time
		int numberOfVisitors = visitorsCounter;
		int orderID = getNextOrderID();
		boolean isUsed = false; // by default
		Order.IdType type = Order.IdType.PRIVATEGROUP;
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = getIdentificationString();
		int numberOfSubscribers = NumberOfSubscribers();
		Order ord = new Order(parkName, numberOfVisitors, orderID, 100, email, phone, type, orderStatus, visitTime,
				timeOfOrder, isUsed, ownerID, numberOfSubscribers);
		ord.priceOfOrder = RegularOrderController.calcOrderPrice(ord);
		return ord;
	}

	/**
	 * Return ID of logged visitor form ClientController
	 * 
	 * @return the logged in visitor's ID if success<br>
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
	 * For each ID that was added, checks if the ID is {@link Subscriber}.<br>
	 * (no matters if S was is a first char)
	 * 
	 * @return the amount of {@link Subscriber}s
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

	/**
	 * This method called from casual entry window.<br>
	 * Disables park and visit hour comboBox and sets visit time to current time<br>
	 * Creates a {@link ParkEntry} by calling createParkEntry method
	 * 
	 * @param ownerID  the {@link ParkEntry}'s owner ID
	 * @param parkName the {@link ParkEntry}'s {@link Park} name
	 */
	public void setSpontaneous(String ownerID, String parkName) {
		spontaneous = true;
		visitorsIDArray.add(ownerID);
		listViewVisitors.getItems().add("visitor #" + visitorsCounter + " " + "(" + ownerID + ")");
		Park_ComboBox.setValue(parkName);
		VisitHour_ComboBox.setValue(LocalTime.now().withSecond(0).withNano(0).toString());
		ord.ownerID = ownerID;
		ord.parkSite = parkName;
		Date_DatePicker.setValue(LocalDate.now()); // get the date of today
		// disable the fields, visitor don't have the choice of park,date and time
		Park_ComboBox.setDisable(true);
		VisitHour_ComboBox.setDisable(true);
		Date_DatePicker.setDisable(true);
		initSubscriberData(ownerID);
		parkEntry = createParkEntry(ownerID, parkName);

	}

	/**
	 * Fill Email and Phone number of a {@link Subscriber} if found
	 * 
	 * @param id the {@link Subscriber}'s ID
	 */
	private void initSubscriberData(String id) {
		if (clientController.client.logedInSubscriber.getVal() != null) {
			Email_textBox.setText(clientController.client.logedInSubscriber.getVal().email);
			Phone_textBox.setText(clientController.client.logedInSubscriber.getVal().phone);
		}
		if (id == null)
			return;
		if (!id.contains("S"))
			id = "S" + id;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", id));
		if (!response.contains("was not found")) {
			Subscriber tempSub = ServerRequest.gson.fromJson(response, Subscriber.class);
			Email_textBox.setText(tempSub.email);
			Phone_textBox.setText(tempSub.phone);
		}
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
		int numberOfSubscribers = NumberOfSubscribers();
		ParkEntry entry = new ParkEntry(ParkEntry.EntryType.PrivateGroup, ownerID, parkID, timeOfOrder, null,
				visitorsCounter, numberOfSubscribers, true, 100);
		return entry;
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
