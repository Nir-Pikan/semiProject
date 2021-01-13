package clientGui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clientIO.ConnectionInterface;
import clientIO.UserInterface;
import clientIO.clientController;
import entities.Subscriber;
import entities.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import entities.Subscriber.Type;
import modules.Property;
import modules.ServerRequest;

class LoginControllerTest {
	static String errMsg = null;
	static String response = null;
	static boolean historyCleaned = false;
	private JFXPanel panel = new JFXPanel();
	
	LoginController cont;
	
	@Before
	static void setPopUp() {
	PopUp.myPop = new PopUp.IPopUp() {
		
		@Override
		public void showInformation(String title, String header, String body) {
		}
		
		@Override
		public void showError(String title, String header, String body) {
			errMsg = body;
		}
		
		@Override
		public boolean ask(String title, String header, String body) {
			return false;
		}
	};	
	
	clientController.serverConnection = new ConnectionInterface() {
		
		@Override
		public String sendRequestAndResponse(ServerRequest request) {
			return response;
		}
	};
	
	clientController.users = new UserInterface() {
		
		@Override
		public void setVisitorID(Property<String> visitorID) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setLogedInWorker(Property<Worker> logedInWorker) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setLogedInSubscriber(Property<Subscriber> logedInSubscriber) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public Property<String> getVisitorID() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Property<Worker> getLogedInWorker() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Property<Subscriber> getLogedInSubscriber() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	Navigator.setNavigator(new NavigatorInterface() {
		
		@Override
		public GuiController navigate(String destenation) {
			return null;
		}
		
		@Override
		public void clearHistory(String fxml) {
			historyCleaned = true;
		}
		
		@Override
		public void clearHistory() {
			historyCleaned = true;
		}
		
		@Override
		public void back() {
		}
	});
	}
	
	@BeforeEach
	void setUp() throws Exception {
		errMsg="";
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EntryReportController.class.getResource("logIn.fxml"));
		loader.load();

		cont = loader.getController();
		
		historyCleaned = false;
	}

	@Test
	void test() {
		response = ServerRequest.gson.toJson(new Subscriber("", "", "", "", "", "", 1, Type.SUBSCRIBER));
		
		cont.UserLogin(null);
		fail("Not yet implemented");
	}

}
