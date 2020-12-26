package gui;

import java.sql.Timestamp;

import entities.ParkEntry;
import io.clientController;

/**
 * Sample Skeleton for 'VisitorsReportBoundary.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import module.GuiController;
import module.JavafxPrinter;
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
	private BarChart<String, Number> VisitorsChart;

	@FXML
	private CategoryAxis CharVisitorsTypeX;

	@FXML
	private NumberAxis ChartVisitorNumberY;

	@FXML
	private Button buttonPrint;

	/**
	 * prints the report when Print report button is clicked
	 */
	@FXML
	void buttonPrint_OnClick(ActionEvent event) {
		// JavafxPrinter.printThisWindow(buttonPrint.getScene().getWindow());
		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Num of visitors");
		series1.getData().add(new XYChart.Data("Single",10));
		series1.getData().add(new XYChart.Data("Group",5));
		series1.getData().add(new XYChart.Data("Subscribers",20));
		VisitorsChart.getData().add(series1);
	}

	/**
	 * Initialize the monthly visitors report using the parameters
	 * 
	 * @param parkName   name of the park the report's on
	 * @param parkID     ID of the park the report's on
	 * @param reportDate the date range of the report
	 *                   <p>
	 * @apiNote reportDate[0] = from , reportDate[1] = to.
	 */
	public void initReport(String parkName, String parkID, Timestamp[] reportDate) {
		Timestamp current = new Timestamp(System.currentTimeMillis());
		textDateToday.setText(current.toString());
		textParkName.setText(parkName);
		textReportDate.setText("from " + reportDate[0].toString() + " to " + reportDate[1].toString());

		// send request to get park entries to clientController
		String response = clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Entry,
				"getEntriesByDate", ServerRequest.gson.toJson(reportDate, Timestamp[].class)));

		switch (response) {
		case "Error: There is no 2 times search between ":
			System.out.println(response);
			break;

		case "Error: start time is later than end time ":
			System.out.println(response);
			break;

		case "Error: could not get entires from DB ":
			System.out.println(response);
			break;

		default:
			int singleVisitorsCounter = 0;
			int groupVisitorsCounter = 0;
			int subscriberVisitorsCounter = 0;

			ParkEntry[] entries = ServerRequest.gson.fromJson(response, ParkEntry[].class);

			// for each entry
			for (ParkEntry entry : entries) {

				// only for wanted park
				if (entry.parkID.equals(parkID)) {

					// single
					if (entry.entryType.equals(ParkEntry.EntryType.Personal)) {
						singleVisitorsCounter++;
						subscriberVisitorsCounter += entry.numberOfSubscribers;
					}

					// subscriber
					if (entry.entryType.equals(ParkEntry.EntryType.Subscriber))
						subscriberVisitorsCounter += entry.numberOfSubscribers;

					// group
					if (entry.entryType.equals(ParkEntry.EntryType.Group)) {
						groupVisitorsCounter += entry.numberOfVisitors;
						subscriberVisitorsCounter += entry.numberOfSubscribers;
					}

					// privateGroup
					if (entry.entryType.equals(ParkEntry.EntryType.PrivateGroup)) {
						singleVisitorsCounter += entry.numberOfVisitors;
						subscriberVisitorsCounter += entry.numberOfSubscribers;
					}
				}
			}
			// set the visitor numbers
			textNumSingleVisitors.setText(String.valueOf(singleVisitorsCounter));
			textNumGroupVisitors.setText(String.valueOf(groupVisitorsCounter));
			textNumSubscriberVisitors.setText(String.valueOf(subscriberVisitorsCounter));
			textTotalVisitors
					.setText(String.valueOf(singleVisitorsCounter + groupVisitorsCounter + subscriberVisitorsCounter));

	
			//update the bar chart
			//TODO Work on how this looks ~Nir Pikan~
			XYChart.Series series1 = new XYChart.Series();
			series1.setName("Num of visitors");
			series1.getData().add(new XYChart.Data("Single",singleVisitorsCounter));
			series1.getData().add(new XYChart.Data("Group",groupVisitorsCounter));
			series1.getData().add(new XYChart.Data("Subscribers",subscriberVisitorsCounter));
			VisitorsChart.getData().add(series1);
		}
	}

	public static enum VisitorType {
		Single, Subscribers, Group
	}
}
