package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import module.GuiController;

public class DevisionManagerDiscountController implements GuiController{

    @FXML
    private TableColumn<?, ?> discountInPercentage;

    @FXML
    private TableColumn<?, ?> discountStartDate;

    @FXML
    private TableColumn<?, ?> discountEndDate;

    @FXML
    private Button exitButton;

}
