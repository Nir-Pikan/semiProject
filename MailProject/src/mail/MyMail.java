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

/**
 * a class responsible for sending mails and SMS from GoNature
 */
public class MyMail {

	private static final String FROM = "g1.gonature@gmail.com"; // GoNature mail address
	private static final String HOST = "smtp.gmail.com"; // Host of the mail address
	private static final int PORT = 587;
	private static final String USERNAME = "g1.gonature"; // GoNature mail user name
	private static final String PASSWORD = "Aa123456!"; // Host of the mail password

	Session session; // for some internal usage of mail sending process

	/**
	 * Initialize GoNature Email address and other preferences
	 */
	public MyMail() {
		Properties mailProps = new Properties();
		mailProps.put("mail.smtp.from", FROM);
		mailProps.put("mail.smtp.host", HOST);
		mailProps.put("mail.smtp.port", PORT);
		mailProps.put("mail.smtp.auth", true);
		mailProps.put("mail.smtp.starttls.enable", "true");

		session = Session.getDefaultInstance(mailProps, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}

		});

	}

	/**
	 * send Mail and SMS as Pop up window.
	 * 
	 * @param destinationMail the email address to send mail to
	 * @param cellPhoneNum    the phone number to send SMS to
	 * @param messageContent  the content of both messages
	 * @param subject         the subject of mail
	 * @return true if mail sending succeeded
	 **/
	public boolean SendEmailAndSMS(String destinationMail, String cellPhoneNum, String messageContent, String subject) {
		infoBox("subject: " + subject + "\n\n" + messageContent, "SMS send", "SMS sent to : " + cellPhoneNum);

		if (!isValidEmailAddress(destinationMail))
			return false; // if destination Mail address is invalid return false
		return SendEmaill(destinationMail, messageContent, subject);

	}

	/**
	 * sends Mail from GoNature to destination email.
	 * 
	 * @param destinationMail the email address to send mail to
	 * @param messageContent  the content of both messages
	 * @param subject         the subject of mail
	 * @return true if mail sending succeeded
	 */
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
			System.out.println("Sent email successfully....");
		} catch (MessagingException mex) {
			return false;
		}

		return true;
	}

	/**
	 * creates a pop up information box to mimic an SMS
	 * 
	 * @param infoMessage   the message to be written in the info box
	 * @param titleBar      the title of the pop up
	 * @param headerMessage the info box's header message
	 */
	private void infoBox(String infoMessage, String titleBar, String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.show();
	}

	/**
	 * validation of a email address
	 * 
	 * @param email the email to be verified
	 * @return true if email matches format, false otherwise
	 */
	public boolean isValidEmailAddress(String email) {
		return email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$");
	}

}
