package com.meettwo.spring.config;


import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Configuration
public class RabbitMQConfig {

	@Autowired
	Environment environment;
	
	final static String mailQueue = "mailing-queue";
	
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
				environment.getRequiredProperty("rabbitmq.host"),
				Integer.parseInt(environment.getRequiredProperty("rabbitmq.port")));
		connectionFactory.setUsername(environment.getRequiredProperty("rabbitmq.user"));
		connectionFactory.setPassword(environment.getRequiredProperty("rabbitmq.pass"));
		return connectionFactory;
	}
	
	@Bean
	ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JodaModule());
		mapper.registerModule(new Hibernate4Module());
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
		
	}

	@Bean
	public MessageConverter jsonMessageConverter(ObjectMapper mapper) {
		final Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		converter.setJsonObjectMapper(mapper);
		return converter;
	}

	@Bean
	public RabbitTemplate template(MessageConverter jsonMessageConverter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setMessageConverter(jsonMessageConverter);
		return template;
	}

	@Bean
	public DirectExchange direct() {
		return new DirectExchange("meettwo.direct");
	}
	
	@Bean
	public Queue mailQueue() {
		return new Queue(mailQueue);
	}
	
	@Bean
	public Binding mailBinding(DirectExchange direct, Queue mailQueue) {
		return BindingBuilder.bind(mailQueue).to(direct).with(environment.getRequiredProperty("rabbitmq.mail.key"));
	}
	
	@Bean
	MailReciever mailReciever() {
		return new MailReciever();
	}
	
	@Bean
	SimpleMessageListenerContainer mailContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter mailListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(mailQueue);
		container.setMessageListener(mailListenerAdapter);
		container.setChannelTransacted(true);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return container;

	}

	@Bean
	MessageListenerAdapter mailListenerAdapter(MailReciever mailReciever, MessageConverter jsonMessageConverter) {
		return new MessageListenerAdapter(mailReciever, jsonMessageConverter);
	}
	
}
