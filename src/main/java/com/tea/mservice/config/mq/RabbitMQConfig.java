package com.tea.mservice.config.mq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfig {

	/*// RabbitMQ的配置信息
	@Value("${mq.rabbit.host}")
	private String mqRabbitHost;
	@Value("${mq.rabbit.port}")
	private Integer mqRabbitPort;
	@Value("${mq.rabbit.username}")
	private String mqRabbitUsername;
	@Value("${mq.rabbit.password}")
	private String mqRabbitPassword;
	@Value("${mq.rabbit.virtualHost}")
	private String mqRabbitVirtualHost;

	@Bean
	@Primary
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(mqRabbitHost, mqRabbitPort);

		connectionFactory.setUsername(mqRabbitUsername);
		connectionFactory.setPassword(mqRabbitPassword);
		connectionFactory.setVirtualHost(mqRabbitVirtualHost);
		connectionFactory.setPublisherConfirms(true);

		return connectionFactory;
	}

	@Bean
	@Primary
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(this.connectionFactory());
	}

	@Bean("rabbitTemplate")
	@Primary
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(this.connectionFactory());
	}*/
}