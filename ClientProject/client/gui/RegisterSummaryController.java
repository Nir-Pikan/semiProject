package gui;

import entities.Subscriber;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

public class RegisterSummaryController implements GuiController{
	
	private Subscriber s;

    @FXML
    private Label LastNameLabel;

    @FXML
    private Label SummaryTitleLabel;

    @FXML
    private Label FirstNameLabel;

    @FXML
    private Label IDLabel;

    @FXML
    private Label PhoneNumberLabel;

    @FXML
    private Label EmailLabel;

    @FXML
    private Label TypeLabel;

    @FXML
    private Label FamilySizeLabel;

    @FXML
    private Button SubmitBtn;

    @FXML
    private Label DetailsTimelineLabel;

    @FXML
    private Label AddCreditCardTimelineLabel;

    @FXML
    private Label SummaryTimelineLabel;

    @FXML
    private Label FirstNameContantLabel;

    @FXML
    private Label LastNameContantLabel;

    @FXML
    private Label IDContantLabel;

    @FXML
    private Label PhoneNumberContantLabel;

    @FXML
    private Label EmailContantLabel;

    @FXML
    private Label TypeContantLabel;

    @FXML
    private Label FamilySizeContantLabel;

    @FXML
    private Label CardNumberContantLabel;

    @FXML
    private Label CreditCardLabel;
    
    /**
     * Submit the registration*/
    @FXML
    void FinishRegistration(ActionEvent event) {
    	
    	//send request to add subscriber to clientController
    	String response = clientController.client.sendRequestAndResponse(new ServerRequest(
    			Manager.Subscriber, "AddNewSubscriber" , ServerRequest.gson.toJson(s,Subscriber.class)));
    	
    	switch(response) {
    	case "Subscriber was added successfully":
    		PopUp.showInformation("Register success","Register success","Subscriber registered successfully!\n"
    				+ "Your Subscriber ID is:\n" + s.subscriberID);
    		
    		//after registration return to home page and forget the history
        	Navigator.instance().clearHistory();
    		break;
    		
    	case "Failed to add Subscriber":
    		PopUp.showInformation("Register failed","Register failed","Subscriber register failed!");
    		break;
    		
    	case "Subscriber already exists":
    		PopUp.showInformation("Register failed","Register failed","Subscriber already exists!");
    		break;
    	
    	default:
    		PopUp.showInformation("Unexpected error","Unexpected error!","server returned an unexpected response");
    	}
    	
    }

    /**
     * Receive subscriber from previous page*/
	public void addSub(Subscriber s) {
		this.s = s;
		FieldsInit();
	}

	/**
	 * initialize the fields using the received subscriber*/
	public void FieldsInit() {
		FirstNameContantLabel.setText(s.firstName);
		LastNameContantLabel.setText(s.lastName);
		IDContantLabel.setText(s.personalID);
		PhoneNumberContantLabel.setText(s.phone);
		EmailContantLabel.setText(s.email);
		TypeContantLabel.setText(s.type.toString());
		FamilySizeContantLabel.setText(String.valueOf(s.familySize));
		if(s.creditCardNumber != null)
			CardNumberContantLabel.setText(s.creditCardNumber);
		else 
			CardNumberContantLabel.setText("none");
	}
	
}
