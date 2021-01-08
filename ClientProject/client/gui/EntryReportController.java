package gui;

import java.sql.Timestamp;
import java.time.LocalDate;

import entities.ParkEntry;
import entities.ParkEntry.EntryType;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modules.JavafxPrinter;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the EntryReport page controller */
public class EntryReportController implements GuiController, Report {

	@FXML
	private Label labelDateToaday;

	@FXML
	private Label textDateToday;

	@FXML
	private Label labelEntryReport;

	@FXML
	private Label labelParkName;

	@FXML
	private TextField textParkName;

	@FXML
	private Label labelReportDate;

	@FXML
	private Label textReportDate;

	@FXML
	private Button buttonExtractReport;

	@FXML
	private BarChart<String, Double> table1_AVGvisitStay;

	@FXML
	private CategoryAxis table1_visitorTypeAxis;

	@FXML
	private NumberAxis table1_avgTimeOfVisitAxis;

	@FXML
	private BarChart<String, Integer> table2_AVGentry;

	@FXML
	private CategoryAxis table2_TimeInDayAxis;

	@FXML
	private NumberAxis table2_NumOfVisitorsAxis;

	private boolean isDataShown = false;

	/** prints the report */
	@FXML
	void ExtractReport(ActionEvent event) {
		JavafxPrinter.printThisWindow(buttonExtractReport.getScene().getWindow());
	}

	/**
	 * initialize the report's fields Important Notice:
	 * 
	 * @param parkName                the park's name
	 * @param parkID                  the park's ID
	 * @param reportStartAndEndTimes: [0]: start, [1]: end
	 */
	public void initReport(String parkName, String parkID, Timestamp[] dates) {
		if (isDataShown)
			return;
		isDataShown = true;
		textDateToday.setText(LocalDate.now().toString());
		textReportDate.setText("From " + dates[0].toLocalDateTime().toLocalDate().toString() + " To "
				+ dates[1].toLocalDateTime().toLocalDate().toString());

		// ---Getting entries by dates---//
		String msgData = ServerRequest.gson.toJson(dates, Timestamp[].class);
		ServerRequest sr = new ServerRequest(Manager.Entry, "getEntriesByDate", msgData);
		String respons = clientController.client.sendRequestAndResponse(sr);
		ParkEntry[] entries = (ParkEntry[]) ServerRequest.gson.fromJson(respons, ParkEntry[].class);
		if (entries == null) {
			PopUp.showInformation("Server didnt found entries", "Server Failure:Server didnt found entries",
					"Server didnt found entries");
			return;
		}
		// ---First Chart---//

		double[] avgStayArray = new double[ParkEntry.EntryType.values().length];
		int[] totalPeopleType = new int[ParkEntry.EntryType.values().length];

		for (int i = 0; i < ParkEntry.EntryType.values().length; i++) {
			avgStayArray[i] = 0;
		}

		for (ParkEntry parkEntry : entries) {
			if (!parkEntry.parkID.equals(parkID))
				continue;
			long stay = Math.abs(parkEntry.exitTime.getTime() - parkEntry.arriveTime.getTime());
			double duration = stay / (1000 * 60);
			avgStayArray[parkEntry.entryType.ordinal()] += (duration * parkEntry.numberOfVisitors);
			totalPeopleType[parkEntry.entryType.ordinal()] += parkEntry.numberOfVisitors;

		}

		for (EntryType entryType : ParkEntry.EntryType.values()) {
			XYChart.Series<String, Double> addSeries = new XYChart.Series<String, Double>();
			addSeries.setName(entryType.toString());
			if (totalPeopleType[entryType.ordinal()] != 0) {
				addSeries.getData().add(new XYChart.Data<String, Double>(entryType.toString(),
						(avgStayArray[entryType.ordinal()] / totalPeopleType[entryType.ordinal()]) / 60));
			} else {
				addSeries.getData().add(new XYChart.Data<String, Double>(entryType.toString(), (double) 0));

			}
			table1_AVGvisitStay.getData().add(addSeries);

		}

		// --- Second Chart ---//

		int[][] sumPeople = new int[ParkEntry.EntryType.values().length][24];
		for (int i = 0; i < ParkEntry.EntryType.values().length; i++) {
			for (int j = 0; j < 24; j++) {
				sumPeople[i][j] = 0;
			}
		}

		int minimalHour = 23;
		int maximalHour = 0;

		for (ParkEntry parkEntry : entries) {
			if (parkEntry.parkID.equals(parkID)) {
				sumPeople[parkEntry.entryType.ordinal()][parkEntry.arriveTime.toLocalDateTime()
						.getHour()] += parkEntry.numberOfVisitors;
				if (parkEntry.arriveTime.toLocalDateTime().getHour() < minimalHour)
					minimalHour = parkEntry.arriveTime.toLocalDateTime().getHour();
				if (parkEntry.arriveTime.toLocalDateTime().getHour() > maximalHour)
					maximalHour = parkEntry.arriveTime.toLocalDateTime().getHour();
			}
		}

		for (EntryType entryType : ParkEntry.EntryType.values()) {

			XYChart.Series<String, Integer> addSeries = new XYChart.Series<String, Integer>();

			addSeries.setName(entryType.toString());
			for (int i = minimalHour; i <= maximalHour; i++) {
				addSeries.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i) + ":00",
						sumPeople[entryType.ordinal()][i]));
			}
			table2_AVGentry.getData().add(addSeries);

		}

	}

	@Override
	public void init() {

	}

}