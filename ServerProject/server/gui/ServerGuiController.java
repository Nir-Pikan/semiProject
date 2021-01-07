package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import io.ServerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import modules.ConsoleReplacer;
import util.UIupdate;

/**
 * the server's gui controller class
 */
public class ServerGuiController implements Initializable {

	/** main window wont close if this is true */
	public static boolean isServerRunning = false;

	private ServerController sc;
	@FXML
	private Label txtIp;

	@FXML
	private Label txtHost;

	@FXML
	private Label connectionStatus;

	@FXML
	private Label DbStatus;

	@FXML
	private Button startBtn;

	@FXML
	private TextField serverPort;

	@FXML
	private ScrollPane scroll;

	@FXML
	/**
	 * starts running the server
	 */
	void StartServer(ActionEvent event) {
		int port;

		if (serverPort.getText().contentEquals("")) {
			port = ServerController.DEFAULT_SERVER_PORT;
			serverPort.setText(Integer.toString(ServerController.DEFAULT_SERVER_PORT));
			serverPort.setEditable(false);
		} else {
			port = Integer.parseInt(serverPort.getText());
		}
		try {
			sc = ServerController.startServer(port, this);
			isServerRunning = true;
			startBtn.setDisable(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	/**
	 * shuts the server down
	 */
	void closeAll(ActionEvent event) {
		try {
			sc.stopListening();
			sc.close();
			isServerRunning = false;
			startBtn.setDisable(false);
			serverPort.setEditable(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * update server's status when connected to host
	 * 
	 * @param address  the address the server connects to
	 * @param hostname the host the server connects to
	 */
	public void setConnected(String address, String hostname) {
		UIupdate.update(() -> {
			txtIp.setText(address);
			txtHost.setText(hostname);
			connectionStatus.setText("Connected");
		});

	}

	/**
	 * update server's status when disconnecting from host
	 */
	public void setDisconnected() {
		UIupdate.update(() -> {
			txtIp.setText("not Connected");
			txtHost.setText("not Connected");
			connectionStatus.setText("not Connected");
		});

	}

	/**
	 * update DB status when connecting to DB
	 */
	public void dbConnected() {
		DbStatus.setText("connected");

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		new ConsoleReplacer(scroll).replaceAll(true);
	}

}
