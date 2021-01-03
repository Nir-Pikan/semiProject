package gui;

/**
 * Sample Skeleton for 'UsageReportBoundary.fxml' Controller Class
 */

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import entities.ParkEntry;
import entities.ParkNameAndTimes;
import io.clientController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import module.GuiController;
import module.JavafxPrinter;
import module.Report;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the UsageReport page controller */
public class UsageReportController implements GuiController, Report {

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
	private TreeTableView<UsageRow> usageTreeTable;

	@FXML
	private TreeTableColumn<UsageRow, String> DateCol;

	@FXML
	private TreeTableColumn<UsageRow, Number> visitorsCol;

	@FXML
	private TreeTableColumn<UsageRow, Number> usageCol;

	@FXML
	private Button buttonPrint;

	@FXML
	void printButton_OnClick(ActionEvent event) {
		JavafxPrinter.printThisWindow(buttonPrint.getScene().getWindow());
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
		textDateToday.setText(LocalDate.now().toString());
		textParkName.setText(parkName);
		textReportDate.setText("From " + reportDate[0].toLocalDateTime().toLocalDate().toString() + " To "
				+ reportDate[1].toLocalDateTime().toLocalDate().toString());

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
	
			DateCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UsageRow,String>, ObservableValue<String>>() {
				
				@Override
				public ObservableValue<String> call(CellDataFeatures<UsageRow, String> param) {
					return param.getValue().getValue().getDate();
				}
			});
			DateCol.setSortable(false);
			
			visitorsCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UsageRow,Number>, ObservableValue<Number>>() {
				
				@Override
				public ObservableValue<Number> call(CellDataFeatures<UsageRow, Number> param) {
					return param.getValue().getValue().getVisitors();
				}
			});
			visitorsCol.setSortable(false);

			usageCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UsageRow,Number>, ObservableValue<Number>>() {
				
		
				@Override
				public ObservableValue<Number> call(CellDataFeatures<UsageRow, Number> param) {
					return param.getValue().getValue().getUsage();
				}
			});
			usageCol.setSortable(false);
			
			
			ParkNameAndTimes p = clientController.client.openingTimes.get(parkID);

			String dates = reportDate[0].toLocalDateTime().toLocalDate().toString() + " to "
					+ reportDate[1].toLocalDateTime().toLocalDate().toString();
			int totalVisitors = 0;
			double totalusage = 0;
			for (int i = 0; i < visitorsPerDay.length; i++) {
				totalVisitors += visitorsPerDay[i];
				double usage = ((double) visitorsPerDay[i])
						/ (maxCapacity * (p.closeTime - p.openTime) / Double.parseDouble(parkParameters[2]));
				totalusage += usage;
			}
			totalusage /= amountOfDays;
			TreeItem<UsageRow> rootData = new TreeItem<UsageRow>(new UsageRow(dates, totalVisitors, totalusage));

			for (int i = 0; i < amountOfDays; i++) {
				// define day item
				String day = i + 1 + "-" + reportDate[0].toLocalDateTime().getMonth().toString().toLowerCase();
				double usage = ((double) visitorsPerDay[i])
						/ (maxCapacity * (p.closeTime - p.openTime) / Double.parseDouble(parkParameters[2]));
				usage *= 10000;
				usage =((int)usage)/100.0;
				TreeItem<UsageRow> dayItem = new TreeItem<UsageRow>(new UsageRow(day, visitorsPerDay[i], usage));
				int[] visitorsAtHour = new int[24];
				for (int j = 0; j < visitorsAtHour.length; j++) {
					visitorsAtHour[j] = 0;
				}
				for (int j = p.openTime; j <= p.closeTime; j++) {
					String hourString = j + ":00";
					int visitors = 0;
					for (ParkEntry entry : entries) {
						// only for wanted park and hour
						if (entry.parkID.equals(parkID) && entry.arriveTime.toLocalDateTime().getHour() <= j
								&& entry.exitTime.toLocalDateTime().getHour() >= j && entry.arriveTime.toLocalDateTime().toLocalDate().getDayOfMonth()==(i+1))
							visitors += entry.numberOfVisitors;
					}
					usage = ((double) visitors)
							/ (maxCapacity ) ;
					
				
					usage *= 10000;
					usage =((int)usage)/100.0;
					TreeItem<UsageRow> hourItem = new TreeItem<UsageRow>(new UsageRow(hourString, visitors, usage));
					dayItem.getChildren().add(hourItem);

				}
				rootData.getChildren().add(dayItem);
			}
			
			  
			usageTreeTable.setRoot(rootData);
			usageTreeTable.setShowRoot(false);
		}

	}

	/**
	 * private class to create rows for the usage table
	 */
	public static class UsageRow {
		SimpleStringProperty date;
		SimpleIntegerProperty visitors;
		SimpleDoubleProperty usage;
		
		public UsageRow(String date, int visitors, double usage) {
			this.date = new SimpleStringProperty(date);
			this.visitors =  new SimpleIntegerProperty(visitors);
			this.usage = new SimpleDoubleProperty(usage);
		}
		public void setDate(String date) {
			this.date = new SimpleStringProperty(date);;
		}
		
		public void setVisitors(int visitors) {
			this.visitors = new SimpleIntegerProperty(visitors);
		}
		
		public void setUsage(double usage) {
			this.usage = new SimpleDoubleProperty(usage);
		}

		/**
		 * @return the date
		 */
		public SimpleStringProperty getDate() {
			return date;
		}

		/**
		 * @return the visitors
		 */
		public SimpleIntegerProperty getVisitors() {
			return visitors;
		}

		/**
		 * @return the usage
		 */
		public SimpleDoubleProperty getUsage() {
			return usage;
		}

	}
}
