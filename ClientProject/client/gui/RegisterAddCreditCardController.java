package gui;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import entities.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** the RegisterAddCreditCard page controller */
public class RegisterAddCreditCardController implements GuiController {

	private Subscriber s;

	@FXML
	private Label NameOnCardLabel;

	@FXML
	private TextField NameOnCardTextField;

	@FXML
	private Label AddCreditCardTitleLabel;

	@FXML
	private Label CardTypeLabel;

	@FXML
	private Label OwnerIDLabel;

	@FXML
	private Label CardNumberLabel;

	@FXML
	private Label CVVLabel;

	@FXML
	private Label ExpirationDateLabel;

	@FXML
	private TextField OwnerIDTextField;

	@FXML
	private TextField CardNumberTextField;

	@FXML
	private TextField CVVTextField;

	@FXML
	private Label RequiredLabel;

	@FXML
	private Label OwnerIDNote;

	@FXML
	private Label CardNumberNote;

	@FXML
	private Label CVVNote;

	@FXML
	private Button NextBtn;

	@FXML
	private Label DetailsTimelineLabel;

	@FXML
	private Label CreditCardTimelineLabel;

	@FXML
	private Label SummaryTimelineLabel;

	@FXML
	private ComboBox<String> CardTypeComboBox;

	@FXML
	private Hyperlink CVVHyperLink;

	@FXML
	private ComboBox<String> ExpirationDateMonthComboBox;

	@FXML
	private Label ExpirationDateNote;

	@FXML
	private ComboBox<String> ExpirationDateYearComboBox;

	@FXML
	private Label NameOnCardNote;

	/**
	 * Will move to the next page if all required fields are filled.
	 */
	@FXML
	void MoveToNextPage(ActionEvent event) {
		if (CheckAllRequiredFields()) {
			// add credit card to subscriber
			s.SetCreditCard(CardNumberTextField.getText(), OwnerIDTextField.getText(), NameOnCardTextField.getText(),
					CVVTextField.getText(), ExpirationDateMonthComboBox.getValue(),
					ExpirationDateYearComboBox.getValue(),
					Subscriber.CardType.valueOf(CardTypeComboBox.getValue().toUpperCase().replaceAll(" ", "")));

			Navigator n = Navigator.instance();
			GuiController g = n.navigate("RegisterSummary");
			((RegisterSummaryController) g).addSub(s);
		}
	}

	/**
	 * Opens the cvv explanation page on Internet browser
	 */
	@FXML
	void OpenCVVHyperLink(ActionEvent event) {
		try {
			URL cvv = new URL("https://www.cvvnumber.com/");
			openWebpage(cvv);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change the card number prompt and CVV prompt according to card type
	 */
	@FXML
	void SetCardType(ActionEvent event) {
		if (CardTypeComboBox.getValue() == "American Express") {
			CardNumberTextField.setPromptText("XXXX-XXXXXX-XXXXX");
			CVVTextField.setPromptText("XXXX");
		} else {
			CardNumberTextField.setPromptText("XXXX-XXXX-XXXX-XXXX");
			CVVTextField.setPromptText("XXX");
		}
	}

	@Override
	public void init() {
		CardTypeComboBox.getItems().clear();
		CardTypeComboBox.getItems().addAll("Visa", "Master Card", "American Express");
		ExpirationDateMonthComboBox.getItems().clear();
		ExpirationDateMonthComboBox.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
				"12");
		ExpirationDateYearComboBox.getItems().clear();
		ExpirationDateYearComboBox.getItems().addAll("2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028",
				"2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041");
		CardTypeComboBox.getSelectionModel().select(0);
	}

	/**
	 * Checks if all required fields on the page are filled correctly. If not, marks
	 * the problems with fields for the user
	 * 
	 * @return true if all required fields are filled correctly, false otherwise
	 */
	private boolean CheckAllRequiredFields() {
		boolean res = true;
		res &= CheckNameOnCard();
		res &= CheckOwnerID();
		res &= CheckCardNumber();
		res &= CheckCVV();
		res &= CheckExpirationDate();
		return res;
	}

	/**
	 * Check if NameOnCard field is field correctly. If not, marks the problem with
	 * the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckNameOnCard() {
		String nameOnCard = NameOnCardTextField.getText();

		// if field is empty
		if (nameOnCard.equals("")) {
			if (!NameOnCardTextField.getStyleClass().contains("error"))
				NameOnCardTextField.getStyleClass().add("error");
			NameOnCardNote.setText("* Please enter Name on card");
			return false;
		}

		// if field contains a character that is not a letter
		if (!nameOnCard.matches("([a-zA-Z \\-'])+")) {
			if (!NameOnCardTextField.getStyleClass().contains("error"))
				NameOnCardTextField.getStyleClass().add("error");
			NameOnCardNote.setText("* Name only contains letters");
			return false;
		}

		// if field is longer then 30 letters
		if (nameOnCard.length() > 30) {
			if (!NameOnCardTextField.getStyleClass().contains("error"))
				NameOnCardTextField.getStyleClass().add("error");
			NameOnCardNote.setText("* Name up to 30 letters");
			return false;
		}

		NameOnCardTextField.getStyleClass().remove("error");
		NameOnCardNote.setText("*");
		return true;
	}

	/**
	 * Check if OwnerID field is field correctly. If not, marks the problem with the
	 * field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckOwnerID() {
		String id = OwnerIDTextField.getText();

		// if field is empty
		if (id.equals("")) {
			if (!OwnerIDTextField.getStyleClass().contains("error"))
				OwnerIDTextField.getStyleClass().add("error");
			OwnerIDNote.setText("* Please enter Owner ID");
			return false;
		}

		// if field contains a character that is not a digit
		if (!id.matches("([0-9])+")) {
			if (!OwnerIDTextField.getStyleClass().contains("error"))
				OwnerIDTextField.getStyleClass().add("error");
			OwnerIDNote.setText("* Owner ID can only contain digits");
			return false;
		}

		// if field contains more or less than 9 digits
		if (id.length() != 9) {
			if (!OwnerIDTextField.getStyleClass().contains("error"))
				OwnerIDTextField.getStyleClass().add("error");
			OwnerIDNote.setText("* Owner ID must be 9 digits long");
			return false;
		}

		OwnerIDTextField.getStyleClass().remove("error");
		OwnerIDNote.setText("*");
		return true;
	}

	/**
	 * Check if CardNumber field is field correctly. If not, marks the problem with
	 * the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckCardNumber() {
		String cardNumber = CardNumberTextField.getText();

		// if field is empty
		if (cardNumber.equals("")) {
			if (!CardNumberTextField.getStyleClass().contains("error"))
				CardNumberTextField.getStyleClass().add("error");
			CardNumberNote.setText("* Please enter Card number");
			return false;
		}

		// if field contains a character that is not a digit or -
		if (!cardNumber.matches("([0-9-])+")) {
			if (!CardNumberTextField.getStyleClass().contains("error"))
				CardNumberTextField.getStyleClass().add("error");
			CardNumberNote.setText("* Can only contain digits and -");
			return false;
		}

		// if cardType is Visa\Master Card
		String format = "(([0-9]{4})-){3}([0-9]{4})";
		String formatNote = "* XXXX-XXXX-XXXX-XXXX";

		// if CardType is American Express
		if (CardTypeComboBox.getValue().equals("American Express")) {
			format = "([0-9]{4})-([0-9]{6})-([0-9]{5})";
			formatNote = "* XXXX-XXXXXX-XXXXX";
		}

		// if field is not in right format
		if (!cardNumber.matches(format)) {
			if (!CardNumberTextField.getStyleClass().contains("error"))
				CardNumberTextField.getStyleClass().add("error");
			CardNumberNote.setText(formatNote);
			return false;
		}

		CardNumberTextField.getStyleClass().remove("error");
		CardNumberNote.setText("*");
		return true;
	}

	/**
	 * Check if CVV field is field correctly. If not, marks the problem with the
	 * field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckCVV() {
		String cvv = CVVTextField.getText();

		// if field is empty
		if (cvv.equals("")) {
			if (!CVVTextField.getStyleClass().contains("error"))
				CVVTextField.getStyleClass().add("error");
			CVVNote.setText("* Please enter CVV");
			return false;
		}

		// if field contains a character that is not a digit
		if (!cvv.matches("([0-9])+")) {
			if (!CVVTextField.getStyleClass().contains("error"))
				CVVTextField.getStyleClass().add("error");
			CVVNote.setText("*CVV can only contain digits");
			return false;
		}

		// if cardType is Visa\Master Card
		String format = "([0-9]{3})";
		String formatNote = "* XXX";

		// if CardType is American Express
		if (CardTypeComboBox.getValue() == "American Express") {
			format = "([0-9]{4})";
			formatNote = "* XXXX";
		}

		// if field is not in right format
		if (!cvv.matches(format)) {
			if (!CVVTextField.getStyleClass().contains("error"))
				CVVTextField.getStyleClass().add("error");
			CVVNote.setText(formatNote);
			return false;
		}

		CVVTextField.getStyleClass().remove("error");
		CVVNote.setText("*");
		return true;
	}

	/**
	 * Check if ExpirationDate fields are field correctly. If not, marks the problem
	 * with the field for the user
	 * 
	 * @return true if filled correctly, false otherwise
	 */
	private boolean CheckExpirationDate() {
		String edMonth = ExpirationDateMonthComboBox.getValue();
		String edYear = ExpirationDateYearComboBox.getValue();
		boolean ans = true;

		// if Month field was not chosen
		if (edMonth == null) {
			if (!ExpirationDateMonthComboBox.getStyleClass().contains("error"))
				ExpirationDateMonthComboBox.getStyleClass().add("error");
			ExpirationDateNote.setText("* Choose both fields");
			ans = false;
		} else
			ExpirationDateMonthComboBox.getStyleClass().remove("error");

		// if Year field was not chosen
		if (edYear == null) {
			if (!ExpirationDateYearComboBox.getStyleClass().contains("error"))
				ExpirationDateYearComboBox.getStyleClass().add("error");
			ExpirationDateNote.setText("* Choose both fields");
			ans = false;
		} else
			ExpirationDateYearComboBox.getStyleClass().remove("error");

		if (ans)
			ExpirationDateNote.setText("*");

		return ans;
	}

	/**
	 * Receive subscriber from previous page
	 */
	public void addSub(Subscriber s) {
		this.s = s;
	}

	/**
	 * Helps openWebpage(URL url) method to open a web page
	 * 
	 * @param uri the web page uri to be opened
	 */
	public static boolean openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Open a web page using url
	 * 
	 * @param url the URL we want to open on browser
	 * @return true if succeed or false if failed
	 */
	public static boolean openWebpage(URL url) {
		try {
			return openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}

}
