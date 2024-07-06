package com.lifetree.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;

	public boolean sendEmailMessage(String toEmail, String subject, String body) throws Exception {
		boolean mailSendStatus = false;
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom(fromEmail);
			helper.setTo(toEmail);
			helper.setSentDate(new Date());
			helper.setText(body, true); // body is sending as a HTML
			helper.setSubject(subject);
			mailSender.send(msg);
			mailSendStatus = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mailSendStatus;
	}
}
