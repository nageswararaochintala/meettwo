package com.meettwo.spring.config;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meettwo.dto.MeetTwoMail;
import com.meettwo.service.EmailService;

public class MailReciever {

	
	@Autowired ObjectMapper mapper;

	@Autowired 
	EmailService emailService;
	
	@RabbitListener(containerFactory = "mailContainer", queues = "mail-queue")
	public void handleMessage(List<MeetTwoMail> mailsMap) {
		
		try {
			List<MeetTwoMail> mails = mapper.convertValue(mailsMap, new TypeReference<List<MeetTwoMail>>() { });			
					
			for (MeetTwoMail mail : mails) {
				emailService.sendEmail(mail.getTo(),mail.getSubject(), mail.getBody());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
