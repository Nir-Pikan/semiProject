package gui;

import javafx.event.ActionEvent;

/**
 * Sample Skeleton for 'IncomeReportBoundary.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import module.GuiController;

public class IncomeReportController implements GuiController{

    @FXML
    private Label labelDateToday;

    @FXML
    private TextField textDateToday;

    @FXML
    private Label labelIncomeReport;

    @FXML
    private Label labalParkName;

    @FXML
    private TextField textParkName;

    @FXML
    private Label labaelReportDate;

    @FXML
    private TextField textReportDate;

    @FXML
    private TextField textTotalIncome;

    @FXML
    private Label labelTotalIncome;

    @FXML
    private Label labelAVGIncomeDay;

    @FXML
    private TextField textAVGIncomeDay;

    @FXML
    private TableView<?> incomeTable;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> visitorsColumn;

    @FXML
    private TableColumn<?, ?> incomeColumn;

    @FXML
    private Button buttonPrint;

    @FXML
    void buttonPrint_OnClick(ActionEvent event) {

    }

}
