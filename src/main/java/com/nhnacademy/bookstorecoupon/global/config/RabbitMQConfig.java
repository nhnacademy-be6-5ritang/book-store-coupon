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


/**
 * RabbitMQ 관련 설정을 정의하는 구성 클래스입니다.
 * <p>
 * 이 클래스는 RabbitMQ와의 연결, 큐, 교환기, 바인딩 및 메시지 컨버터를 설정합니다.
 * </p>
 *
 * @author 이기훈
 */
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




	/**
	 * 쿠폰 발급을 위한 큐를 생성합니다.
	 * <p>
	 * 큐는 기본적으로 내구성이 없으며, 큐 이름은 {@link #COUPON_ISSUE_QUEUE}입니다.
	 * </p>
	 *
	 * @return 생성된 {@link Queue} 객체
	 */
	@Bean
	public Queue couponIssueQueue() {
		return new Queue(COUPON_ISSUE_QUEUE, false);
	}

	/**
	 * 메시지를 JSON 형식으로 변환하는 {@link Jackson2JsonMessageConverter}를 생성합니다.
	 *
	 * @return 생성된 {@link Jackson2JsonMessageConverter} 객체
	 */
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * RabbitMQ의 직접 교환기를 생성합니다.
	 * <p>
	 * 교환기 이름은 "5ritang.coupon.exchange"입니다.
	 * </p>
	 *
	 * @return 생성된 {@link DirectExchange} 객체
	 */
	@Bean
	DirectExchange directExchange() {
		return new DirectExchange("5ritang.coupon.exchange");
	}

	/**
	 * 큐와 교환기를 바인딩하는 {@link Binding}을 생성합니다.
	 * <p>
	 * 큐는 {@link #couponIssueQueue()}에서 생성되며, 교환기는 {@link #directExchange()}에서 생성됩니다.
	 * 바인딩 키는 "5ritang.coupon.key"입니다.
	 * </p>
	 *
	 * @param directExchange 직접 교환기
	 * @param couponIssueQueue 쿠폰 발급 큐
	 * @return 생성된 {@link Binding} 객체
	 */
	@Bean
	Binding binding(DirectExchange directExchange, Queue couponIssueQueue) {
		return BindingBuilder.bind(couponIssueQueue).to(directExchange).with("5ritang.coupon.key");
	}


	/**
	 * RabbitTemplate을 생성합니다.
	 * <p>
	 * 이 템플릿은 메시지 변환기로 {@link #jsonMessageConverter()}를 사용합니다.
	 * </p>
	 *
	 * @param connectionFactory RabbitMQ 연결 팩토리
	 * @return 생성된 {@link RabbitTemplate} 객체
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}


	/**
	 * 메시지 리스너 컨테이너를 생성합니다.
	 * <p>
	 * 이 컨테이너는 {@link #couponIssueQueue()}에서 생성된 큐와 {@link CouponIssuanceListener}를 사용합니다.
	 * </p>
	 *
	 * @param connectionFactory RabbitMQ 연결 팩토리
	 * @param couponIssuanceListener 쿠폰 발급 리스너
	 * @return 생성된 {@link SimpleMessageListenerContainer} 객체
	 */
	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
		CouponIssuanceListener couponIssuanceListener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(couponIssueQueue());
		container.setMessageListener(couponIssuanceListener);
		return container;
	}


	/**
	 * RabbitMQ와의 연결을 위한 {@link ConnectionFactory}를 생성합니다.
	 * <p>
	 * 이 팩토리는 호스트, 포트, 사용자 이름 및 비밀번호를 설정합니다.
	 * </p>
	 *
	 * @return 생성된 {@link ConnectionFactory} 객체
	 */
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
