package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import module.GuiController;

public class NewTimeSelectionController implements GuiController{

    @FXML
    private ComboBox<String> VisitHour_ComboBox;

    @FXML
    private DatePicker Date_DatePicker;

    @FXML
    private Button PlaceOrder_Button;

    @FXML
    private TableView<String> OrdersTable_TableView;

    @FXML
    void Date_DatePicker_Selected(ActionEvent event) {

    }

    @FXML
    void PlaceOrder_Button_Clicked(ActionEvent event) {

    }

}
