package gui;
/**
 * Sample Skeleton for 'ReportExportWindowBoundary.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ReportExportController {

    @FXML
    private Label labelDateToday;

    @FXML
    private TextField textDateToday;

    @FXML
    private Label labelParkManager;

    @FXML
    private Label labelParkName;

    @FXML
    private Button buttonParkSettings;

    @FXML
    private ComboBox<?> ReportSelectionComboBox;

    @FXML
    private Label labelReportSelection;

    @FXML
    private Button buttonGetReport;

    @FXML
    private ComboBox<?> ParkSelectionComboBox;

    @FXML
    private Label LabelParkId;

    @FXML
    private TextField textParkId;

    @FXML
    void getReoprt_OnClick(ActionEvent event) {

    }

}
