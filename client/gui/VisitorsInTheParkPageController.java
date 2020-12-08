package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

public class VisitorsInTheParkPageController implements GuiController {

    @FXML
    private ChoiceBox<?> parkNumChoise;

    @FXML
    private Label parkNum;

    @FXML
    private Button ShowVisitorsAmountButton;

    @FXML
    private TextField visitorsAmountText;

    @FXML
    private Button closeButton;

}
