package gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

/** the CancelReport page controller */
public class CancelReportController implements GuiController{

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
    private TextField textTotalOrders;

    @FXML
    private Label labelTotalOrders;

    @FXML
    private Label labelOrdersConfirmed;

    @FXML
    private TextField textOrdersConfirmed;

    @FXML
    private Button buttonPrint;

    @FXML
    private Label labelOrdersNotConfirmed;

    @FXML
    private TextField textOrdersNotConfirmed;

    @FXML
    private Label labelOrdersCanceled;

    @FXML
    private TextField textOrdersCanceled;

    @FXML
    private PieChart pieChartCancel;

    @FXML
    void buttonPrint_OnClick(ActionEvent event) {

    }

}
