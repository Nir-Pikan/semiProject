package gui;
/**
 * Sample Skeleton for 'VisitorsReportBoundary.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;
import module.JavafxPrinter;

public class VisitorsReportController implements GuiController{

    @FXML
    private Label labelDateToaday;

    @FXML
    private TextField textDateToday;

    @FXML
    private Label labelVisitorsReport;

    @FXML
    private Label labelParkName;

    @FXML
    private TextField textParkName;

    @FXML
    private Label labelReportDate;

    @FXML
    private TextField textReportDate;

    @FXML
    private Label labelNumSingleVisitors;

    @FXML
    private TextField textNumSingleVisitors;

    @FXML
    private Label labelNumGroupVisitors;

    @FXML
    private TextField textNumGroupVisitors;

    @FXML
    private Label labelNumSubscriberVisitors;

    @FXML
    private TextField textNumSubscriberVisitors;

    @FXML
    private Label labelTotalVisitors;

    @FXML
    private TextField textTotalVisitors;

    @FXML
    private BarChart<?, ?> VisitorsChart;

    @FXML
    private CategoryAxis CharVisitorsTypeX;

    @FXML
    private NumberAxis ChartVisitorNumberY;

    @FXML
    private Button buttonPrint;

    @FXML
    void buttonPrint_OnClick(ActionEvent event) {
JavafxPrinter.printThisWindow(buttonPrint.getScene().getWindow());
    }

}
