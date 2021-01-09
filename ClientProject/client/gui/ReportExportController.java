package gui;

import java.sql.Timestamp;
import java.time.LocalDate;

import entities.ParkNameAndTimes;
import entities.Worker;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** the ReportExport page controller */
public class ReportExportController implements GuiController {

	@FXML
	private Label labelParkManager;

	@FXML
	private Label labelParkName;

	@FXML
	private ComboBox<ReportType> ReportSelectionComboBox;

	@FXML
	private Label labelReportSelection;

	@FXML
	private Button buttonGetReport;

	@FXML
	private ComboBox<String> ParkSelectionComboBox;

	@FXML
	private ComboBox<String> monthSelction;

	@FXML
	private ComboBox<String> yearSelction;

	@FXML
	private Label LabelParkId;

	@FXML
	private TextField textParkId;

	/** create the asked report in a popup */
	@FXML
	void getReoprt_OnClick(ActionEvent event) {
		if (!CheckAllRequiredFields())
			return;

		String selectedMonth = monthSelction.getValue();
		LocalDate startOfMonthDay = LocalDate.of(Integer.parseInt(yearSelction.getValue()),
				Months.valueOf(selectedMonth).ordinal() + 1, 1);
		Timestamp startOfMonth = Timestamp.valueOf(startOfMonthDay.atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(startOfMonthDay.plusMonths(1).atStartOfDay());

		Report r;
		switch (ReportSelectionComboBox.getValue()) {
		case Cancel:
			r = (Report) PopUp.showCostumContent("CancelReport", "CancelReport.fxml");
			break;
		case Entry:
			r = (Report) PopUp.showCostumContent("EntryReport", "EntryReport.fxml");
			break;
		case Income:
			r = (Report) PopUp.showCostumContent("IncomeReport", "IncomeReport.fxml");
			break;
		case Usage:
			r = (Report) PopUp.showCostumContent("UsageReport", "UsageReport.fxml");
			break;
		case Visitor:
			r = (Report) PopUp.showCostumContent("VisitorsReport", "VisitorsReport.fxml");
			break;
		default:
			return;
		}
		Timestamp[] dates = new Timestamp[] { startOfMonth, endOfMonth };
		r.initReport(textParkId.getText(), ParkSelectionComboBox.getValue(), dates);
	}

	@Override
	public void init() {
		Worker w = clientController.client.logedInWorker.getVal();
		ParkSelectionComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			ParkNameAndTimes p = clientController.client.openingTimes.get(newVal);
			if (p == null) {
				textParkId.setText("");
				return;
			}
			textParkId.setText(p.parkID);
		});
		String[] monthStrings = new String[Months.values().length];

		for (int j = 0; j < monthStrings.length; j++) {

			monthStrings[j] = Months.values()[j].toString();
		}

		monthSelction.getItems().clear();
		monthSelction.getItems().addAll(monthStrings);
		yearSelction.getItems().clear();
		yearSelction.getItems().addAll("2020", "2021");
		if (w.getWorkerType().equals("departmentManager")) {
			ParkSelectionComboBox.getItems().addAll(clientController.client.parkNames);
			ReportSelectionComboBox.getItems().addAll(ReportType.Entry, ReportType.Cancel);

		} else {
			ParkSelectionComboBox.getItems().add(w.getPermissions().GetParkID());
			ParkSelectionComboBox.getSelectionModel().clearAndSelect(0);
			ParkSelectionComboBox.setDisable(true);
			textParkId.setText(w.getPermissions().GetParkID());
			ReportSelectionComboBox.getItems().addAll(ReportType.Visitor, ReportType.Usage, ReportType.Income);
		}

	}

	/** checks if all required fields are filled */
	private boolean CheckAllRequiredFields() {
		boolean res = true;
		res &= CheckParkName();
		res &= CheckParkId();
		res &= CheckReportSelection();
		res &= CheckMonthSelection();
		res &= CheckYearSelection();

		return res;
	}

	/**
	 * checks if a year was selected
	 * 
	 * @return true if year was selected<br>
	 *         false otherwise
	 */
	private boolean CheckYearSelection() {
		if (yearSelction.getValue() == null) {
			yearSelction.getStyleClass().add("error");
			return false;
		}
		yearSelction.getStyleClass().remove("error");
		return true;
	}

	/**
	 * checks if a month was selected
	 * 
	 * @return true if month was selected<br>
	 *         false otherwise
	 */
	private boolean CheckMonthSelection() {
		if (monthSelction.getValue() == null) {
			monthSelction.getStyleClass().add("error");
			return false;
		}
		monthSelction.getStyleClass().remove("error");
		return true;
	}

	/**
	 * checks if a type of report was selected
	 * 
	 * @return true if type was selected<br>
	 *         false otherwise
	 */
	private boolean CheckReportSelection() {

		if (ReportSelectionComboBox.getValue() == null) {
			ReportSelectionComboBox.getStyleClass().add("error");
			return false;
		}

		ReportSelectionComboBox.getStyleClass().remove("error");
		return true;
	}

	/**
	 * checks if a park ID was selected
	 * 
	 * @return true if park ID was selected<br>
	 *         false otherwise
	 */
	private boolean CheckParkId() {

		if (textParkId.getText() == null) {
			textParkId.getStyleClass().add("error");
			return false;
		}
		if (textParkId.getText().equals("")) {
			textParkId.getStyleClass().add("error");
			return false;
		}
		textParkId.getStyleClass().remove("error");
		return true;
	}

	/**
	 * checks if a park name was entered
	 * 
	 * @return true if park name was entered<br>
	 *         false otherwise
	 */
	private boolean CheckParkName() {
		if (ParkSelectionComboBox.getValue() == null) {
			ParkSelectionComboBox.getStyleClass().add("error");
			return false;
		}
		String parkNameString = ParkSelectionComboBox.getValue();
		if (parkNameString.equals("")) {
			ParkSelectionComboBox.getStyleClass().add("error");
			return false;
		}
		ParkSelectionComboBox.getStyleClass().remove("error");
		return true;
	}

	private enum ReportType {
		Visitor, Entry, Usage, Income, Cancel
	}

	public enum Months {

		January, February, March, April, May, June, July, August, September, October, November, December;

	}
}
