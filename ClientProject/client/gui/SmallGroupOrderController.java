package gui;

import java.time.LocalDate;

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

/** the SmallGroupOrder page controller */
public class SmallGroupOrderController implements GuiController {

	private int visitorsCounter = 1;

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

	@Override
	public void init() {
		Park_ComboBox.getItems().clear(); // for what? maybe not necessary
		Park_ComboBox.getItems().addAll("#1", "#2", "#3");
		VisitHour_ComboBox.getItems().clear(); // for what? maybe not necessary
		// every visit is about 4 hours so: if the park works from 8:00 to 16:00 the
		// last enter time should be 12:00 ?
		VisitHour_ComboBox.getItems().addAll("8:00", "9:00", "10:00", "11:00", "12:00");
		// CardTypeComboBox.getSelectionModel().select(0);
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

	@FXML
	void AddVisitor_Button_Clicked(ActionEvent event) { // could the visitor be for different parks? time? date ?
		if (CheckAllRequiredFields()) {
			System.out.println("Visitor Edded");
			String telephone = Phone_textBox.getText();
			// add to listViewVisitors
			listViewVisitors.getItems().add("visitor #" + visitorsCounter++ + " " + "(" + telephone + ")");
		} else
			System.out.println("Visitor not Edded");
	}

	@FXML
	void PlaceOrder_Button_Clicked(ActionEvent event) {

	}

	public void setSpontaneous(String ordererId) {
		// TODO Auto-generated method stub

	}

}
