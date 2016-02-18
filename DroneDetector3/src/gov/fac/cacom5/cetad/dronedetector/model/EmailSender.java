package gov.fac.cacom5.cetad.dronedetector.model;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class EmailSender {
	public EmailSender() {

	}
	
	public static void send(String from, String[] to, String password, String subject, String message)
	{
		String host = "smtp.globat.com";
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", from);
		properties.put("mail.smtp.password", password);
		properties.put("mail.smtp.port", 587);
		properties.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(properties, null);
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(new InternetAddress(from));
			// Now get the address of recipents
			InternetAddress[] toAddresses = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				toAddresses[i] = new InternetAddress(to[i]);
			}
			//Now add all the toAddresses elements to mimeMessage
			for (int i = 0; i < toAddresses.length; i++) {
				mimeMessage.setRecipient(RecipientType.TO, toAddresses[i]);
			}
			//Add subject
			mimeMessage.setSubject(subject);
			//Set message to mimeMessage
			mimeMessage.setText(message);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, password);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
