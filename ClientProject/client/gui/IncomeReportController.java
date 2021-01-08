package gui;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import entities.ParkEntry;
import io.clientController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import module.GuiController;
import module.JavafxPrinter;
import module.Report;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the IncomeReport page controller */
public class IncomeReportController implements GuiController, Report {

	@FXML
	private Label labelDateToday;

	@FXML
	private Label textDateToday;

	@FXML
	private Label labelIncomeReport;

	@FXML
	private Label labalParkName;

	@FXML
	private Label textParkName;

	@FXML
	private Label labaelReportDate;

	@FXML
	private Label textReportDate;

	@FXML
	private Label textTotalIncome;

	@FXML
	private Label labelTotalIncome;

	@FXML
	private Label labelAVGIncomeDay;

	@FXML
	private Label textAVGIncomeDay;

	@FXML
	private TableView<Cell> incomeTable;

	@FXML
	private TableColumn<Cell, String> dateColumn;

	@FXML
	private TableColumn<Cell, Integer> visitorsColumn;

	@FXML
	private TableColumn<Cell, Float> incomeColumn;

	@FXML
	private Button buttonPrint;

	private int TotalIncome = 0;

	private Timestamp[] reportStartAndEndTimes;
	private String parkName;

	/** prints the report */
	@FXML
	void buttonPrint_OnClick(ActionEvent event) {
		JavafxPrinter.printThisWindow(buttonPrint.getScene().getWindow());
	}

	/**
	 * initialize the report's fields and call createReport() Important Notice:
	 * 
	 * @param parkName                the park's name
	 * @param parkID                  the park's ID
	 * @param reportStartAndEndTimes: [0]: start, [1]: end
	 */
	public void initReport(String parkName, String parkID, Timestamp[] reportStartAndEndTimes) {
		this.reportStartAndEndTimes = reportStartAndEndTimes;
		this.parkName = parkName;
		dateColumn.setCellValueFactory(new PropertyValueFactory<Cell, String>("Date"));
		visitorsColumn.setCellValueFactory(new PropertyValueFactory<Cell, Integer>("Visitors"));
		incomeColumn.setCellValueFactory(new PropertyValueFactory<Cell, Float>("Income"));
		createReport();
	}

	/**
	 * creates the report
	 */
	void createReport() {
		ArrayList<Cell> cellsList = new ArrayList<Cell>();
		TotalIncome = 0;
		String requestData = ServerRequest.gson.toJson(reportStartAndEndTimes, Timestamp[].class);
		ServerRequest serverRequest = new ServerRequest(Manager.Entry, "getEntriesByDate", requestData);
		String response = clientController.client.sendRequestAndResponse(serverRequest);
		ParkEntry[] allParkEntrys = ServerRequest.gson.fromJson(response, ParkEntry[].class);
		if (allParkEntrys == null)
			return;
		Map<myDate, ArrayList<ParkEntry>> map = new TreeMap<myDate, ArrayList<ParkEntry>>();
		for (ParkEntry entry : allParkEntrys) {
			if (ValidOrderToReport(entry)) {
				myDate date = new myDate(entry.arriveTime.toLocalDateTime().toLocalDate().toString().trim());
				if (!map.containsKey(date)) {
					map.put(date, new ArrayList<ParkEntry>());
				}
				map.get(date).add(entry);
			}
		}
		for (Map.Entry<myDate, ArrayList<ParkEntry>> dayEntrys : map.entrySet()) {
			float income = 0;
			int visitors = 0;
			myDate date = dayEntrys.getKey();

			for (ParkEntry parkEntry : dayEntrys.getValue()) {
				visitors += parkEntry.numberOfVisitors;
				income += parkEntry.priceOfEntry;
			}
			TotalIncome += income;
			cellsList.add(new Cell(date.getDate(), visitors, income));
		}
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		textDateToday.setText(currentTime.toLocalDateTime().toLocalDate().toString());
		textParkName.setText(parkName);
		textReportDate.setText("from " + reportStartAndEndTimes[0].toLocalDateTime().toLocalDate().toString() + " to "
				+ reportStartAndEndTimes[1].toLocalDateTime().toLocalDate().toString());
		textTotalIncome.setText(TotalIncome + "");
		float avgIncome = 0;
		if (map.size() != 0) {
			avgIncome = TotalIncome / map.size();
		}
		textAVGIncomeDay.setText(avgIncome + "");
		incomeTable.setItems(FXCollections.observableArrayList(cellsList));
	}

	/**
	 * checks if {@link ParkEntry} is valid to report on
	 * 
	 * @param parkEntry the {@link ParkEntry} to verify
	 * @return true if entry is in the past<br>
	 *         false otherwise
	 */
	private boolean ValidOrderToReport(ParkEntry parkEntry) {
		return parkEntry.arriveTime.before(new Timestamp(System.currentTimeMillis()));
	}

	/** class representing a row in the chart */
	public class Cell {
		public float Income;
		public int Visitors;
		public String Date;

		public int getVisitors() {
			return Visitors;
		}

		public void setVisitors(int visitors) {
			this.Visitors = visitors;
		}

		public String getDate() {
			return Date;
		}

		public void setDate(String date) {
			this.Date = date;
		}

		public float getIncome() {
			return Income;
		}

		public void setIncome(float price) {
			this.Income = price;
		}

		public Cell(String date, int visitors, float price) {
			this.Date = date;
			this.Visitors = visitors;
			this.Income = price;
		}
	}

	class myDate implements Comparable<myDate> {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((date == null) ? 0 : date.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			myDate other = (myDate) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (date == null) {
				if (other.date != null)
					return false;
			} else if (!date.equals(other.date))
				return false;
			return true;
		}

		private String date;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public myDate(String date) {
			this.date = date.trim();
		}

		@Override
		public int compareTo(myDate other) {
			if (GetYear(date) > GetYear(other.getDate()))
				return 1;
			else if (GetYear(date) < GetYear(other.getDate()))
				return -1;
			else if (GetMonth(date) > GetMonth(other.getDate()))
				return 1;
			else if (GetMonth(date) < GetMonth(other.getDate()))
				return -1;
			else if (GetDay(date) > GetDay(other.getDate()))
				return 1;
			else if (GetDay(date) < GetDay(other.getDate()))
				return -1;
			return 0;
		}

		private IncomeReportController getEnclosingInstance() {
			return IncomeReportController.this;
		}
	}

	public static int GetYear(String time) {
		String[] arr = time.split("-");
		;
		return Integer.parseInt(arr[0]);
	}

	public static int GetMonth(String time) {
		String[] arr = time.split("-");
		;
		return Integer.parseInt(arr[1]);
	}

	public static int GetDay(String time) {
		String[] arr = time.split("-");
		;
		return Integer.parseInt(arr[2]);
	}

}
