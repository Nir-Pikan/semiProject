package clientGui;

import entities.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

/** the RegisterCommonDetails page controller */
public class RegisterCommonDetailsController implements GuiController {

	private static final int MAX_FAMILY_SIZE = 20;

	@FXML
	private Label LastNameLabel;

	@FXML
	private TextField FirstNameTextField;

	@FXML
	private TextField LastNameTextField;

	@FXML
	private Label CommonDetailsTitleLabel;

	@FXML
	private Label FirstNameLabel;

	@FXML
	private Label IDLabel;

	@FXML
	private Label PhoneNumberLabel;

	@FXML
	private Label EmailLabel;

	@FXML
	private Label TypeLabel;

	@FXML
	private Label CreditCardLabel;

	@FXML
	private TextField IDTextField;

	@FXML
	private TextField PhoneNumberTextField;

	@FXML
	private TextField EmailTextField;

	@FXML
	private Label RequiredLabel;

	@FXML
	private Label FirstNameNote;

	@FXML
	private Label LastNameNote;

	@FXML
	private Label IDNote;

	@FXML
	private Label PhoneNumberNote;

	@FXML
	private Label EmailNote;

	@FXML
	private CheckBox CreditCardCheckBox;

	@FXML
	private RadioButton SubscriberRadionBtn;

	@FXML
	private RadioButton GuideRadioBtn;

	@FXML
	private Label FamilySizeLabel;

	@FXML
	private Spinner<Integer> FamilySizeSpinBox;

	@FXML
	private Button NextBtn;

	@FXML
	private Label DetailsTimelineLabel;

	@FXML
	private Label AddCreditCardTimelineLabel;

	@FXML
	private Label SummaryTimelineLabel;

	@Override
	public void init() {
		// Configure the FamilySizeSpinBox
		SpinnerValueFactory<Integer> familySizeFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
				MAX_FAMILY_SIZE, 1);
		FamilySizeSpinBox.setValueFactory(familySizeFactory);
	}

	/**
	 * Will move to the next page if all required fields are filled.<br>
	 * Next page is determined by the CreditCardCheckBox
	 */
	@FXML
	void MoveToNextPage(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			// create a SUBSCRIBER/GUIDE with input info
			Subscriber.Type type = null;
			int familySize = 1;

			// in case of subscriber
			if (SubscriberRadionBtn.isSelected()) {
				type = Subscriber.Type.SUBSCRIBER;
				familySize = FamilySizeSpinBox.getValue();
			}

			// in case of guide
			else
				type = Subscriber.Type.GUIDE;

			Subscriber s = new Subscriber("S" + IDTextField.getText(), IDTextField.getText(),
					FirstNameTextField.getText(), LastNameTextField.getText(), PhoneNumberTextField.getText(),
					EmailTextField.getText(), familySize, type);

			if (CreditCardCheckBox.isSelected()) {
				Navigator n = Navigator.instance();
				GuiController g = n.navigate("RegisterAddCreditCard");
				((RegisterAddCreditCardController) g).addSub(s);
			}

			else {
				Navigator n = Navigator.instance();
				GuiController g = n.navigate("RegisterSummary");
				((RegisterSummaryController) g).addSub(s);
			}
		}
	}

	/**
	 * Shows the FamilySize fields if Subscriber type is chosen
	 */
	@FXML
	void ShowFamilySize(ActionEvent event) {
		FamilySizeLabel.setVisible(true);
		FamilySizeSpinBox.setVisible(true);
	}

	/**
	 * Hides the FamilySize fields if Guide type is chosen
	 */
	@FXML
	void HideFamilySize(ActionEvent event) {
		FamilySizeLabel.setVisible(false);
		FamilySizeSpinBox.setVisible(false);
	}

	/**
	 * Checks if all required fields on the page are filled correctly.<br>
	 * If not, marks the problems with fields for the user
	 * 
	 * @return true if all required fields are filled correctly, false otherwise
	 */
	private boolean CheckAllRequiredFields() {
		boolean res = true;
		res &= CheckFirstName();
		res &= CheckLastName();
		res &= CheckID();
		res &= CheckPhoneNumber();
		res &= CheckEmail();
		return res;
	}

	/**
	 * Check if FirstName field is field correctly.<br>
	 * If not, marks the problem with the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckFirstName() {
		String firstName = FirstNameTextField.getText();

		// if field is empty
		if (firstName.equals("")) {
			if (!FirstNameTextField.getStyleClass().contains("error"))
				FirstNameTextField.getStyleClass().add("error");
			FirstNameNote.setText("* Please enter First Name");
			return false;
		}

		// if field contains a character that is not a letter
		if (!firstName.matches("([a-zA-Z \\-'])+")) {
			if (!FirstNameTextField.getStyleClass().contains("error"))
				FirstNameTextField.getStyleClass().add("error");
			FirstNameNote.setText("* First Name can only contain letters");
			return false;
		}

		// if field is longer then 20 letters
		if (firstName.length() > 20) {
			if (!FirstNameTextField.getStyleClass().contains("error"))
				FirstNameTextField.getStyleClass().add("error");
			FirstNameNote.setText("* First Name up to 20 letters");
			return false;
		}

		FirstNameTextField.getStyleClass().remove("error");
		FirstNameNote.setText("*");
		return true;
	}

	/**
	 * Check if LastName field is field correctly.<br>
	 * If not, marks the problem with the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckLastName() {
		String lastName = LastNameTextField.getText();

		// if field is empty
		if (lastName.equals("")) {
			if (!LastNameTextField.getStyleClass().contains("error"))
				LastNameTextField.getStyleClass().add("error");
			LastNameNote.setText("* Please enter Last Name");
			return false;
		}

		// if field contains a character that is not a letter
		if (!lastName.matches("([a-zA-Z \\-'])+")) {
			if (!LastNameTextField.getStyleClass().contains("error"))
				LastNameTextField.getStyleClass().add("error");
			LastNameNote.setText("* Last Name can only contain letters");
			return false;
		}

		// if field is longer then 20 letters
		if (lastName.length() > 20) {
			if (!LastNameTextField.getStyleClass().contains("error"))
				LastNameTextField.getStyleClass().add("error");
			LastNameNote.setText("* Last Name up to 20 letters");
			return false;
		}

		LastNameNote.setText("*");
		LastNameTextField.getStyleClass().remove("error");
		return true;
	}

	/**
	 * Check if ID field is field correctly.<br>
	 * If not, marks the problem with the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckID() {
		String id = IDTextField.getText();

		// if field is empty
		if (id.equals("")) {
			if (!IDTextField.getStyleClass().contains("error"))
				IDTextField.getStyleClass().add("error");
			IDNote.setText("* Please enter ID");
			return false;
		}

		// if field contains a character that is not a digit
		if (!id.matches("([0-9])+")) {
			if (!IDTextField.getStyleClass().contains("error"))
				IDTextField.getStyleClass().add("error");
			IDNote.setText("* ID can only contain digits");
			return false;
		}

		// if field contains more or less than 9 digits
		if (id.length() != 9) {
			if (!IDTextField.getStyleClass().contains("error"))
				IDTextField.getStyleClass().add("error");
			IDNote.setText("* ID must be 9 digits long");
			return false;
		}

		IDTextField.getStyleClass().remove("error");
		IDNote.setText("*");
		return true;
	}

	/**
	 * Check if PhoneNumber field is field correctly.<br>
	 * If not, marks the problem with the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckPhoneNumber() {
		String phoneNumber = PhoneNumberTextField.getText();

		// if field is empty
		if (phoneNumber.equals("")) {
			if (!PhoneNumberTextField.getStyleClass().contains("error"))
				PhoneNumberTextField.getStyleClass().add("error");
			PhoneNumberNote.setText("* Please enter Phone Number");
			return false;
		}

		// if field contains a character that is not a digit
		if (!phoneNumber.matches("([0-9])+")) {
			if (!PhoneNumberTextField.getStyleClass().contains("error"))
				PhoneNumberTextField.getStyleClass().add("error");
			PhoneNumberNote.setText("* Phone Number can only contain digits");
			return false;
		}

		// if field contains more or less than 10 digits
		if (phoneNumber.length() != 10) {
			if (!PhoneNumberTextField.getStyleClass().contains("error"))
				PhoneNumberTextField.getStyleClass().add("error");
			PhoneNumberNote.setText("* Phone Number must be 10 digits long");
			return false;
		}

		PhoneNumberTextField.getStyleClass().remove("error");
		PhoneNumberNote.setText("*");
		return true;
	}

	/**
	 * Check if Email field is field correctly.<br>
	 * If not, marks the problem with the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckEmail() {
		String email = EmailTextField.getText();

		// if field is empty
		if (email.equals("")) {
			if (!EmailTextField.getStyleClass().contains("error"))
				EmailTextField.getStyleClass().add("error");
			EmailNote.setText("* Please enter Email");
			return false;
		}

		String emailFormat = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$";

		// if field is not in wanted format
		if (!email.matches(emailFormat)) {
			if (!EmailTextField.getStyleClass().contains("error"))
				EmailTextField.getStyleClass().add("error");
			EmailNote.setText("* Email must be _@_._");
			return false;
		}

		EmailTextField.getStyleClass().remove("error");
		EmailNote.setText("*");
		return true;
	}

}
