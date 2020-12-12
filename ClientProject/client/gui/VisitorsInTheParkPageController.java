package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

public class VisitorsInTheParkPageController implements GuiController {

    @FXML
    private ChoiceBox<String> parkNumChoise;

    @FXML
    private Label parkNum;

    @FXML
    private TextField visitorsAmountText;

    @FXML
    private Button closeButton;

    @FXML
    void familyOrder(ActionEvent event) {

    }

    @FXML
    void privateGroupOrder(ActionEvent event) {

    }

    @FXML
    void regularOrder(ActionEvent event) {

    }

}
