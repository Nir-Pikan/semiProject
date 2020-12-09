package gui;
/**
 * Sample Skeleton for 'UsageReportBoundary.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import module.GuiController;

public class UsageReportController implements GuiController{

    @FXML
    private Label labelDateToday;

    @FXML
    private TextField textDateToday;

    @FXML
    private Label labelUsageReport;

    @FXML
    private Label labelParkName;

    @FXML
    private TextField textParkName;

    @FXML
    private Label labelReportDate;

    @FXML
    private TextField textReportDate;

    @FXML
    private Label labelMaxCapacity;

    @FXML
    private TextField textMaxCapacity;

    @FXML
    private TableView<?> visitorUsageTable;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> visitorsColumn;

    @FXML
    private TableColumn<?, ?> usageColumn;

    @FXML
    private Button buttonPrint;

    @FXML
    void printButton_OnClick(ActionEvent event) {

    }

}
