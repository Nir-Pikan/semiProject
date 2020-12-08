package module;

import java.io.IOException;
import java.util.Stack;

import gui.MainScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/** (singelton) class for navigation between windows */
public class Navigator {

	private static Navigator instance = null;
	private static Pane baseNode = null;
	private static String defaultTab = "login";
	private Tab current = null;

	private Stack<Tab> history;

	private Navigator() {
		if (baseNode == null)
			throw new RuntimeException("Navigator not initiated, run Navigator.init(Pane baseNode) first");
		history = new Stack<>();
		navigate(defaultTab);
	}

	/**
	 * <pre>
	 * (singelton) get an instance of navigator
	 * Navigator.init() need to be called before the call to this function
	 * </pre>
	 * 
	 * @exception RuntimeException if Navigator.init() not called once before the
	 *                             call to this function
	 * @return Navigator - the instance of this class
	 */
	public static Navigator instance() {
		if (instance == null)
			instance = new Navigator();
		return instance;
	}

	/**init the Navigator to change the content of the given Pane
	 * @param baseNode the node that the navigator need to change*/
	public static void init(Pane baseNode) {
		Navigator.baseNode = baseNode;
	}

	/** navigate to the given file(window) and push the current view to the history*/
	public GuiController navigate(String destenation) {
		String fxmlName = null;

		
		if (destenation.endsWith(".fxml"))
			fxmlName = destenation;
		else
			fxmlName = destenation + ".fxml";
		
		//push the current tab to the history
		if (current != null)
			history.push(current);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainScreenController.class.getResource(fxmlName));
		try {
			current = new Tab();
			current.node = loader.load();
			current.controller = loader.getController();
			current.controller.init();
			current.name = destenation;
			baseNode.getChildren().clear();
			baseNode.getChildren().add(current.node);
			return current.controller;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**navigates to the last page all the data from current page will be deleted*/
	public void back() {
		if (history.isEmpty())
			return;
		Tab last = history.pop();
		current = last;
		baseNode.getChildren().clear();
		baseNode.getChildren().add(current.node);
	}

	/**navigates to the default page(login) and clear the history*/
	public void clearHistory() {
		history = new Stack<>();
		current = null;
		navigate(defaultTab);
	}

	/**helper class for saving windows*/
	private class Tab {
		public Node node;
		public GuiController controller;
		public String name;

		public Tab(Node body, GuiController controller, String name) {
			super();
			this.node = body;
			this.controller = controller;
			this.name = name;
		}

		public Tab() {
			super();
		}

	}
}
