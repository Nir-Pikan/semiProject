package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SmallGroupOrderController {

    @FXML
    private ComboBox<?> Park_ComboBox;

    @FXML
    private ComboBox<?> VisitHour_ComboBox;

    @FXML
    private TextField Email_textBox;

    @FXML
    private DatePicker Date_DatePicker;

    @FXML
    private Button PlaceOrder_Button;

    @FXML
    private TextField Phone_textBox;

    @FXML
    private ListView<?> listViewVisitors;

    @FXML
    private Button AddVisitor_Button;

    @FXML
    void AddVisitor_Button_Clicked(ActionEvent event) {

    }

    @FXML
    void PlaceOrder_Button_Clicked(ActionEvent event) {

    }

}
