package com.meettwo.spring.config;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meettwo.service.UserService;

public class UserDeleteReciever {
	
	@Autowired ObjectMapper mapper;
	
	@Autowired
	UserService userService;
	
	@RabbitListener(containerFactory = "userDeleteContainer", queues = "userDelete-queue")
	public void handleMessage(List<String> users) {
		
		try {
					
			for (String userId : users) {
				userService.deleteUser(userId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
