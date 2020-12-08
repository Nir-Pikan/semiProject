package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import module.GuiController;

public class ParksDetailsPageController implements GuiController{

    @FXML
    private TableView<?> parksDetailsPageTableView;

    @FXML
    private TableColumn<?, ?> parkNum;

    @FXML
    private TableColumn<?, ?> currentMaxVisitorsLimit;

    @FXML
    private TableColumn<?, ?> maxVisitorsLimitRequest;

    @FXML
    private TableColumn<?, ?> currentAvgVisitTime;

    @FXML
    private TableColumn<?, ?> AvgVisitTimeRequest;

    @FXML
    private TableColumn<?, ?> currentMaxPreorders;

    @FXML
    private TableColumn<?, ?> maxPreordersRequest;

    @FXML
    private Button ExitButton;

}
