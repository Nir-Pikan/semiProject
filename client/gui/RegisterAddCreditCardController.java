package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterAddCreditCardController {

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
    private Button NextBtn;

    @FXML
    private Label DetailsTimelineLabel;

    @FXML
    private Label CreditCardTimelineLabel;

    @FXML
    private Label SummaryTimelineLabel;

    @FXML
    private ComboBox<?> CardTypeComboBox;

    @FXML
    private Hyperlink CVVHyperLink;

    @FXML
    private ComboBox<?> ExpirationDateMonthComboBox;

    @FXML
    private ComboBox<?> ExpirationDateYearComboBox;

    @FXML
    void MoveToNextPage(ActionEvent event) {

    }

    @FXML
    void OpenCVVHyperLink(ActionEvent event) {

    }

    @FXML
    void SetCVV(ActionEvent event) {

    }

    @FXML
    void SetCardNumber(ActionEvent event) {

    }

    @FXML
    void SetCardType(ActionEvent event) {

    }

    @FXML
    void SetExpirationDateMonth(ActionEvent event) {

    }

    @FXML
    void SetExpirationDateYear(ActionEvent event) {

    }

    @FXML
    void SetNameOnCard(ActionEvent event) {

    }

    @FXML
    void SetOnwerID(ActionEvent event) {

    }

}
