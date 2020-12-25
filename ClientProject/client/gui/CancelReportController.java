package gui;


import java.sql.Timestamp;

import entities.Order;
import entities.Worker;
import entities.Order.IdType;
import entities.Order.OrderStatus;
import io.clientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import module.GuiController;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the CancelReport page controller */
public class CancelReportController implements GuiController{

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
    
    /**
     * impotant Notice:
     * @param parkName
     * @param reportStartAndEndTimes
     */ 
    public CancelReportController(String parkName, Timestamp[] reportStartAndEndTimes)            
    {
    	this.reportStartAndEndTimes = reportStartAndEndTimes;
    	this.parkName = parkName;
    }
    
    
    @FXML
    void buttonPrint_OnClick(ActionEvent event) 
    {
    	ServerRequest serverRequest = new ServerRequest(Manager.Order, "GetAllListOfOrders", "");
    	String response = clientController.client.sendRequestAndResponse(serverRequest);
    	Order[] allOrders = ServerRequest.gson.fromJson(response, Order[].class);
    	if(allOrders == null)
    		return;   	
        Canceled = 0;
        used = 0;
        NotCanceledNotUsed = 0;
        TotalOrders = 0;
    	    
    	for (Order order : allOrders)
    	{
    		AnalyzeOrder(order);
		}
    	Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    	textDateToday.setText(GetStringTime(currentTime));
    	textParkName.setText(parkName);
    	textReportDate.setText(GetStringTime(reportStartAndEndTimes[0]) + "  -  " + GetStringTime(reportStartAndEndTimes[1]));
    	textTotalOrders.setText(TotalOrders + "");
    	textOrdersConfirmed.setText(used + "");
    	textOrdersNotConfirmed.setText(NotCanceledNotUsed + "");
    	textOrdersCanceled.setText(Canceled + "");
    	ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Canceled", Canceled),
                new PieChart.Data("Arrived", used),
                new PieChart.Data("Not Arrived", NotCanceledNotUsed));
    	pieChartCancel.setData(pieChartData);
    }
    
    public String GetStringTime(Timestamp time)
    {
    	String[] arr = time.toString().split(" ")[0].split("-");
    	return arr[2] + "." + arr[1] + "." + arr[0];
    }
    
    
    private void AnalyzeOrder(Order order)
    {
    	if(!order.visitTime.before(new Timestamp(System.currentTimeMillis())))
    			return; 
    	TotalOrders++;		
    	if( order.orderStatus.equals(OrderStatus.CANCEL) || order.orderStatus.equals(OrderStatus.SEMICANCELED))
    		Canceled++;
    	else if(!order.isUsed)
    		NotCanceledNotUsed++;
    	else
  		   used++; 	   
    }

}
