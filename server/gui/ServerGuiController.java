package gui;

import java.io.IOException;
import java.sql.SQLException;

import io.ServerController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.UIupdate;

public class ServerGuiController {

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

	public void setConnected(String address,String hostname) {
		UIupdate.update(() -> {
			txtIp.setText(address);
			txtHost.setText(hostname);
			connectionStatus.setText("Connected");
		});
		
	}

	public void setDisconnected() {
		UIupdate.update(() -> {
			txtIp.setText("not Connected");
			txtHost.setText("not Connected");
			connectionStatus.setText("not Connected");
		});
		

	}

	public void dbConnected() {
		DbStatus.setText("connected");

	}

}
