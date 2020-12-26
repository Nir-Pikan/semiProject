package mocks;

import java.net.URL;
import java.util.ResourceBundle;

import io.CardReaderController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CardReaderSystem implements Initializable{

    @FXML
    private Button CloseButton;

    @FXML
    private TextField CardNumTextField;

    @FXML
    private TextField NoPeopleTextField;
    
    @FXML
    private Button ReadButton;

    @FXML
    private TableView<Card> CardsTableView;

    @FXML
    private TableColumn<Card, String> IDCol;

    @FXML
    private TableColumn<Card, Integer> NumberOfPeopleCol;

    @FXML
    private Label AckLabel;

    @FXML
    private Button VisitorExitButton;

    @FXML
    private Button VisitorEnterButton;
    
    private CardReaderController cardReaderController;
    private ObservableList<Card> data =FXCollections.observableArrayList();
    public void setController(CardReaderController cardReaderController) {
    	this.cardReaderController = cardReaderController;
    }
    

    /** Card reader reads the card and add it to a TableView **/
    @FXML
    void ReadCardID(ActionEvent event) {
    	String cardNum = CardNumTextField.getText(); // read the number from the TextFiled
    	String numberOfPeopleString= NoPeopleTextField.getText();
    	int numberOfPeople = Integer.valueOf(numberOfPeopleString);
    	Card card = new Card(cardNum,numberOfPeople);
    	data.add(card);
    	CardsTableView.setItems(data);
    }
    

    @FXML
    void VisitorEnterToThePark(ActionEvent event) {
    	Card selectedItem = CardsTableView.getSelectionModel().getSelectedItem();
    	
    	cardReaderController.enterVisitor(selectedItem.getCard(), selectedItem.getNumberOfPeople());
    	//cardReaderController.enterVisitor(selectedItem.getCard()); //method enterVisitor need to be implemented 
    	
    	
    	
    	
    	
    }
   
  
    /** Card reader recognize that the visitor exits the park and remove him from the TableView **/
	@FXML
    void VisitorExitsFromPark(ActionEvent event) {
		Card selectedItem = CardsTableView.getSelectionModel().getSelectedItem();
		CardsTableView.getItems().remove(selectedItem);
		cardReaderController.exitVisitor(selectedItem.getCard()); //method exitVisitor need to be implemented 
    }
    

    @FXML
    void closeButtonPress(ActionEvent event) {
   	 // get a handle to the stage
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) { // needed for appropriate working of TableView
    	
    	CardsTableView.setEditable(true);
		IDCol.setCellValueFactory(new PropertyValueFactory<Card,String>("card"));
		NumberOfPeopleCol.setCellValueFactory(new PropertyValueFactory<Card, Integer>("numberOfPeople"));
	//	CardsTableView.getColumns().addAll(IDCol, NumberOfPeopleCol);
		cardReaderController=new CardReaderController();
	}
    
    

}

    
    
    
