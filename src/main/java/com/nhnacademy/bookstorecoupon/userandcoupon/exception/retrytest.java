package com.nhnacademy.bookstorecoupon.userandcoupon.exception;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class retrytest {

	private static final int MAX_RETRY_ATTEMPTS = 3;
	private static final int RETRY_DELAY_MS = 5000; // 5초 대기

	@Scheduled(cron = "0 55 19 * * *")
	public void testScheduler() {
		log.warn("기한만료 쿠폰 체크로직 발동");

		try {
			// 실제 로직 수행
			executeLogic();
		} catch (Exception e) {
			log.error("기한만료 쿠폰 체크로직 실행 중 오류 발생", e);
			// 재시도 로직 호출
			retryTest(1);
		}
	}

	private void retryTest(int attempt) {
		if (attempt > MAX_RETRY_ATTEMPTS) {
			log.error("최대 재시도 횟수 초과");
			// 재시도 실패 알림 전송 또는 다른 처리
			return;
		}

		try {
			Thread.sleep(RETRY_DELAY_MS); // 대기 시간
			log.warn("재시도 - 시도 " + attempt);
			// 새로운 스레드 대신 스케줄링을 사용
			new Thread(() -> {
				try {
					executeLogic();
				} catch (Exception e) {
					log.error("재시도 중 오류 발생", e);
					retryTest(attempt + 1); // 다음 시도
				}
			}).start();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("재시도 중 오류 발생", e);
		}
	}

	private void executeLogic() throws Exception {
		// 실제 로직 수행
		throw new Exception("Test exception");
	}
}