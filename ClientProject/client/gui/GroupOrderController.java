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
import javafx.util.Callback;
import logic.OpenOrderTimes;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the GroupOrder page controller */
public class GroupOrderController implements GuiController {

	public Map<String, ParkNameAndTimes> openingTimes = clientController.client.openingTimes;
	public String[] parkNames = clientController.client.parkNames;

	Order ord = new Order();
	private ParkEntry parkEntry;
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
		VisitHour_ComboBox.getItems().clear();
		Park_ComboBox.getItems().addAll(parkNames);
		NumberOfVisitors_ComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
				"14", "15");
		PlaceOrder_Button.setDisable(false);
		if (!checkIfGuide()) {
			FamilyIndicator_checkBox.setSelected(true);
			FamilyIndicator_checkBox.setDisable(true);
		}
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
						LocalDate tomorrow = LocalDate.now().plusDays(1);
						;
						setDisable(empty || item.compareTo(tomorrow) < 0);
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

	private boolean checkIfGuide() {
		if (clientController.client.logedInSubscriber.getVal() != null)
			if (clientController.client.logedInSubscriber.getVal().type == Subscriber.Type.GUIDE)
				return true;
		return false;
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
			if (spontaneous == true) {
				// ord = createSpontaneousOrderDetails(ord.ownerID,ord.parkSite);
				parkEntry.numberOfVisitors = Integer.parseInt(NumberOfVisitors_ComboBox.getValue());
				MoveToTheNextPage(ord, parkEntry);
				return;
			}
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
//				PopUp.showInformation("No more orders allowed in this time", "No more orders allowed in this time",
//						"No more orders allowed in this time");   // another options will be displayed and offer to waiting list
				Order newSelectedOrder = OpenOrderTimes.askForWaitingListAndShowOptions(ord);
				if (newSelectedOrder == null)
					return;
				else
					MoveToTheNextPage(newSelectedOrder, null);
//					 response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Order,
//							"IsOrderAllowed", ServerRequest.gson.toJson(newSelectedOrder, Order.class)));
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
		checkIfGuideByIDAndSetFamilyCheckBox(ownerID);
//			FamilyIndicator_checkBox.setSelected(true);
//			FamilyIndicator_checkBox.setDisable(true);
//		}else {
//			FamilyIndicator_checkBox.setSelected(false);
//			FamilyIndicator_checkBox.setDisable(false);
//		}
		ord.email = Email_textBox.getText(); // need this for OrderSummary because is no email in ParkEntry entity
		ord.phone = Phone_textBox.getText(); // need this for OrderSummary because is no phone in ParkEntry entity
		parkEntry = createParkEntry(ownerID, parkName);
	}
	
	private void checkIfGuideByIDAndSetFamilyCheckBox(String id) {
		if (!id.contains("S"))
			id = "S" + id;
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Subscriber, "GetSubscriberData", id));
//		if (response.contains("was not found"))
//			//throw new Navigator.NavigationInterruption(); // it's don't help me match just throw exception
//			Navigator.instance().clearHistory(); // return to the main window
			
		Subscriber sub = ServerRequest.gson.fromJson(response, Subscriber.class);
		if(sub.type == Subscriber.Type.GUIDE) {
			FamilyIndicator_checkBox.setSelected(false);
			FamilyIndicator_checkBox.setDisable(false);
		}
		else {
			FamilyIndicator_checkBox.setSelected(true);
			FamilyIndicator_checkBox.setDisable(true);
		}
			
	}

	/* don't delete */
//	private Order createSpontaneousOrderDetails(String ownerID, String parkName) {
//		int orderID = getNextOrderID();
//		int priceOfOrder = 100;
//		int numberOfVisitors =Integer.parseInt(NumberOfVisitors_ComboBox.getValue());
//		String email = Email_textBox.getText();
//		String phone = Phone_textBox.getText();
//		Order.IdType type = Order.IdType.PRIVATEGROUP; // by default
//		Order.OrderStatus orderStatus = Order.OrderStatus.CONFIRMED;
//		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis());
//		boolean isUsed = true;
//		int numberOfSubscribers = 0;
//		Order ord = new Order(parkName, numberOfVisitors, orderID, priceOfOrder, email, phone, type, orderStatus,
//				timeOfOrder, timeOfOrder, isUsed, ownerID, numberOfSubscribers);
//		return ord;
//	}

	private int getNextOrderID() {
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "NextOrderID", null));
		return ServerRequest.gson.fromJson(response, Integer.class);
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
		int orderID = getNextOrderID();
		int priceOfOrder = 100; // for now, need to be calculated by other controller
		boolean isUsed = false; // by default
		Order.IdType type = familyOrGuideCheckBox();
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = getIdentificationString(); // TODO the real ownerID will be provided from previous page (popUp)
		int numberOfSubscribers = 0; // in a group order this is not relevant
		Order ord = new Order(parkName, numberOfVisitors, orderID, priceOfOrder, email, phone, type, orderStatus,
				visitTime, timeOfOrder, isUsed, ownerID, numberOfSubscribers);
		return ord;
	}

	private String getIdentificationString() {
		if (clientController.client.visitorID.getVal() != null) // TODO check if needed
			return clientController.client.visitorID.getVal().intern();
		if (clientController.client.logedInSubscriber.getVal() != null)
			return clientController.client.logedInSubscriber.getVal().personalID;
//		if(clientController.client.logedInWorker.getVal() != null)
//			return clientController.client.logedInWorker.getVal().getWorkerID();
		return null;
	}

	private Order.IdType familyOrGuideCheckBox() {
		if (FamilyIndicator_checkBox.isSelected())
			return Order.IdType.FAMILY;
		return Order.IdType.GUIDE;
	}

	private void MoveToTheNextPage(Order ord, ParkEntry parkEntry) {
		Navigator n = Navigator.instance();
		GuiController g = n.navigate("OrderSummary");
		((OrderSummaryController) g).addOrderDataToFields(ord, parkEntry);
	}

	private ParkEntry createParkEntry(String ownerID, String parkID) {
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis());
		int numberOfSubscribers = 0;
		int priceOfOrder = 100;
		ParkEntry entry = new ParkEntry(ParkEntry.EntryType.Group, ownerID, parkID, timeOfOrder, null, 1,
				numberOfSubscribers, true, priceOfOrder); // real number of visitor will be set later
		return entry;
	}

}
