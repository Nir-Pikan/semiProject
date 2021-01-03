package gui;

import entities.Subscriber;
import entities.Worker;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import module.GuiController;
import module.Navigator;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

public class LoginController implements GuiController 
{

	@FXML
	private GridPane visitorLoginForm;

	@FXML
	private Button btnUserLogin;

	@FXML
	private TextField txtId;

	@FXML
	private GridPane workerLoginForm;

	@FXML
	private Button btnWorkerLogin;

	@FXML
	private TextField txtUsername;

	@FXML
    private PasswordField txtPassword;

	@FXML
	private CheckBox cbUserWorker;
	

	/** when clicking on the user/worker check box hide/reveal wanted fields */
	@FXML
	void UserWorkerCheckBox(ActionEvent event) {
		if (cbUserWorker.isSelected()) {
			visitorLoginForm.setManaged(false);
			visitorLoginForm.setVisible(false);
			workerLoginForm.setManaged(true);
			workerLoginForm.setVisible(true);
		} 
		else
		{
			visitorLoginForm.setManaged(true);
			visitorLoginForm.setVisible(true);
			workerLoginForm.setManaged(false);
			workerLoginForm.setVisible(false);
		}
	}

	@FXML
	void WorkerLogin(ActionEvent event)
	{
		String[] data = new String[2];
		data[0] = txtUsername.getText();
		data[1] = txtPassword.getText();
		ServerRequest serverRequest = new ServerRequest(Manager.Worker,
				"LogInWorker", ServerRequest.gson.toJson(data,String[].class));
		String response = clientController.client.sendRequestAndResponse(serverRequest);
		Worker worker = ServerRequest.gson.fromJson(response, Worker.class);
		if(worker == null)
		{
			PopUp.showError("Sign up error", "Faild to log in", "Please check the user name and the password");
			return;
		}
		clientController.client.logedInWorker.setVal(worker);
		Navigator.instance().clearHistory();
	}
	
	@FXML
	void UserLogin(ActionEvent event) 
	{
		String userID = txtId.getText();
		if(isSubscriberID(userID))
		{
			LoginSubscriber(userID, "This subscriber ID not exist \nPlease check the input", true);
			return;		
		}
		if(isID(userID))
		{
			 if(LoginSubscriber("S"+userID,"",false))
				 return;
			 clientController.client.visitorID.setVal(userID);
			 Navigator.instance().clearHistory();
			 return;
		}
		PopUp.showError("Error", "Faild to identify", "Please check the input:\nID: 9 digit number\nSubscriber ID: need to start with 'S'");
	}


	//return true if success to log in the subscriber
	private boolean LoginSubscriber(String subscriberID, String ErrorMessageForPopUp, boolean needPopUpForFail)
	{
		ServerRequest serverRequest = new ServerRequest(Manager.Subscriber,
				"GetSubscriberData", subscriberID);
		String response = clientController.client.sendRequestAndResponse(serverRequest);
		if(response.endsWith("not found"))
		{
			if(needPopUpForFail)
			     PopUp.showError("Sign up error", "Faild to identify", ErrorMessageForPopUp);
			return false;
		}
		Subscriber subscriber = ServerRequest.gson.fromJson(response, Subscriber.class);
		if(subscriber == null)
		{
			if(needPopUpForFail)
			    PopUp.showError("Sign up error", "Faild to identify", ErrorMessageForPopUp);
			return false;
		}
		clientController.client.logedInSubscriber.setVal(subscriber);
		Navigator.instance().clearHistory();
		return true;
	}
	
	private boolean isSubscriberID(String idString)
	{
		if(idString.length() > 0 && idString.charAt(0) == 'S')
			return true;
		return false;
	}
	
	private boolean isID(String idString)
	{
		if(idString.length()!= 9)
			return false;
		for (char num : idString.toCharArray())
		{
			if(!isNumber(num))
				return false;
		}
		return true;
	}
	
	private boolean isNumber(char num)
	{
		return (0 <= num - '0') && (9 >= num - '0');
	}
	
	
	public void init()
	{
		workerLoginForm.setManaged(false);
		workerLoginForm.setVisible(false);	
	}

}
