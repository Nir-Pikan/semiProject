package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import module.GuiController;

public class DiscountRequestPageController implements GuiController{

    @FXML
    private Button discountRequestDecline;

    @FXML
    private Button discountRequestAccept;

    @FXML
    private Label discountPercentageText;

    @FXML
    private Label startDateText;

    @FXML
    private Label endDateText;

    @FXML
    private Button exitButton;

}