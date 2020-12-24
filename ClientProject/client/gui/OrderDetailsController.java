package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

/** the OrderDetails page controller */
public class OrderDetailsController implements GuiController{

    @FXML
    private Button approveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField personIdTxt;

    @FXML
    private TextField parkNameTxt;

    @FXML
    private TextField orderTypeTxt;

    @FXML
    private TextField noOfVisitorsTxt;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private Label orderNoTxt;

    @FXML
    private TextField noOfSubscribersTxt;

    @FXML
    void ApproveOrder(ActionEvent event) {

    }

    @FXML
    void cancelOrder(ActionEvent event) {

    }

}
