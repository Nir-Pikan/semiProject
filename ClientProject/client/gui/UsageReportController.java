package gui;

/**
 * Sample Skeleton for 'UsageReportBoundary.fxml' Controller Class
 */

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;

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
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the UsageReport page controller */
public class UsageReportController implements GuiController {

	@FXML
	private Label labelDateToday;

	@FXML
	private Label textDateToday;

	@FXML
	private Label labelUsageReport;

	@FXML
	private Label labelParkName;

	@FXML
	private Label textParkName;

	@FXML
	private Label labelReportDate;

	@FXML
	private Label textReportDate;

	@FXML
	private Label labelMaxCapacity;

	@FXML
	private Label textMaxCapacity;

	@FXML
	private TableView<UsageRow> visitorUsageTable;

	@FXML
	private TableColumn<UsageRow, String> dateColumn;

	@FXML
	private TableColumn<UsageRow, Integer> visitorsColumn;

	@FXML
	private TableColumn<UsageRow, Double> usageColumn;

	@FXML
	private Button buttonPrint;

	@FXML
	void printButton_OnClick(ActionEvent event) {
		// JavafxPrinter.printThisWindow(buttonPrint.getScene().getWindow());
	}

	/**
	 * Initialize the monthly usage report using the parameters
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
//=========== Count all visitors in each day ======================================================================
			int[] visitorsPerDay = new int[YearMonth
					.of(reportDate[1].toLocalDateTime().getYear(), reportDate[1].toLocalDateTime().getMonth())
					.lengthOfMonth()];
			int amountOfDays = visitorsPerDay.length;

			ParkEntry[] entries = ServerRequest.gson.fromJson(response, ParkEntry[].class);

			// for each entry
			for (ParkEntry entry : entries) {

				// only for wanted park
				if (entry.parkID.equals(parkID))
					visitorsPerDay[entry.arriveTime.toLocalDateTime().getDayOfMonth() - 1] += entry.numberOfVisitors;
			}
//=================================================================================================================	

//=========== get wanted park parameters to calculate usage% ====================================================== 

			// send request to get parks to clientController
			String response2 = clientController.client
					.sendRequestAndResponse(new ServerRequest(Manager.Park, "get current parameter", parkID));

			String[] parkParameters = ServerRequest.gson.fromJson(response2, String[].class);

			textMaxCapacity.setText(parkParameters[0]);
			int maxCapacity = Integer.parseInt(parkParameters[0]);
//=================================================================================================================		

//=========== create the table ==================================================================================== 			
			dateColumn.setCellValueFactory(new PropertyValueFactory<UsageRow, String>("date"));
			visitorsColumn.setCellValueFactory(new PropertyValueFactory<UsageRow, Integer>("visitors"));
			usageColumn.setCellValueFactory(new PropertyValueFactory<UsageRow, Double>("usage"));

			ArrayList<UsageRow> rows = new ArrayList<>();

			// create all rows
			for (int i = 0; i < amountOfDays; i++) {
				String day = i + "." + reportDate[0].toLocalDateTime().getMonth();
				double usage = ((double) visitorsPerDay[i]) / maxCapacity;
				usage *= 100;
				UsageRow row = new UsageRow(day, visitorsPerDay[i], usage);
				rows.add(row);
			}

			visitorUsageTable.setItems(FXCollections.observableArrayList(rows));
//=================================================================================================================
		}
	}

	/**
	 * private class to create rows for the usage table
	 */
	private class UsageRow {
		protected String date;
		protected int visitors;
		protected double usage;

		public UsageRow(String date, int visitors, double usage) {
			this.date = date;
			this.visitors = visitors;
			this.usage = usage;
		}
	}
}
