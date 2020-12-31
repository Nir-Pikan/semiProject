package gui;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import io.ServerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modules.TextAreaPrintStream;
import util.UIupdate;

public class ServerGuiController implements Initializable{

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
    private TextArea log;
    
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TextAreaPrintStream t ;
	System.setOut(t = new TextAreaPrintStream(log, new OutputStream() {
		
		@Override
		public void write(int arg0) throws IOException {
		}
	}));
	t.setShowInOriginalSystemOut();
	}
	
	

}
