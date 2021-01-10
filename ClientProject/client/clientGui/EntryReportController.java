package clientGui;

import java.sql.Timestamp;
import java.time.LocalDate;

import clientIO.ConnectionInterface;
import clientIO.clientController;
import entities.ParkEntry;
import entities.ParkEntry.EntryType;
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



	public void setTextDateToday(Label textDateToday) {
		this.textDateToday = textDateToday;
	}


	public void setTextReportDate(Label textReportDate) {
		this.textReportDate = textReportDate;
	}

	public void setTable1_AVGvisitStay(BarChart<String, Double> table1_AVGvisitStay) {
		this.table1_AVGvisitStay = table1_AVGvisitStay;
	}

	public void setTable2_AVGentry(BarChart<String, Integer> table2_AVGentry) {
		this.table2_AVGentry = table2_AVGentry;
	}

	public void setTable1_visitorTypeAxis(CategoryAxis table1_visitorTypeAxis) {
		this.table1_visitorTypeAxis = table1_visitorTypeAxis;
	}


	public void setTable1_avgTimeOfVisitAxis(NumberAxis table1_avgTimeOfVisitAxis) {
		this.table1_avgTimeOfVisitAxis = table1_avgTimeOfVisitAxis;
	}


	public void setTable2_TimeInDayAxis(CategoryAxis table2_TimeInDayAxis) {
		this.table2_TimeInDayAxis = table2_TimeInDayAxis;
	}


	public void setTable2_NumOfVisitorsAxis(NumberAxis table2_NumOfVisitorsAxis) {
		this.table2_NumOfVisitorsAxis = table2_NumOfVisitorsAxis;
	}

	
	
	
	ConnectionInterface serverConnection=clientController.serverConnection;
	
	public void setServerConnection(ConnectionInterface connI) {
		serverConnection=connI;
	}
	
	public double[] avgStayArray;
	public int[] totalPeopleType;

	public int[][] sumPeople;
	public 	int minimalHour;
	public int maximalHour;
	public ParkEntry[] entries ;
	
	

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
		String respons = serverConnection.sendRequestAndResponse(sr);
		entries = (ParkEntry[]) ServerRequest.gson.fromJson(respons, ParkEntry[].class);
		if (entries == null) {
			PopUp.showInformation("Server didnt found entries", "Server Failure:Server didnt found entries",
					"Server didnt found entries");
			return;
		}
		// ---First Chart---//

		avgStayArray = new double[ParkEntry.EntryType.values().length];
		totalPeopleType = new int[ParkEntry.EntryType.values().length];

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
				avgStayArray[entryType.ordinal()]=	(avgStayArray[entryType.ordinal()] / totalPeopleType[entryType.ordinal()]) / 60;
				
				addSeries.getData().add(new XYChart.Data<String, Double>(entryType.toString(),avgStayArray[entryType.ordinal()]));
			} else {
				avgStayArray[entryType.ordinal()]=0;
				addSeries.getData().add(new XYChart.Data<String, Double>(entryType.toString(), (double) 0));

			}
			table1_AVGvisitStay.getData().add(addSeries);

		}

		// --- Second Chart ---//

		sumPeople = new int[ParkEntry.EntryType.values().length][24];
		for (int i = 0; i < ParkEntry.EntryType.values().length; i++) {
			for (int j = 0; j < 24; j++) {
				sumPeople[i][j] = 0;
			}
		}

		minimalHour = 23;
		maximalHour = 0;

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