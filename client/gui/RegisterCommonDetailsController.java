package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import module.GuiController;

public class RegisterCommonDetailsController implements GuiController{

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
    private CheckBox CreditCardCheckBox;

    @FXML
    private RadioButton SubscriberRadionBtn;

    @FXML
    private RadioButton GuideRadioBtn;

    @FXML
    private Label FamilySizeLabel;

    @FXML
    private Spinner<?> FamilySizeSpinBox;

    @FXML
    private Button NextBtn;

    @FXML
    private Label DetailsTimelineLabel;

    @FXML
    private Label AddCreditCardTimelineLabel;

    @FXML
    private Label SummaryTimelineLabel;

    @FXML
    void HideFamilySize(ActionEvent event) {

    }

    @FXML
    void MoveToNextPage(ActionEvent event) {

    }

    @FXML
    void SetCreditCard(ActionEvent event) {

    }

    @FXML
    void SetEmail(ActionEvent event) {

    }

    @FXML
    void SetFamilySize(InputMethodEvent event) {

    }

    @FXML
    void SetFirstName(ActionEvent event) {

    }

    @FXML
    void SetID(ActionEvent event) {

    }

    @FXML
    void SetLastName(ActionEvent event) {

    }

    @FXML
    void SetPhoneNumber(ActionEvent event) {

    }

    @FXML
    void ShowFamilySize(ActionEvent event) {

    }

}
