package com.nhnacademy.bookstorecoupon.global.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * @author 이경헌
 * LogNCrashAppender 클래스는 Logback의 AppenderBase를 확장하여 로그 이벤트가 발생할 때마다 외부 서비스로 로그를 전송하는 기능을 제공합니다.
 * 이 클래스는 프로덕션 환경에서만 작동하도록 설정됩니다.
 */
public class LogNCrashAppender extends AppenderBase<ILoggingEvent> {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(LogNCrashAppender.class);
	private static final String PROFILE = System.getenv("SERVER_PROFILE");

	@Override
	protected void append(ILoggingEvent loggingEvent) {
		if (PROFILE.equals("prod")) {
			Map<String, Object> logData = new HashMap<>();
			logData.put("projectName", "Xyx7DoyszcG66ULx");
			logData.put("projectVersion", "1.0.0");
			logData.put("logVersion", "v2");
			logData.put("body", loggingEvent.getFormattedMessage());
			logData.put("logSource", "http");
			logData.put("logType", "log");
			logData.put("platform", "5ritang-Coupon");
			logData.put("host", "192.168.0.75");
			logData.put("logLevel", loggingEvent.getLevel().toString());

			String url = "https://api-logncrash.nhncloudservice.com/v2/log";

			try {
				restTemplate.postForEntity(url, logData, String.class);
			} catch (Exception e) {
				logger.error("외부 서비스로 로그를 보내는 동안 에러가 발생했습니다.", e);
			}
		}
	}
}
