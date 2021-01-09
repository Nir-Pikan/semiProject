package logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DbController;
import mail.MyMail;
import modules.IController;
import modules.ServerRequest;

/** the Message controller class */
public class MessageController implements IController {

	MyMail mailService;
	DbController db;

	/** creates the {@link MessageController} */
	public MessageController() {
		mailService = new MyMail();
		db = DbController.getInstance();
		createTable();

	}

	/** creates the messages table in DB for motd */
	private void createTable() {
		db.createTable("motd(id int NOT NULL AUTO_INCREMENT, msg varchar(255),PRIMARY KEY (id))");
		System.out.println("motd table created");

	}

	/**
	 * Send Email pop up window for SMS
	 * 
	 * @param destinationMail the email address to send mail to
	 * @param cellPhoneNum    the phone number to send SMS to
	 * @param messageContent  the content of the message
	 * @param subject         the message's subject
	 */
	public void SendEmailAndSMS(String destinationMail, String cellPhoneNum, String messageContent, String subject) {
		mailService.SendEmailAndSMS(destinationMail, cellPhoneNum, messageContent, subject);
	}

	@Override
	public String handleRequest(ServerRequest request) {
		switch (request.job) {
		case "getMotd":

			return ServerRequest.gson.toJson(getMotd(), String[].class);
		default:
			return "No such job";
		}
	}

	/** gets all the messages from the motd table from DB */
	private String[] getMotd() {
		List<String> l = new ArrayList<>();
		ResultSet rs = db.sendQuery("SELECT msg from motd;");
		if (rs == null)
			return new String[] {};
		try {
			while (rs.next()) {
				l.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l.toArray(new String[] {});
	}

}
