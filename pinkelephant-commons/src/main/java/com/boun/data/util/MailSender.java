package com.boun.data.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	private static MailSender instance = new MailSender();

	private static final String SMTP_HOST_NAME = "smtp.live.com";
	private static final String SMTP_AUTH_USER = "pink___elephant@hotmail.com";
	private static final String SMTP_AUTH_PWD = "TestSWE574";

	private MailSender() {
	}

	public static MailSender getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		getInstance().sendMail("mehmetcelikel@gmail.com", "subject", "messageStr");
	}

	public void sendMail(String to, String subject, String messageStr) {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(true);
		// uncomment for debugging infos to stdout
		// mailSession.setDebug(true);

		try {
			Transport transport = session.getTransport();

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(SMTP_AUTH_USER));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent("<h:body>" + messageStr + "</body>", "text/html;     charset=utf-8");

			transport.connect();
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
}