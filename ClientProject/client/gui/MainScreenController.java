package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.api.databinding.MappingInfo;

import entities.Permission;
import entities.Subscriber;
import entities.Worker;
import io.clientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import module.JavafxPrinter;
import module.Navigator;

public class MainScreenController{

	@FXML
	private Button btnReturn;

	@FXML
	private VBox menu;

	@FXML
	private Pane body;

	@FXML
	private Label greetingMsg;

	@FXML
	private Button Login;

	private static final List<MenuItem> VISITOR_MAP = createVisitorMap();
	private static final Map<String,MenuItem> WORKER_MAP = createWorkerMap();
	private static final List<MenuItem> SUBSCRIBER_MAP = createSubscriberMap();
	
	@FXML
	void back(ActionEvent event) {
		Navigator.instance().back();
	}

	

	@FXML
	void logInOut(ActionEvent event) {
		Login.setVisible(false);
		Login.setManaged(false);
		greetingMsg.setText("");
		menu.getChildren().clear();
		Navigator.instance().clearHistory();
		Navigator.instance().navigate("login");
		if(clientController.client.logedInSunscriber.getVal() != null) {
			clientController.client.logedInSunscriber.silentSet(null);
			return;
		}
		if(clientController.client.logedInWorker.getVal() != null) {
			clientController.client.logedInWorker.silentSet(null);
			return;
		}
		if(clientController.client.visitorID.getVal() != null) {
			clientController.client.visitorID.silentSet(null);
			return;
		}
	}

	
	/**initialize the main screen with the listeners for the login
	 * @return the body of the main screen for the navigator
	 */
	public Pane init() {
		Login.setVisible(false);
		Login.setManaged(false);
		clientController.client.logedInSunscriber.AddListener(
				(prop,oldVal,newVal)->{
					
			if(newVal != null) {
				Subscriber s = clientController.client.logedInSunscriber.getVal();
				greetingMsg.setText("Hello "+s.firstName+ " "+s.lastName);
				Login.setVisible(true);
				Login.setManaged(true);
				setMenu(SUBSCRIBER_MAP);
			}
		});
		clientController.client.visitorID.AddListener(
				(prop,oldVal,newVal)->{
			if(newVal != null) {
				greetingMsg.setText("Hello Visitor");
				Login.setVisible(true);
				Login.setManaged(true);
				
				setMenu(VISITOR_MAP);
			}
		});
		clientController.client.logedInWorker.AddListener(
				(prop,oldVal,newVal)->{
			if(newVal != null) {
				Worker w = clientController.client.logedInWorker.getVal();
				greetingMsg.setText("Hello "+w.getFirstName()+ " "+w.getLastName());
				Login.setVisible(true);
				Login.setManaged(true);
				List<MenuItem> menuItems = new ArrayList<>();
				for(Permission p : prop.getVal().getPermissions().GetPermissions()) {
					menuItems.add(WORKER_MAP.getOrDefault(p.GetName(), new MenuItem(p.GetName(), null)));
				}
				setMenu(menuItems);
			}
		});
		return body;
	}
	
	
	private void setMenu(List<MenuItem> menuItems) {
		menu.getChildren().clear();
		for(MenuItem entry : menuItems) {
			Button b = new Button(entry.buttonContent);
			b.setOnAction((event)->{
				Navigator.instance().navigate(entry.pageToOpen);
			});
			menu.getChildren().add(b);
		}
	}
	
	
	private static List<MenuItem> createVisitorMap() {
		List<MenuItem> res = new ArrayList<>();
		 res.add(new MenuItem("new Single Order", "RegularOrder"));
		 res.add(new MenuItem("new Private Group Order", "SmallGroupOrder"));
		 res.add(new MenuItem("check Existing Order", "OrderDetails"));
		 return res;
	}
	
	private static Map<String, MenuItem> createWorkerMap() {
		Map<String, MenuItem> res = new HashMap<String, MenuItem>();
		//TODO add premissions names
		 res.put("", new MenuItem("Subscriber registration", "RegisterCommonDetails"));
		 res.put("",new MenuItem("check current visitors number", "VisitorsInThePark"));
		 res.put("", new MenuItem("Generate Reports", "ReportExport"));
		 res.put("", new MenuItem("change Park Parameters", "ParkManagerParametersUpdate"));
		 res.put("", new MenuItem("Approve Park Parameters", "DevisionManagerParksDetailsApprove"));
		 res.put("", new MenuItem("Approve Discounts", "DevisionManagerDiscount"));
		 return res;
	}
	
	private static List<MenuItem> createSubscriberMap() {
		List<MenuItem> res = new ArrayList<>();
		res.addAll(VISITOR_MAP);
		res.add(2,new MenuItem("new Group Order", "GroupOrder"));
		 return res;
	}
	
	
	private static class MenuItem{
		String buttonContent;
		String pageToOpen;
		
		/**
		 * @param buttonContent
		 * @param pageToOpen
		 */
		public MenuItem(String buttonContent, String pageToOpen) {
			this.buttonContent = buttonContent;
			this.pageToOpen = pageToOpen;
		}
		
	}
	// TODO remove this
	public Pane InitMockup(int caseNum) {
		switch (caseNum) {
		case 1:
			greetingMsg.setText("Hello guest");
			Login.setVisible(false);
			Login.setManaged(false);
			break;
		case 2:// Visitor
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr[] = { "new Single Order","new Private Group Order", "check Existing Order" };
			for (String s : arr) {
				menu.getChildren().add(new Button(s));
			}
			break;
		case 3:// subscriber
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr2[] = { "new Single Order","new Private Group Order","new Group Order", "check Existing Order" };
			for (String s : arr2) {
				menu.getChildren().add(new Button(s));
			}
			break;
		case 4:// worker
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr3[] = { "Subscriber registration", "check current visitors number" };
			for (String s : arr3) {
				menu.getChildren().add(new Button(s));
			}
			break;
		case 5:// parkManager
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr4[] = { "check current visitors number", "Generate Reports" , "change Park Parameters"};
			for (String s : arr4) {
				menu.getChildren().add(new Button(s));
			}
			break;
			
		case 6:// devisionManager
			greetingMsg.setText("Hello Jane Doe");
			Login.setText("LogOut");
			String arr5[] = { "check current visitors number", "Generate Reports" , "Approve Park Parameters","Approve discounts" };
			for (String s : arr5) {
				menu.getChildren().add(new Button(s));
			}
			break;
		}
		return body;
	}
	
}
