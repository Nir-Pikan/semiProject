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
import javafx.scene.control.CheckBox;
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

/** the GroupOrder page controller */
public class GroupOrderController implements GuiController {

	public Map<String, ParkNameAndTimes> openingTimes = clientController.client.openingTimes;
	public String[] parkNames = clientController.client.parkNames;

	Order ord = new Order();
	private ParkEntry parkEntry;
	Subscriber sub;
	private boolean spontaneous = false;

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
	private Label EmailNote;

	@FXML
	private Label NumberOfVisitorsNote;

	@FXML
	private Label VisitHourNote;

	@FXML
	private Label DateNote;

	@FXML
	private Label ParkNote;

	/**
	 * Initialize the GUI: limits the Date picker and set group/family size //TODO  edit ~nir~
	 * and set subscriber data if found
	 * 
	 */
	@Override
	public void init() {
		Park_ComboBox.getItems().clear();
		VisitHour_ComboBox.getItems().clear();
		Park_ComboBox.getItems().addAll(parkNames);
		NumberOfVisitors_ComboBox.getItems().addAll("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
				"14", "15");
		PlaceOrder_Button.setDisable(false);
		checkIfGuide();
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
	 * Set the working hours by the park working times//TODO edit ~nir~
	 * 
	 * @param parkDetails
	 * @return the String[] with all the working hours
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
	 * When park was chosen set the working hours of the park//TODO edit ~nir~
	 * 
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
	 * Check if the visitor is a guide and set family checkBox and call to method
	 * that sets subscriber email and phone number//TODO edit ~nir~
	 * 
	 */
	private void checkIfGuide() {
		if (clientController.client.logedInSubscriber.getVal() != null)
			if (clientController.client.logedInSubscriber.getVal().type == Subscriber.Type.SUBSCRIBER) {
				if (clientController.client.logedInSubscriber.getVal().familySize < 2) {
					PopUp.showInformation("Family order error", "Family order error", "You cant create family order");
					throw new Navigator.NavigationInterruption();
				}
				FamilyIndicator_checkBox.setSelected(true);
				FamilyIndicator_checkBox.setDisable(true);
				setFamilyDropBox();
			} else if (clientController.client.logedInSubscriber.getVal().familySize < 2) {
				FamilyIndicator_checkBox.setSelected(false);
				FamilyIndicator_checkBox.setDisable(true);
			}
		initSubscriberPhoneAndMail();
	}

	/**
	 * Sets subscriber email and phone number in the window//TODO edit ~nir~
	 * 
	 */
	private void initSubscriberPhoneAndMail() {
		String subscriberID;
		if (clientController.client.logedInSubscriber.getVal() != null) {
			subscriberID = clientController.client.logedInSubscriber.getVal().subscriberID;
			String response = clientController.client
					.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", subscriberID));
			Subscriber tempSub = ServerRequest.gson.fromJson(response, Subscriber.class);
			Email_textBox.setText(tempSub.email);
			Phone_textBox.setText(tempSub.phone);
		}
	}

	/**
	 * When family checkBox clicked set appropriate size of group or family in the
	 * dropBox//TODO edit ~nir~
	 * 
	 */
	@FXML
	private void FamilyCheckBoxClicked(ActionEvent event) {
		NumberOfVisitors_ComboBox.getItems().clear();
		if (FamilyIndicator_checkBox.isSelected()) {
			setFamilyDropBox();
		} else {
			NumberOfVisitors_ComboBox.getItems().addAll("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
					"14", "15");
		}
	}

	/**
	 * Set the family size dropBox by the number of subscriber family size //TODO edit ~nir~
	 * 
	 * 
	 */
	private void setFamilyDropBox() {
		int familySize;
		NumberOfVisitors_ComboBox.getItems().clear();
		if (clientController.client.logedInSubscriber.getVal() != null)
			familySize = clientController.client.logedInSubscriber.getVal().familySize;
		else
			familySize = sub.familySize;
		for (int i = 2; i <= familySize; i++)
			NumberOfVisitors_ComboBox.getItems().add("" + i);
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
			ParkNote.setText("* Select park");
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
			DateNote.setText("* Select date");
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
			VisitHourNote.setText("* Select hour");
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
			NumberOfVisitorsNote.setText("* Select number");
			return false;
		}
		NumberOfVisitors_ComboBox.getStyleClass().remove("error");
		NumberOfVisitorsNote.setText("*");
		return true;
	}

	/**
	 * Check if the email address is filled in appropriate form //TODO edit ~nir~
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

	/**
	 * Check if the phone number is filled in appropriate form  //TODO edit ~nir~
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
	 * When Place Order button clicked check if all the fields filled correctly.
	 * Create Order Entity or parkEntry Entity and 
	 * contact with the server to check if Order or Casual entry  is available.
	 * Get server response and shows appropriate popUp window 
	 * @param event
	 */
	@FXML
	void PlaceOrder_Button_Clicked(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			if (spontaneous == true) {
				parkEntry.numberOfVisitors = Integer.parseInt(NumberOfVisitors_ComboBox.getValue());
				if (!checkAvailableSpaceInThePark(parkEntry.parkID, parkEntry.numberOfVisitors)) {
					PopUp.showInformation("Not enough space", "Not enough space", "Not enough space in the park");
					return;
				}
				if (parkEntry.entryType == ParkEntry.EntryType.Subscriber)
					parkEntry.numberOfSubscribers = parkEntry.numberOfVisitors;
				ord.email = Email_textBox.getText();
				ord.phone = Phone_textBox.getText();
				parkEntry.priceOfOrder = RegularOrderController.calcEntryPrice(parkEntry);
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
	 *   //TODO edit ~nir~
	 * Check available space in the park 
	 * @param park
	 * @param visitorsNum
	 * @return true if the park have enough free space, otherwise false
	 */
	private boolean checkAvailableSpaceInThePark(String park, int visitorsNum) {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Park, "get number of visitor available", park));
		int availableSpace = ServerRequest.gson.fromJson(response, Integer.class);
		if (visitorsNum > availableSpace)
			return false;
		return true;
	}
	
	/**  //TODO edit ~nir~
	 * This method called from casual entry window.
	 * Disables park and visit hour comboBox and set visit time to current time
	 * Creates a parkEntry Entity by ownerID parkName by calling createParkEntry method
	 * @param ownerID
	 * @param parkName
	 */
	public void setSpontaneous(String ownerID, String parkName) {
		spontaneous = true;
		Park_ComboBox.setValue(parkName);
		VisitHour_ComboBox.setValue(LocalTime.now().withSecond(0).withNano(0).toString());
		ord.ownerID = ownerID;
		ord.parkSite = parkName;
		Date_DatePicker.setValue(LocalDate.now()); // get the date of today
		// disable the fields, visitor don't have the choice of park,date and time
		Park_ComboBox.setDisable(true);
		VisitHour_ComboBox.setDisable(true);
		Date_DatePicker.setDisable(true);
		ParkEntry.EntryType entryType = checkIfGuideByIDAndSetFamilyCheckBox(ownerID);
		parkEntry = createParkEntry(ownerID, parkName, entryType);
	}

	/**  //TODO edit ~nir~
	 * Set a family box to selected or not selected mode according to subscriber or guide type and number of family members
	 * @param id
	 * @return the type of a subscriber guide or family
	 */
	private ParkEntry.EntryType checkIfGuideByIDAndSetFamilyCheckBox(String id) {
		if (!id.contains("S"))
			id = "S" + id;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", id));
		sub = ServerRequest.gson.fromJson(response, Subscriber.class);
		Email_textBox.setText(sub.email);
		Phone_textBox.setText(sub.phone);
		if (sub.type == Subscriber.Type.SUBSCRIBER) {
			if (sub.familySize < 2) {
				PopUp.showInformation("Family order error", "Family order error", "You cant create family order");
				Navigator.instance().back();
			}
			FamilyIndicator_checkBox.setSelected(true);
			FamilyIndicator_checkBox.setDisable(true);
			setFamilyDropBox();
			return ParkEntry.EntryType.Subscriber;
		} else if (sub.familySize < 2) {
			FamilyIndicator_checkBox.setSelected(false);
			FamilyIndicator_checkBox.setDisable(true);
		}
		return ParkEntry.EntryType.Group;

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
	/**  //TODO edit ~nir~
	 * Creates Order Entity details by filled data in the window and type of a subscriber
	 * @return Order Entity
	 */
	private Order createOrderDetails() {
		String parkName = Park_ComboBox.getValue();
		LocalDate date = Date_DatePicker.getValue();
		int visitHour = Integer.parseInt(VisitHour_ComboBox.getValue().split(":")[0]);
		Timestamp visitTime = Timestamp.valueOf(date.atTime(LocalTime.of(visitHour, 0)));
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis()); // get the current time
		int numberOfVisitors = Integer.parseInt(NumberOfVisitors_ComboBox.getValue());
		int orderID = getNextOrderID();
		boolean isUsed = false; // by default
		Order.IdType type = familyOrGuideCheckBox();
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = getIdentificationString();
		int numberOfSubscribers = type == Order.IdType.FAMILY ? numberOfVisitors : 0;
		Order ord = new Order(parkName, numberOfVisitors, orderID, 100, email, phone, type, orderStatus, visitTime,
				timeOfOrder, isUsed, ownerID, numberOfSubscribers);
		ord.priceOfOrder = RegularOrderController.calcOrderPrice(ord);
		return ord;
	}
	/**  //TODO edit ~nir~
	 * Returns Identification ID String from logged subscriber in cliendControler
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
	 * Check and return the type of subscriber, Family or guide
	 * @return
	 */
	private Order.IdType familyOrGuideCheckBox() {
		if (FamilyIndicator_checkBox.isSelected())
			return Order.IdType.FAMILY;
		return Order.IdType.GUIDE;
	}
	/**  //TODO edit ~nir~
	 * Send the Order and Entry Entities to OrderSummary window
	 * @param ord (Order Entity)s
	 * @param parkEntry	
	 */
	private void MoveToTheNextPage(Order ord, ParkEntry parkEntry) {
		Navigator n = Navigator.instance();
		GuiController g = n.navigate("OrderSummary");
		((OrderSummaryController) g).addOrderDataToFields(ord, parkEntry);
	}
	/**  //TODO edit ~nir~
	 * Creates parkEntry Entity by current time, ownerID, parkID and subscriber type
	 * @param ownerID
	 * @param parkID
	 * @param type
	 * @return
	 */
	private ParkEntry createParkEntry(String ownerID, String parkID, ParkEntry.EntryType type) {
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis());
		int numberOfSubscribers = 0;
		ParkEntry entry = new ParkEntry(type, ownerID, parkID, timeOfOrder, null, 1, numberOfSubscribers, true, 100); 
		return entry;
	}

}
