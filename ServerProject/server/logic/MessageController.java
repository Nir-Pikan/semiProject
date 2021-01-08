package logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DbController;
import mail.MyMail;
import modules.IController;
import modules.ServerRequest;

public class MessageController implements IController{
	
	MyMail mailService;
	DbController db;
	
	public MessageController() {
		mailService = new MyMail();
		db=DbController.getInstance();
		createTable();
		
	}
	private void createTable() {
		db.createTable("motd(id int NOT NULL AUTO_INCREMENT, msg varchar(255),PRIMARY KEY (id))");
		System.out.println("motd table created");
		
	}
	/** Send Email Popup window for SMS**/
	public void SendEmailAndSMS(String destinationMail,String cellPhoneNum, String messageContent, String subject) { // add also phone number! String
		mailService.SendEmailAndSMS(destinationMail,cellPhoneNum, messageContent, subject);
	}
	
	@Override
	public String handleRequest(ServerRequest request) {
		switch(request.job) {
		case "getMotd":
			
			return ServerRequest.gson.toJson(getMotd(),String[].class);
			default:
				return "No such job";
		}
	}
	private String[] getMotd() {
		List<String> l = new ArrayList<>();
		ResultSet rs = db.sendQuery("SELECT msg from motd;");
		if(rs ==null)
			return new String[] {};
		try {
			while(rs.next()) {
				l.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l.toArray(new String[] {});
	}

}
