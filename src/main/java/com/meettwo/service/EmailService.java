package com.meettwo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${email.from}")
	String from;

	/**
	 * This method used to send mail given information vai method params
	 * @param to specifies the list of mail address that on mail will be sent
	 * @param sub specifies the mail subject 
	 * @param msgBody specifies the body of the mail
	 */
	public void sendEmail(String[] to, String sub, String msgBody){
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(sub);
        message.setText(msgBody);
        javaMailSender.send(message);
    }
}
