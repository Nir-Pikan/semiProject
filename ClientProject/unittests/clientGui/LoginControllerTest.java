package clientGui;

import static org.junit.jupiter.api.Assertions.*;

import java.security.Permissions;
import java.util.PrimitiveIterator.OfDouble;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clientIO.ConnectionInterface;
import clientIO.UserInterface;
import clientIO.clientController;
import entities.Subscriber;
import entities.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import junit.framework.Assert;
import entities.Subscriber.Type;
import modules.Property;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

class LoginControllerTest {
	static String errMsg = null;
	static String response = null;
	static ServerRequest expectedRequest = null;
	static boolean historyCleaned = false;
	private JFXPanel panel = new JFXPanel();
	
	LoginController cont;
	
	@BeforeEach
	void setUp() throws Exception 
	{
		setMocks();
		errMsg="";
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LoginController.class.getResource("logIn.fxml"));
		loader.load();

		cont = loader.getController();
		
		historyCleaned = false;
	}
	
	static void setMocks() 
	{
	  PopUp.myPop = new PopUp.IPopUp()
	  {
		
		@Override
		public void showInformation(String title, String header, String body) 
		{
			
		}
		
		@Override
		public void showError(String title, String header, String body) 
		{
			errMsg = body;
		}
		
		@Override
		public boolean ask(String title, String header, String body) 
		{
			return false;
		}
	  };	
	
	clientController.serverConnection = new ConnectionInterface() 
	{	
		@Override
		public String sendRequestAndResponse(ServerRequest request) 
		{
			if(expectedRequest.data.equals(request.data) && expectedRequest.job.equals(request.job) &&
					expectedRequest.manager.equals(request.manager))
			{
			    return response;
			}
			return "";		
		}
	};
	
	clientController.users = new UserInterface() 
	{
		Property<String> VisitorID = new Property<String>();
		Property<Subscriber> LogedInSubscriber = new Property<Subscriber>();
		Property<Worker> LogedInWorker = new Property<Worker>();
		
		@Override
		public void setVisitorID(Property<String> visitorID) 
		{
			VisitorID = visitorID;
		}
		
		@Override
		public void setLogedInWorker(Property<Worker> logedInWorker) 
		{
			LogedInWorker = logedInWorker;			
		}
		
		@Override
		public void setLogedInSubscriber(Property<Subscriber> logedInSubscriber) 
		{
			LogedInSubscriber = logedInSubscriber;			
		}
		
		@Override
		public Property<String> getVisitorID() 
		{
			return VisitorID;
		}
		
		@Override
		public Property<Worker> getLogedInWorker()
		{
			return LogedInWorker;
		}
		
		@Override
		public Property<Subscriber> getLogedInSubscriber() 
		{
			return LogedInSubscriber;
		}
	};
	
	Navigator.setNavigator(new NavigatorInterface() 
	{
		
		@Override
		public GuiController navigate(String destenation)
		{
			return null;
		}
		
		@Override
		public void clearHistory(String fxml) 
		{
			historyCleaned = true;
		}
		
		@Override
		public void clearHistory() 
		{
			historyCleaned = true;
		}
		
		@Override
		public void back() 
		{
			
		}
	});
	}

	/**
	 * check if the input is valid ID that not match to any subscriber is loged in as ID
       input: valid ID
       expected: loged in as ID
	 */
	@Test
	void IDCorrectFormat_NotBelongsToSubscriber_EnterAsID_success() 
	{
		String correctID = "123456789";
		cont.txtId.setText(correctID);
		expectedRequest = new ServerRequest(Manager.Subscriber, "GetSubscriberData", "S" + correctID);
		response = "not found";
		
		cont.btnUserLogin.fire(); 
		
		assertTrue(historyCleaned);
		assertTrue(clientController.users.getVisitorID().getVal().equals(correctID));
	}
	
	/**
	 * wrong ID login
	 * input: too short number to ID
	 * expected: to not log in and pop with appropriet message
	 */
	@Test
	void IDWrongFormat_lessThan9NumbersDigits_Fail()
	{
		String wrongID = "12345678";
		cont.txtId.setText(wrongID);
		
		cont.btnUserLogin.fire(); 
		
		assertFalse(historyCleaned);
		assertTrue(errMsg.equals("Please check the input:\nID: 9 digit number\nSubscriber ID: need to start with 'S'"));
	}
	
	/**
	 *  wrong ID login
	 *  input: too long number to ID
        excpect: to not log in and pop with appropriet message
	 */
	@Test
	void IDWrongFormat_MoreThan9NumbersDigits_Fail() 
	{
		String wrongID = "0123456789";
		cont.txtId.setText(wrongID);
		
		cont.btnUserLogin.fire(); 
		
		assertFalse(historyCleaned);
		assertTrue(errMsg.equals("Please check the input:\nID: 9 digit number\nSubscriber ID: need to start with 'S'"));
	}
	
	/**
	 * wrong ID login
	 * input: not valid ID
       excpect: to not log in and pop with appropriet message
	 */
	@Test
	void IDWrongFormat_9DigitsButAllNumbers_Fail() 
	{
		String wrongID = "123X56789";
		cont.txtId.setText(wrongID);
		
		cont.btnUserLogin.fire(); 
		
		assertFalse(historyCleaned);
		assertTrue(errMsg.equals("Please check the input:\nID: 9 digit number\nSubscriber ID: need to start with 'S'"));
	}
	
	/**
	 * success login ID of subscriber
	 * input: valid ID of subscriber
	 * excpect: to log in as subscriber
	 */
	@Test
	void IDCorrectFormat_BelongsToSubscriber_EnterAsSubscriber_success() 
	{
		String IDBelongToSubscriber = "123456789";
		Subscriber subscriber = new Subscriber("S"+IDBelongToSubscriber, IDBelongToSubscriber, "Aviv", "Or", "0541234567", "nir@gmail.com", 1, Type.SUBSCRIBER);
		cont.txtId.setText(IDBelongToSubscriber);
		expectedRequest = new ServerRequest(Manager.Subscriber, "GetSubscriberData", "S" + IDBelongToSubscriber);
		response = ServerRequest.gson.toJson(subscriber, Subscriber.class);
		
		cont.btnUserLogin.fire(); 
		
		assertTrue(historyCleaned);
		assertTrue(clientController.users.getLogedInSubscriber().getVal().equals(subscriber));	
	}
	
	/**
	 * success login of subscriber
	 * input: valid subscriberID
       excpect: to log in as subscriber
	 */
	@Test
	void SubscriberIDCorrectThatExist_Success() 
	{
		String SubscriberID = "S123456789";
		Subscriber subscriber = new Subscriber(SubscriberID, "123456789", "Aviv", "Or", "0541234567", "nir@gmail.com", 1, Type.SUBSCRIBER);
		cont.txtId.setText(SubscriberID);
		expectedRequest = new ServerRequest(Manager.Subscriber, "GetSubscriberData", SubscriberID);
		response = ServerRequest.gson.toJson(subscriber, Subscriber.class);
		
		cont.btnUserLogin.fire(); 
		
		assertTrue(historyCleaned);
		assertTrue(clientController.users.getLogedInSubscriber().getVal().equals(subscriber));	
	}
	
	/**
	 * success login of subscriber
	 * input: valid subscriberID id is diffrent but still
	 * excpect: to log in as subscriber
	 */
	@Test
	void SubscriberIDCorrectThatExistWithDifrrentID_Success() 
	{
		String SubscriberID = "S123456789";
		String DiffID = "000000000";
		Subscriber subscriber = new Subscriber(SubscriberID, DiffID, "Aviv", "Or", "0541234567", "nir@gmail.com", 1, Type.SUBSCRIBER);
		cont.txtId.setText(SubscriberID);
		expectedRequest = new ServerRequest(Manager.Subscriber, "GetSubscriberData", SubscriberID);
		response = ServerRequest.gson.toJson(subscriber, Subscriber.class);
		
		cont.btnUserLogin.fire(); 
		
		assertTrue(historyCleaned);
		assertTrue(clientController.users.getLogedInSubscriber().getVal().equals(subscriber));	
	}
	
	/**
	 * wrong subscriber login
	 * input: not existing subscriberID
	 * excpect: to not log in and pop with appropriet message
	 */
	@Test
	void SubscriberNotExist_Fail() 
	{
		String SubscriberID = "S123456789";
		Subscriber subscriber = null;
		cont.txtId.setText(SubscriberID);
		expectedRequest = new ServerRequest(Manager.Subscriber, "GetSubscriberData", SubscriberID);
		response = ServerRequest.gson.toJson(subscriber, Subscriber.class);
		
		cont.btnUserLogin.fire(); 
		
		assertFalse(historyCleaned);
		assertTrue(errMsg.equals("This subscriber ID not exist \nPlease check the input"));
	}
	
	/**
	 * wrong subscriber login
	 * input: not existing subscriberID
	 * excpect: to not log in and pop with appropriet message
	 */
	@Test
	void SubscriberWrongFormat_Fail() 
	{
		String WrongSubscriberID = "X123456789";
		cont.txtId.setText(WrongSubscriberID);
		
		cont.btnUserLogin.fire(); 
		
		assertFalse(historyCleaned);
		assertTrue(errMsg.equals("Please check the input:\nID: 9 digit number\nSubscriber ID: need to start with 'S'"));
	}
	
	/**
	 *  success login of Worker
	 *  input: valid worker UserName and Password
	 *  excpect: to log in as Worker
	 */
	@Test
	void WorkerCorrectExist_Success() 
	{
		String WorkerUserName = "UserName";
		String WorkerPassword = "1234";
		Worker worker = new Worker(WorkerUserName, "Aviv", "Michael", "123456789", "roman@gmail.com", "Manager",
				WorkerPassword, false, null);
		cont.txtUsername.setText(WorkerUserName);
		cont.txtPassword.setText(WorkerPassword);
		String[] data = new String[2];
		data[0] = WorkerUserName;
		data[1] = WorkerPassword;
		expectedRequest = new ServerRequest(Manager.Worker, "LogInWorker",
				ServerRequest.gson.toJson(data, String[].class));
		response = ServerRequest.gson.toJson(worker, Worker.class);
		
		cont.btnWorkerLogin.fire(); 
		
		assertTrue(historyCleaned);
		assertTrue(clientController.users.getLogedInWorker().getVal().getUserName().equals(worker.getUserName()));	
	}
	
	/**
	 *  wrong login of Worker
	 *  input: not existing worker UserName and Password
	 *  excpect: to not log in and pop with appropriet message
	 */
	@Test
	void WorkerNotExist_Fail() 
	{
		String WorkerUserName = "UserName";
		String WorkerPassword = "1234";
		cont.txtUsername.setText(WorkerUserName);
		cont.txtPassword.setText(WorkerPassword);
		Worker worker = null;
		String[] data = new String[2];
		data[0] = WorkerUserName;
		data[1] = WorkerPassword;
		expectedRequest = new ServerRequest(Manager.Worker, "LogInWorker",
				ServerRequest.gson.toJson(data, String[].class));
		response = ServerRequest.gson.toJson(worker, Worker.class);
		
		cont.btnWorkerLogin.fire(); 
		
		assertFalse(historyCleaned);
		assertTrue(errMsg.equals("Please check the user name and the password"));
	}
	
	/**
	 *  wrong login of Worker
	 *  input: already loged in worker UserName and Password
        excpect: to not log in and pop with appropriet message
	 */
	@Test
	void WorkerCorrectButAlreadyLogedIn_Fail() 
	{
		String WorkerUserName = "UserName";
		String WorkerPassword = "1234";
		cont.txtUsername.setText(WorkerUserName);
		cont.txtPassword.setText(WorkerPassword);
		String[] data = new String[2];
		data[0] = WorkerUserName;
		data[1] = WorkerPassword;
		expectedRequest = new ServerRequest(Manager.Worker, "LogInWorker",
				ServerRequest.gson.toJson(data, String[].class));
		response = "user already logged in";
		
		cont.btnWorkerLogin.fire(); 
		
		assertFalse(historyCleaned);
		assertTrue(errMsg.equals("User already logged in"));
	}
	
}
