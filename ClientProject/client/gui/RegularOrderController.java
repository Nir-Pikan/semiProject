package gui;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import com.google.gson.Gson;

import entities.Order;
import entities.Subscriber;
import entities.Order.IdType;
import entities.Order.OrderStatus;
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
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

public class RegularOrderController implements GuiController {
	// the hours when the parks is working
	private int openingHour = 8;
	private int closeHour = 16;

	private Order o;
	private int i = -1;

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
		Park_ComboBox.getItems().addAll("#1", "#2", "#3"); // TODO give a names from a DB (Roman) exist in a client

		VisitHour_ComboBox.getItems().clear();
		// every visit is about 4 hours so: if the park works from 8:00 to 16:00 the
		// last enter time should be 12:00 ?
		VisitHour_ComboBox.getItems().addAll("8:00", "9:00", "10:00", "11:00", "12:00");
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

	// TODO error length visual
	private boolean CheckAllRequiredFields() {
		boolean res = true;
		res &= CheckParkSelection();
		res &= CheckDateSelection();
		res &= CheckVisitorHour();
		res &= CheckEmail();
		res &= CheckPhoneNumber();
		return res;
	}

	private boolean CheckParkSelection() {
		if (Park_ComboBox.getSelectionModel().isEmpty()) {
			if (!Park_ComboBox.getStyleClass().contains("error"))
				Park_ComboBox.getStyleClass().add("error");
			ParkSelectionNote.setText("* Please choose park");
			return false;
		}
		ParkSelectionNote.setText("*");
		return true;
	}

	private boolean CheckDateSelection() {
		if (Date_DatePicker.getValue() == null) {
			if (!Date_DatePicker.getStyleClass().contains("error")) // is it a CSS property that don't declared for date
																	// picker?
				Date_DatePicker.getStyleClass().add("error"); // is it a CSS property that don't declared for date
																// picker?
			DateSelecionNote.setText("* Please select date");
			return false;
		} else if (Date_DatePicker.getValue().isBefore(LocalDate.now())) { // delete THIS
			if (!Date_DatePicker.getStyleClass().contains("error"))

				Date_DatePicker.getStyleClass().add("error");

			DateSelecionNote.setText("* Date is to early");
			return false;
		}

		DateSelecionNote.setText("*");
		return true;
	}

	private boolean CheckVisitorHour() {
		if (VisitHour_ComboBox.getValue() == null) {
			if (!VisitHour_ComboBox.getStyleClass().contains("error"))
				VisitHour_ComboBox.getStyleClass().add("error");
			VisitorHourNote.setText("* Please select hour");
			return false;
		}
		VisitorHourNote.setText("*");
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
		if (CheckAllRequiredFields())
			System.out.println("Pass");
		else
			System.out.println("No Pass");

		o = createOrderDetails();
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


//		Timestamp startAndEnd[] = new Timestamp[2];
//		Date start = new Date(2020, 12, 30, 9, 0, 0);
//		Date end = new Date(2020, 12, 31, 10, 0, 0);
//		long startLong = start.getTime();
//		long endLong = end.getTime();
//		startAndEnd[0] = new Timestamp(startLong);
//		startAndEnd[1] = new Timestamp(endLong);
		// "2020-12-30 09:00:00.0" && visitTime < "2020-12-31 10:00:00.0"

//		Timestamp start = new Timestamp(2020 - 1900, 11, 30, 9, 0, 0, 0);
//		Timestamp end = new Timestamp(2020 - 1900, 11, 31, 10, 0, 0, 0);
//		String parkSite = "#2";
//		String startTimeAndEndTimeAndParkSite[] = new String[3];
//		startTimeAndEndTimeAndParkSite[0] = ServerRequest.gson.toJson(start, Timestamp.class);
//		startTimeAndEndTimeAndParkSite[1] = ServerRequest.gson.toJson(end, Timestamp.class);
//		startTimeAndEndTimeAndParkSite[2] = parkSite;
//		String response = clientController.client
//				.sendRequestAndResponse(new ServerRequest(Manager.Order, "GetAllListOfOrders", null));
//		Order[] res = ServerRequest.gson.fromJson(response, Order[].class);
//		if (res == null) {
//			System.out.println("no data");
////			System.out.println(start + "this the start time");
////			System.out.println(end + "this is the end time");
//		} else {
//			for (Order i : res) {
//				System.out.println(i.orderID);
//			//	System.out.println("some data");
//			}
//		}

//		String response = clientController.client.sendRequestAndResponse(
//				new ServerRequest(Manager.Order, "AddNewOrder", ServerRequest.gson.toJson(o, Order.class)));
//		String response = clientController.client
//				.sendRequestAndResponse(new ServerRequest(Manager.Order, "GetOrderByVisitorID", "323533744"));

//		public Order(String parkSite, int numberOfVisitors, long orderID, float priceOfOrder, String email, String phone,
//				IdType type, OrderStatus orderSttatus, Timestamp visitTime, Timestamp timeOfOrder, boolean isUsed) {

//		
		// write into the DB
		String response = clientController.client.sendRequestAndResponse(
				new ServerRequest(Manager.Order, "AddNewOrder", ServerRequest.gson.toJson(o, Order.class)));
//		i++;
//		if(i>0)
//		{

//		System.out.println(oTest.parkSite);
//		System.out.println(oTest.numberOfVisitors);
//		System.out.println(oTest.orderID);
//		System.out.println(oTest.priceOfOrder);
//		System.out.println(oTest.email);
//		System.out.println(oTest.phone);
//		System.out.println(oTest.type);
//		System.out.println(oTest.orderStatus);
//		System.out.println(oTest.visitTime);
//		System.out.println(oTest.timeOfOrder);
//		System.out.println(oTest.isUsed);
//		}
		
//		o.orderID = 3;
//		String response = clientController.client.sendRequestAndResponse(
//				new ServerRequest(Manager.Order, "UpdateOrder", ServerRequest.gson.toJson(o, Order.class)));

		switch (response) {
		case "Order was added successfully":
			PopUp.showInformation("Order placed success", "Order placed success",
					"Order placed successfully!\n" + "Your Order ID is:\n" + o.orderID);

			// after registration return to home page and forget the history
			// Navigator.instance().clearHistory(); // go to the summary page
			// !!!!!!!!!!!!!!!!
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
		case "No more orders allwed in this time":
			PopUp.showInformation("The park is full at this time and date", "The park is full at this time and date",
					"The park is full at this time and date");
		case "Owner with this ID is not found":
			PopUp.showInformation("Owner not exists", "Owner not exists!", "Owner with this ID not exists");
			break;
		case "Failed to cancel an order":
			PopUp.showInformation("Failed to cancel an order", "Failed to cancel an order",
					"Failed to cancel an order");
			break;
		case "Order Canceled":
			PopUp.showInformation("Order Canceled", "Order Canceled", "Order Canceled");
			break;
		case "Order seted as used":
			PopUp.showInformation("Order seted as used", "Order seted as used", "Order seted as used");
			break;
		case "Failed to set order as used":
			PopUp.showInformation("Failed to set order as used", "Failed to set order as used", "Failed to set order as used");
		break;
		case "Order updated":
			PopUp.showInformation("Order updated", "Order updated", "Order updated");
			break;
		case "Failed to update order":
			PopUp.showInformation("Failed to update order", "Failed to update order", "Failed to update order");
			break;
		case "Error: No such job":
			PopUp.showInformation("Unexpected error", "Unexpected error!", "server returned an unexpected response");
		}

		/*
		 * use later //send request to add order to clientController String response =
		 * clientController.client.sendRequestAndResponse(new ServerRequest(
		 * Manager.Order, "AddNewOrder" , ServerRequest.gson.toJson(o,Order.class)));
		 */
	}

	/**
	 * get Timestamp that want to change and add (or sub) the hours we send but
	 * still in range of working hours
	 * 
	 * @param stamp the Timestamp we want to change
	 * @param hours
	 * @return
	 */
	private Timestamp addTimeInHours(Timestamp stamp, int hours) {
		int tempHours = stamp.getHours() + hours;
		if (tempHours < openingHour)
			stamp.setHours(8);
		else if (tempHours > closeHour)
			stamp.setHours(16);
		else
			stamp.setHours(stamp.getHours() + hours);
		return stamp;
	}

	// no need enymore if the function above (addTimeInHours) works
//	private Timestamp SubtractThreeHours(Timestamp stamp) {
//		long current = stamp.getTime();
//		long substracted = current - 180 * 60 * 1000;
//		return new Timestamp(substracted);
//	}
//
//	private Timestamp AddFourHours(Timestamp stamp) {
//		long current = stamp.getTime();
//		long increased = current + 240 * 60 * 1000;
//		return new Timestamp(increased);
//	}

	private Order createOrderDetails() {
		String parkName = Park_ComboBox.getValue();
		LocalDate date = Date_DatePicker.getValue();
		int visitHour = Integer.parseInt(VisitHour_ComboBox.getValue().split(":")[0]);
		Timestamp visitTime = Timestamp.valueOf(date.atTime(LocalTime.of(visitHour, 0)));
		Timestamp timeOfOrder = new Timestamp(System.currentTimeMillis()); // get the current time
		int numberOfVisitors = 1; // by default
		String response = clientController.client
				.sendRequestAndResponse(new ServerRequest(Manager.Order, "NextOrderID", null));
		int orderID = ServerRequest.gson.fromJson(response, Integer.class);
		int priceOfOrder = 100; // for now
		boolean isUsed = false; // by default
		Order.IdType type = Order.IdType.PRIVATE; // by default
		String email = Email_textBox.getText();
		String phone = Phone_textBox.getText();
		Order.OrderStatus orderStatus = Order.OrderStatus.IDLE; // default status of order before some changes
		String ownerID = "323533745";
		Order o = new Order(parkName, numberOfVisitors, orderID, priceOfOrder, email, phone, type, orderStatus,
				visitTime, timeOfOrder, isUsed, ownerID);
		return o;
	}

	/*
	 * System.out.println(o.parkSite); System.out.println(o.numberOfVisitors);
	 * System.out.println(o.orderID); System.out.println(o.priceOfOrder);
	 * System.out.println(o.email); System.out.println(o.phone);
	 * System.out.println(o.type); System.out.println(o.orderStatus);
	 * System.out.println(o.visitTime); System.out.println(o.timeOfOrder);
	 * System.out.println(o.isUsed);
	 */
// how when and why???????????????????????????????????????????????? where is the button????????????????????????????
	public void setSpontaneous(String ordererId) {
		// TODO Auto-generated method stub

	}

}
