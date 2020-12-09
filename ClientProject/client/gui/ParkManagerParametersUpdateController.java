package gui;
/**
 * Sample Skeleton for 'ParkManagerParametersUpdateBoundary.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

public class ParkManagerParametersUpdateController implements GuiController{

    @FXML
    private Label labelDateToday;

    @FXML
    private TextField textDateToday;

    @FXML
    private Label labelParkManager;

    @FXML
    private Label labelParkName;

    @FXML
    private TextField textParkName;

    @FXML
    private Label labelcurrentOnChanged;

    @FXML
    private Label labelParkMaxCapacity;

    @FXML
    private TextField textParkMaxCapacity;

    @FXML
    private Label labelOnCapacityChanged;

    @FXML
    private Label labelParkMaxPreOrders;

    @FXML
    private TextField textParkMaxPreOrders;

    @FXML
    private Label labelOnPreOrdersChanged;

    @FXML
    private Label labelParkAVGtime;

    @FXML
    private TextField textParkAVGtime;

    @FXML
    private Label labelOnAVGchanged;

    @FXML
    private Button buttonEditParameters;

    @FXML
    private Button buttonSendRequest;

    @FXML
    void editParametersButton_OnClick(ActionEvent event) {

    }

    @FXML
    void sendRequest_OnClick(ActionEvent event) {

    }

}
