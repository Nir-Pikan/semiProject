package mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MyMail {

	private static final String FROM = "g1.gonature@gmail.com"; // GoNature mail address
	private static final String HOST= "smtp.gmail.com"; // Host of the mail address
	private static final int PORT = 587;
	private static final String USERNAME = "g1.gonature"; // GoNature mail username
	private static final String PASSWORD= "Aa123456!"; // Host of the mail password
	
	Session session; // for some internal usage of mail sending process

	public MyMail() { // Initialize GoNature Email address and other preferences
		Properties mailProps = new Properties();
		mailProps.put("mail.smtp.from", FROM);
		mailProps.put("mail.smtp.host", HOST);
		mailProps.put("mail.smtp.port", PORT);
		mailProps.put("mail.smtp.auth", true);
		mailProps.put("mail.smtp.starttls.enable", "true");

		session = Session.getDefaultInstance(mailProps, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME,PASSWORD );
			}

		});

	}

	/** send Mail and SMS as Pop up window. return true if mail sending succeeded **/
	public boolean SendEmailAndSMS(String destinationMail,String cellPhoneNum, String messageContent, String subject) {
		infoBox("subject: " + subject + "\n\n" +messageContent, "SMS send", "SMS sent to : "+ cellPhoneNum); //use cell phone number as a header (only for vision)
		if(!isValidEmailAddress(destinationMail))
			return false;  // if destination Mail address is invalid return false
		return SendEmaill(destinationMail, messageContent, subject);
		
	}

	private boolean SendEmaill(String destinationMail, String messageContent, String subject) { 
		
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(FROM));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationMail));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(messageContent);
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully...."); //TODO remove later (Roman)
		} catch (MessagingException mex) {
			return false;
		}

		return true;
	}

	private void infoBox(String infoMessage, String titleBar, String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}

	/** validation of a email address **/
	public boolean isValidEmailAddress(String email) {
		return email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$");
	}

}
