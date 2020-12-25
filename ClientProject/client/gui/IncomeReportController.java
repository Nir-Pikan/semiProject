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
    private TableColumn<Cell, String> dateColumn;

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
     * @param reportStartAndEndTimes: [0]: start, [1]: end 
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
    	Map<myDate, ArrayList<Order>> map = new TreeMap<myDate, ArrayList<Order>>();
    	for (Order order : allOrders) 
    	{
    		if(ValidOrderToReport(order)) 
    		{
    			myDate date = new myDate(GetStringTime(order.visitTime));
    			if(!map.containsKey(date))
    			{
    				map.put(date, new ArrayList<Order>());
    			}
    			map.get(date).add(order);
    		}
		}
    	for (Map.Entry<myDate, ArrayList<Order>> entry : map.entrySet())
    	{
			int income = 0;
			int visitors = 0;
			myDate date = entry.getKey();
			
			for (Order order : entry.getValue()) 
			{
				visitors += order.numberOfVisitors;
				income += order.priceOfOrder;
			}
			TotalIncome += income;
				
			incomeTable.getItems().add(new Cell(income, visitors, date.getDate()));
		}
    	Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    	textDateToday.setText(GetStringTime(currentTime));
    	textParkName.setText(parkName);
    	textReportDate.setText(GetStringTime(reportStartAndEndTimes[0]) + " - " + GetStringTime(reportStartAndEndTimes[1]));
    	textTotalIncome.setText(TotalIncome + ""); 
    	textAVGIncomeDay.setText((TotalIncome/map.size()) + "");
    }
    
    public static String GetStringTime(Timestamp time)
    {
    	String[] arr = time.toString().split(" ")[0].split("-");
    	return arr[2] + "." + arr[1] + "." + arr[0];
    }
    
    private boolean ValidOrderToReport(Order order)
    {
      	if(!order.visitTime.before(new Timestamp(System.currentTimeMillis())) || !isTimeInRangeOfStartAndEndTime(order.visitTime))
			return false; 		
	    if( order.orderStatus.equals(OrderStatus.CANCEL) || order.orderStatus.equals(OrderStatus.SEMICANCELED))
	    	return false; 
	    return true;
    }
    
    private boolean isTimeInRangeOfStartAndEndTime(Timestamp time)
    {
    	return time.before(reportStartAndEndTimes[1]) && time.after(reportStartAndEndTimes[0]);
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) { // needed for appropriate working of TableView
    	dateColumn.setCellValueFactory(new PropertyValueFactory<Cell,String>("date"));
    	visitorsColumn.setCellValueFactory(new PropertyValueFactory<Cell,Integer>("visitors"));
    	incomeColumn.setCellValueFactory(new PropertyValueFactory<Cell,Integer>("price"));
	}

    public class Cell
    {
    	private Integer price;
    	private Integer visitors;
    	private String date;
    	
    	public Integer getVisitors() {
			return visitors;
		}

		public void setVisitors(Integer visitors) {
			this.visitors = visitors;
		}

		public String getDay() {
			return date;
		}

		public void setDay(String date) {
			this.date = date;
		}

		public Integer getPrice() {
			return price;
		}

		public void setPrice(Integer price) {
			this.price = price;
		}

		public Cell(Integer price,Integer visitors , String date)
    	{
			this.date = date;
			this.visitors = visitors;
    		this.price = price;
    	}
    }
    
    class myDate implements Comparable<myDate>
    {
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

		public myDate( String date)
    	{
    		this.date = date.trim();
    	}
    	
		@Override
		public int compareTo(myDate other) 
		{
			if(GetYear(date) > GetYear(other.getDate()))
				return 1;
			else if(GetYear(date) < GetYear(other.getDate()))
				return -1;
			else if(GetMonth(date) > GetMonth(other.getDate()))
				return 1;
			else if(GetMonth(date) < GetMonth(other.getDate()))
				return -1;
			else if(GetDay(date) > GetDay(other.getDate()))
				return 1;
			else if(GetDay(date) < GetDay(other.getDate()))
				return -1;
			return 0;
		}	

		private IncomeReportController getEnclosingInstance() {
			return IncomeReportController.this;
		}
	}

    public static int GetYear(String time)
    {
    	String[] arr = time.split(".");;
    	return Integer.parseInt(arr[2]);
    }
    
    public static int GetMonth(String time)
    {
    	String[] arr = time.split(".");;
    	return Integer.parseInt(arr[1]);
    }
    
    public static int GetDay(String time)
    {
    	String[] arr = time.split(".");;
    	return Integer.parseInt(arr[0]);
    }
		
	
}
