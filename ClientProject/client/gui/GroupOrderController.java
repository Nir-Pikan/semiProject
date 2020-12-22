package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

public class GroupOrderController implements GuiController {

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
		VisitHour_ComboBox.getItems().addAll("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30",
				"12:00"/* , "12:30", "13:00","13:30", "14:00", "14:30", "15:00", "15:30", "16:00" */);
		// CardTypeComboBox.getSelectionModel().select(0);
		NumberOfVisitors_ComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
				"14", "15");
		PlaceOrder_Button.setDisable(false); // why disabling the button by default??
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
		ParkNote.setText("*");
		return true;
	}

	private boolean CheckDateSelection() {
		if (Date_DatePicker.getValue() == null) {
			if (!Date_DatePicker.getStyleClass().contains("error")) // is it a CSS property that don't declared for date
																	// picker?
				Date_DatePicker.getStyleClass().add("error"); // is it a CSS property that don't declared for date
																// picker?
			DateNote.setText("* Please select date");
			return false;
		}
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
		if (CheckAllRequiredFields())
			System.out.println("Pass");
		else
			System.out.println("No Pass");
	}

	public void setSpontaneous(String ordererId) {
		// TODO Auto-generated method stub

	}

	public void setFamilyOrderOnly() {
		// TODO Auto-generated method stub

	}

}
