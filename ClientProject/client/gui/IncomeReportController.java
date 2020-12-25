package gui;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import entities.Order;
import entities.Order.OrderStatus;
import io.clientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

/**
 * Sample Skeleton for 'IncomeReportBoundary.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mocks.Card;
import module.GuiController;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the IncomeReport page controller */
public class IncomeReportController implements GuiController, Initializable{

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
    private TableColumn<Cell, Integer> dateColumn;

    @FXML
    private TableColumn<Cell, Integer> visitorsColumn;

    @FXML
    private TableColumn<Cell, Integer> incomeColumn;

    @FXML
    private Button buttonPrint;

    private int TotalIncome = 0;    
    
    private Timestamp[] reportStartAndEndTimes;
    private String parkName;
    
    /**
     * impotant Notice:
     * @param parkName
     * @param reportStartAndEndTimes
     */ 
    public IncomeReportController(String parkName, Timestamp[] reportStartAndEndTimes)            
    {
    	this.reportStartAndEndTimes = reportStartAndEndTimes;
    	this.parkName = parkName;
    }
    
    @FXML
    void buttonPrint_OnClick(ActionEvent event)
    {
    	TotalIncome = 0;
    	ServerRequest serverRequest = new ServerRequest(Manager.Order, "GetAllListOfOrders", "");
    	String response = clientController.client.sendRequestAndResponse(serverRequest);
    	Order[] allOrders = ServerRequest.gson.fromJson(response, Order[].class);
    	if(allOrders == null)
    		return; 
    	Map<Integer, ArrayList<Order>> map = new TreeMap<Integer, ArrayList<Order>>();
    	for (Order order : allOrders) 
    	{
    		if(ValidOrderToReport(order)) 
    		{
    			int day = order.visitTime.getDay();
    			if(!map.containsKey(day))
    			{
    				map.put(day, new ArrayList<Order>());
    			}
    			map.get(day).add(order);
    		}
		}
    	for (Map.Entry<Integer, ArrayList<Order>> entry : map.entrySet())
    	{
			int income = 0;
			int visitors = 0;
			int day = entry.getKey();
			
			for (Order order : entry.getValue()) 
			{
				visitors += order.numberOfVisitors;
				income += order.priceOfOrder;
			}
			TotalIncome += income;
				
			incomeTable.getItems().add(new Cell(income, visitors, day));
		}
    	Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    	textDateToday.setText(GetStringTime(currentTime));
    	textParkName.setText(parkName);
    	textReportDate.setText(GetStringTime(reportStartAndEndTimes[0]) + " - " + GetStringTime(reportStartAndEndTimes[1]));
    	textTotalIncome.setText(TotalIncome + ""); 
    	textAVGIncomeDay.setText((TotalIncome/map.size()) + "");
    }
    
    public String GetStringTime(Timestamp time)
    {
    	String[] arr = time.toString().split(" ")[0].split("-");
    	return arr[2] + "." + arr[1] + "." + arr[0];
    }
    
    private boolean ValidOrderToReport(Order order)
    {
    	//// need also to check if it is from the last month ///////////////////////////////////////////////////////////////
      	if(!order.visitTime.before(new Timestamp(System.currentTimeMillis())))
			return false; 		
	    if( order.orderStatus.equals(OrderStatus.CANCEL) || order.orderStatus.equals(OrderStatus.SEMICANCELED))
	    	return false; 
	    return true;
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) { // needed for appropriate working of TableView
    	dateColumn.setCellValueFactory(new PropertyValueFactory<Cell,Integer>("day"));
    	visitorsColumn.setCellValueFactory(new PropertyValueFactory<Cell,Integer>("visitors"));
    	incomeColumn.setCellValueFactory(new PropertyValueFactory<Cell,Integer>("price"));
	}

    public class Cell
    {
    	public Integer price;
    	public Integer visitors;
    	public Integer day;
    	
    	public Integer getVisitors() {
			return visitors;
		}

		public void setVisitors(Integer visitors) {
			this.visitors = visitors;
		}

		public Integer getDay() {
			return day;
		}

		public void setDay(Integer day) {
			this.day = day;
		}

		public Integer getPrice() {
			return price;
		}

		public void setPrice(Integer price) {
			this.price = price;
		}

		public Cell(Integer price,Integer visitors , Integer day)
    	{
			this.day = day;
			this.visitors = visitors;
    		this.price = price;
    	}
    }
		
	
}
