package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import module.Navigator;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the MainScreen page controller */
public class MainScreenController {

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
	private static final Map<String, MenuItem> WORKER_MAP = createWorkerMap();
	private static final List<MenuItem> SUBSCRIBER_MAP = createSubscriberMap();

	/** when clicking on the back button load previous page */
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
		Navigator.instance().clearHistory("login");
		if (clientController.client.logedInSubscriber.getVal() != null) {
			clientController.client.logedInSubscriber.silentSet(null);
			return;
		}
		if (clientController.client.logedInWorker.getVal() != null) {
			clientController.client.sendRequestAndResponse(new ServerRequest(Manager.Worker, "LogOutWorker",
					clientController.client.logedInWorker.getVal().getUserName()));
			clientController.client.logedInWorker.silentSet(null);
			return;
		}
		if (clientController.client.visitorID.getVal() != null) {
			clientController.client.visitorID.silentSet(null);
			return;
		}
	}

	/**
	 * initialize the main screen with the listeners for the login
	 * 
	 * @return the body of the main screen for the navigator
	 */
	public Pane init() {
		Login.setVisible(false);
		Login.setManaged(false);
		clientController.client.logedInSubscriber.AddListener((prop, oldVal, newVal) -> {

			if (newVal != null) {
				Subscriber s = clientController.client.logedInSubscriber.getVal();
				greetingMsg.setText("Hello " + s.firstName + " " + s.lastName);
				Login.setVisible(true);
				Login.setManaged(true);
				setMenu(SUBSCRIBER_MAP);
			}
		});
		clientController.client.visitorID.AddListener((prop, oldVal, newVal) -> {
			if (newVal != null) {
				greetingMsg.setText("Hello Visitor");
				Login.setVisible(true);
				Login.setManaged(true);

				setMenu(VISITOR_MAP);
			}
		});
		clientController.client.logedInWorker.AddListener((prop, oldVal, newVal) -> {
			if (newVal != null) {
				Worker w = clientController.client.logedInWorker.getVal();
				greetingMsg.setText("Hello "+w.getFirstName()+ " "+w.getLastName() +"("+w.getWorkerType()+")");
				Login.setVisible(true);
				Login.setManaged(true);
				List<MenuItem> menuItems = new ArrayList<>();
				for (Permission p : prop.getVal().getPermissions().GetPermissions()) {
					menuItems.add(WORKER_MAP.getOrDefault(p.GetName(), new MenuItem(p.GetName(), null)));
				}
				setMenu(menuItems);
			}
		});
		clientController.client.observable.addObserver((obs,event)->{
			if(event == clientController.SERVER_CLOSED) {
				Login.fire();
			}
		});
		return body;
	}

	private void setMenu(List<MenuItem> menuItems) {
		menu.getChildren().clear();
		for (MenuItem entry : menuItems) {
			Button b = new Button(entry.buttonContent);
			b.setOnAction((event) -> {
				Navigator.instance().navigate(entry.pageToOpen);
			});
			menu.getChildren().add(b);
		}
	}

	private static List<MenuItem> createVisitorMap() {
		List<MenuItem> res = new ArrayList<>();
		res.add(new MenuItem("New Single Order", "RegularOrder"));
		res.add(new MenuItem("New Private Group Order", "SmallGroupOrder"));
		res.add(new MenuItem("Check Existing Order", "OrderDetails"));
		return res;
	}

	private static Map<String, MenuItem> createWorkerMap() {
		Map<String, MenuItem> res = new HashMap<String, MenuItem>();

		res.put("Registration", new MenuItem("Subscriber registration", "RegisterCommonDetails"));
		res.put("VistitorsView", new MenuItem("Casual Orders & View visitors", "VisitorsInThePark"));
		res.put("ReportExport", new MenuItem("Generate Reports", "ReportExport"));
		res.put("EditParameters", new MenuItem("Change Park Parameters", "ParkManagerParametersUpdate"));
		res.put("ApproveParameters", new MenuItem("Approve Park Parameters", "DevisionManagerParksDetailsApprove"));
		res.put("ApproveDiscounts", new MenuItem("Approve Discounts", "DevisionManagerDiscount"));
		res.put("AddDiscounts", new MenuItem("Add New Discount", "AddDiscount"));
		return res;
	}

	private static List<MenuItem> createSubscriberMap() {
		List<MenuItem> res = new ArrayList<>();
		res.addAll(VISITOR_MAP);
		res.add(2, new MenuItem("New Group Order", "GroupOrder")); // n changed to capital
		return res;
	}

	private static class MenuItem {
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

	// TODO DELETE
	public Pane initTesting() {
		return body;
	}
}
