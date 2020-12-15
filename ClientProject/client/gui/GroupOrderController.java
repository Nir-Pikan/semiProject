package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import module.GuiController;

public class GroupOrderController implements GuiController{

    @FXML
    private ComboBox<String> Park_ComboBox;

    @FXML
    private ComboBox<String> VisitHour_ComboBox;

    @FXML
    private ComboBox<String> NumberOfVisitors_ComboBox;

    @FXML
    private TextField Email_textBox;

    @FXML
    private DatePicker Date_DatePicker;

    @FXML
    private CheckBox FamilyIndicator_checkBox;
    
    @FXML
    private Button PlaceOrder_Button;

    @FXML
    void PlaceOrder_Button_Clicked(ActionEvent event) {

    }

	public void setSpontaneous(String ordererId) {
		// TODO Auto-generated method stub
		
	}


	public void setFamilyOrderOnly() {
		// TODO Auto-generated method stub
		
	}

}
