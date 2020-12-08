package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;

public class EntryReportController implements GuiController{

    @FXML
    private Label labelDateToaday;

    @FXML
    private TextField textDateToday;

    @FXML
    private Label labelEntryReport;

    @FXML
    private Label labelParkName;

    @FXML
    private TextField textParkName;

    @FXML
    private Label labelReportDate;

    @FXML
    private TextField textReportDate;

    @FXML
    private Button buttonExtractReport;

    @FXML
    private BarChart<?, ?> table1_AVGvisitStay;

    @FXML
    private CategoryAxis table1_visitorTypeAxis;

    @FXML
    private NumberAxis table1_avgTimeOfVisitAxis;

    @FXML
    private StackedBarChart<?, ?> table2_AVGentry;

    @FXML
    private CategoryAxis table2_TimeInDayAxis;

    @FXML
    private NumberAxis table2_NumOfVisitorsAxis;

    @FXML
    void ExtractReport(ActionEvent event) {

    }

}
