package logic;

import mail.MyMail;

public class MessageController {
	
	MyMail mailService;
	public MessageController() {
		mailService = new MyMail();
		
	}
	/** Send Email Popup window for SMS**/
	public void SendEmailAndSMS(String destinationMail,String cellPhoneNum, String massageContent, String subject) { // add also phone number! String
		mailService.SendEmailAndSMS(destinationMail,cellPhoneNum, massageContent, subject);
	}

}
