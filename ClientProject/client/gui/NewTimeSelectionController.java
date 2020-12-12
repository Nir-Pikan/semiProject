package gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import module.GuiController;

public class NewTimeSelectionController implements GuiController{

    @FXML
    private ComboBox<String> VisitHour_ComboBox;

    @FXML
    private DatePicker Date_DatePicker;

    @FXML
    private Button PlaceOrder_Button;

    @FXML
    private TableView<Times> OrdersTable_TableView;

    @FXML
    private TableColumn<Times, String> timesCul;

    @FXML
    private TableColumn<Times, String> AvailabilityCul;

    
    @FXML
    void Date_DatePicker_Selected(ActionEvent event) {

    }

    @FXML
    void PlaceOrder_Button_Clicked(ActionEvent event) {

    }

    
    @Override
    public void init() {
    	timesCul.setCellValueFactory(new PropertyValueFactory<Times, String>("time"));
    	AvailabilityCul.setCellValueFactory(new PropertyValueFactory<Times, String>("availability"));
    	//OrdersTable_TableView.setItems(FXCollections.observableArrayList(new Times("12:00","full"),new Times("13:00","Not full")));
    }
    
    /**class for the availability table*/
    public class Times{
    	public String time;
    	public String availability;
    	
		public Times(String time, String availability) {
			this.time = time;
			this.availability = availability;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getAvailability() {
			return availability;
		}

		public void setAvailability(String availability) {
			this.availability = availability;
		}
		
    }
}
