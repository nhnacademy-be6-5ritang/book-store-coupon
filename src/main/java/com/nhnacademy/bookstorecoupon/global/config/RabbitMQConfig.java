package com.nhnacademy.bookstorecoupon.global.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.bookstorecoupon.global.listener.CouponIssuanceListener;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	public static final String COUPON_ISSUE_QUEUE = "5ritang_coupon_issue";
	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	@Value("${spring.rabbitmq.port}")
	private int port;




	@Bean
	public Queue couponIssueQueue() {
		return new Queue(COUPON_ISSUE_QUEUE, false);
	}

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	DirectExchange directExchange() {
		return new DirectExchange("5ritang.coupon.exchange");
	}

	@Bean
	Binding binding(DirectExchange directExchange, Queue couponIssueQueue) {
		return BindingBuilder.bind(couponIssueQueue).to(directExchange).with("5ritang.coupon.key");
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
		CouponIssuanceListener couponIssuanceListener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(couponIssueQueue());
		container.setMessageListener(couponIssuanceListener);
		return container;
	}

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		System.out.println("host: "+host);
		return connectionFactory;
	}
}
