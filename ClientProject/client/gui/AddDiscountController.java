package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import module.GuiController;

public class AddDiscountController implements GuiController{

    @FXML
    private DatePicker dateStartDate;

    @FXML
    private DatePicker dateEndDate;

    @FXML
    private TextField txtDiscountId;

    @FXML
    private TextField txtDiscountValue;

    @FXML
    void submitDiscount(ActionEvent event) {

    }

}
