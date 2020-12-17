package mail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MyMail {

	String from; // GoNature mail address
	String host; // Host of the mail address
	Session session; // for some internal usage of mail sending process

	public MyMail() { // Initialize GoNature Email address and other preferences
		from = "g1.gonature@gmail.com";
		host = "smtp.gmail.com";
		Properties mailProps = new Properties();
		mailProps.put("mail.smtp.from", from);
		mailProps.put("mail.smtp.host", host);
		mailProps.put("mail.smtp.port", 587);
		mailProps.put("mail.smtp.auth", true);
		mailProps.put("mail.smtp.starttls.enable", "true");

		session = Session.getDefaultInstance(mailProps, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("g1.gonature", "Aa123456!");
			}

		});

	}

	/** send Mail and SMS as Pop up window. return true if mail sending succeeded **/
	public boolean SendEmailAndSMS(String destinationMail,String cellPhoneNum, String massageContent, String subject) {
		infoBox(massageContent, subject, cellPhoneNum); //use cell phone number as a header (only for vision)
		if(!isValidEmailAddress(destinationMail))
			return false;  // if destination Mail address is invalid return false
		return SendEmaill(destinationMail, massageContent, subject);
		
	}

	private boolean SendEmaill(String destinationMail, String massageContent, String subject) { 
		
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationMail));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(massageContent);
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
