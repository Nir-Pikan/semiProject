package gui;

import java.sql.Timestamp;

import entities.ParkEntry;
import entities.Subscriber;
import io.clientController;

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
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the VisitorsReport page controller */
public class VisitorsReportController implements GuiController {

	@FXML
	private Label labelDateToaday;

	@FXML
	private Label textDateToday;

	@FXML
	private Label labelVisitorsReport;

	@FXML
	private Label labelParkName;

	@FXML
	private Label textParkName;

	@FXML
	private Label labelReportDate;

	@FXML
	private Label textReportDate;

	@FXML
	private Label labelNumSingleVisitors;

	@FXML
	private Label textNumSingleVisitors;

	@FXML
	private Label labelNumGroupVisitors;

	@FXML
	private Label textNumGroupVisitors;

	@FXML
	private Label labelNumSubscriberVisitors;

	@FXML
	private Label textNumSubscriberVisitors;

	@FXML
	private Label labelTotalVisitors;

	@FXML
	private Label textTotalVisitors;

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
	
	/**
	 * Initialize the monthly visitors report using the parameters
	 * @param parkName name of the park the report's on 
	 * @param parkID ID of the park the report's on
	 * @param reportDate the date range of the report
	 * <p>
	 * @apiNote reportDate[0] = from , reportDate[1] = to. 
	 * */
	public void initReport(String parkName, String parkID, Timestamp[] reportDate) {
		Timestamp current = new Timestamp(System.currentTimeMillis());
		textDateToday.setText(current.toString());
		textParkName.setText(parkName);
		textReportDate.setText("from " + reportDate[0].toString() + " to " + reportDate[1].toString());
		
    	//send request to get park entries to clientController
    	String response = clientController.client.sendRequestAndResponse(new ServerRequest(
    			Manager.Entry, "getEntriesByDate" , ServerRequest.gson.toJson(reportDate,Timestamp[].class)));
    	
    	switch(response) {
    	case "Error: There is no 2 times search between ":
    		
    		break;
    		
    	case "Error: start time is later than end time ":
    
    		break;
    		
    	case "Error: could not get entires from DB ":
    		
    		break;
    	
    	default:
    		ServerRequest.gson.fromJson(response, ParkEntry[].class);
    	}
		
	};
	
	//for testing
	public static void main(String[] args) {
		
	}

}
