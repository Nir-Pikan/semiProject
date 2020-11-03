package common;

import java.io.IOException;
import java.util.Stack;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import moudules.GuiController;

public class Navigator {

	private static Navigator instance = null;
	private static Pane baseNode = null;
	private static String defaultTab = "";
	private Tab current = null;

	private Stack<Tab> history;

	private Navigator() {
		if (baseNode == null)
			throw new RuntimeException("Navigator not initiated, run Navigator.init(Pane baseNode) first");
		history = new Stack<>();
		navigate(defaultTab);
	}

	public static Navigator instance() {
		if (instance == null)
			instance = new Navigator();
		return instance;
	}

	public static void init(Pane baseNode) {
		Navigator.baseNode = baseNode;
	}

	public void navigate(String destenation) {
		String fxmlName = null;

		// placeHolder - need to replace
		fxmlName = destenation + ".fxml";
		//
		if (current != null)
			history.push(current);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlName));
		try {
			current = new Tab();
			current.node = loader.load();
			current.controller = loader.getController();
			current.name = destenation;
			baseNode.getChildren().clear();
			baseNode.getChildren().add(current.node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void back() {
		if (history.isEmpty())
			return;
		Tab last = history.pop();
		current = last;
		baseNode.getChildren().clear();
		baseNode.getChildren().add(current.node);
	}

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
