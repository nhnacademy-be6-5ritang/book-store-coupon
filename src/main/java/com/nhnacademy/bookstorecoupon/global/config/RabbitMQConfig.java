package com.nhnacademy.bookstorecoupon.global.config;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.bookstorecoupon.global.listener.CouponIssuanceListener;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	public static final String COUPON_ISSUE_QUEUE = "couponIssueQueue";

	@Bean
	public Queue couponIssueQueue() {
		return new Queue(COUPON_ISSUE_QUEUE, false);
	}

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
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
}
