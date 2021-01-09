package clientGui;

import java.sql.Timestamp;

import clientIO.clientController;
import entities.Order;
import entities.Order.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import modules.JavafxPrinter;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the CancelReport page controller */
public class CancelReportController implements GuiController, Report {

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
	private Label textTotalOrders;

	@FXML
	private Label labelTotalOrders;

	@FXML
	private Label labelOrdersConfirmed;

	@FXML
	private Label textOrdersConfirmed;

	@FXML
	private Button buttonPrint;

	@FXML
	private Label labelOrdersNotConfirmed;

	@FXML
	private Label textOrdersNotConfirmed;

	@FXML
	private Label labelOrdersCanceled;

	@FXML
	private Label textOrdersCanceled;

	@FXML
	private PieChart pieChartCancel;

	private int Canceled = 0;
	private int used = 0;
	private int NotCanceledNotUsed = 0;
	private int TotalOrders = 0;
	private Timestamp[] reportStartAndEndTimes;
	private String parkName;

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
		createReport();
	}

	/**
	 * creates the report
	 */
	void createReport() {
		String[] _requestData = { ServerRequest.gson.toJson(reportStartAndEndTimes[0], Timestamp.class),
				ServerRequest.gson.toJson(reportStartAndEndTimes[1], Timestamp.class), parkName };
		String requestData = ServerRequest.gson.toJson(_requestData, String[].class);
		ServerRequest serverRequest = new ServerRequest(Manager.Order, "GetOrderListForDate", requestData);
		String response = clientController.client.sendRequestAndResponse(serverRequest);
		Order[] allOrders = ServerRequest.gson.fromJson(response, Order[].class);
		if (allOrders == null)
			return;
		Canceled = 0;
		used = 0;
		NotCanceledNotUsed = 0;
		TotalOrders = 0;

		for (Order order : allOrders) {
			AnalyzeOrder(order);
		}
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		textDateToday.setText(currentTime.toLocalDateTime().toLocalDate().toString());
		textParkName.setText(parkName);
		textReportDate.setText("from " + reportStartAndEndTimes[0].toLocalDateTime().toLocalDate().toString() + " to "
				+ reportStartAndEndTimes[1].toLocalDateTime().toLocalDate().toString());
		textTotalOrders.setText(TotalOrders + "");
		textOrdersConfirmed.setText(used + "");
		textOrdersNotConfirmed.setText(NotCanceledNotUsed + "");
		textOrdersCanceled.setText(Canceled + "");
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Canceled", Canceled), new PieChart.Data("Arrived", used),
				new PieChart.Data("Not Arrived", NotCanceledNotUsed));
		pieChartCancel.setData(pieChartData);
	}

	/**
	 * update report counters according to the {@link Order}
	 * 
	 * @param order the order we update counters according to
	 */
	private void AnalyzeOrder(Order order) {
		if (!order.visitTime.before(new Timestamp(System.currentTimeMillis())))
			return;
		TotalOrders++;
		if (order.orderStatus.equals(OrderStatus.CANCEL) || order.orderStatus.equals(OrderStatus.SEMICANCELED))
			Canceled++;
		else if (!order.isUsed)
			NotCanceledNotUsed++;
		else
			used++;
	}

}
