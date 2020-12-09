package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import module.GuiController;

public class ParkManagerChangesRequestPageController implements GuiController{

    @FXML
    private Label maxVisitorsCurrentLabel;

    @FXML
    private Label maxVisitorsRequestLabes;

    @FXML
    private Label avgVisitTimeCurrentLabel;

    @FXML
    private Label avgVisitTimeRequesLabel;

    @FXML
    private Label maxPreordereRequestLabel;

    @FXML
    private Label maxPreorderCurrentLabel;

    @FXML
    private Button maxVisitorsAcceptButton;

    @FXML
    private Button maxVisitorsDeclineButton;

    @FXML
    private Button avgVisitTimeAcceptButton;

    @FXML
    private Button maxPreorderAcceptButton;

    @FXML
    private Button avgVisitTimeDeclineButton;

    @FXML
    private Button maxPreorderDeclineButton;

    @FXML
    private Label parkNumLabel;

}

