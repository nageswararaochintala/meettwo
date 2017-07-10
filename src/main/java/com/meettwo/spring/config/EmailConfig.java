package com.meettwo.spring.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


/**
 * {@link EmailConfig} class used to configure the {@link JavaMailSender} for
 * mail sending by creating the {@link JavaMailSender} class bean object.
 * 
 * @author Nageswara rao.ch
 * @version 1.0
 */
@Configuration
public class EmailConfig {
	
	@Value("${email.protocol}")
	private String protocol;
	
	@Value("${email.auth}")
	private String auth;
	
	@Value("${email.starttls.enable}")
	private String starttls;
	
	@Value("${email.ssl.enable}")
	private String ssl;
	
	@Value("${email.debug}")
	private String debug;
	
	@Value("${email.host}")
	private String host;
	
	@Value("${email.port}")
	private Integer port;
	
	@Value("${email.user}")
	private String user;
	
	@Value("${email.pass}")
	private String password;
	
	@Bean
	public JavaMailSender javaMailSender() {
		
	    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
	    
	    javaMailSender.setHost(host);
	    javaMailSender.setPort(port);
	    javaMailSender.setUsername(user);
	    javaMailSender.setPassword(password);
	
	    javaMailSender.setJavaMailProperties(getMailProperties());
	
	    return javaMailSender;
	}
	
	private Properties getMailProperties() {
	    Properties properties = new Properties();
	    properties.setProperty("mail.transport.protocol", protocol);
	    properties.setProperty("mail.smtp.auth", auth);
	    properties.setProperty("mail.debug", debug);
	    properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
	    properties.setProperty("mail.smtp.ssl.enable",ssl);
	    return properties;
	}
}
