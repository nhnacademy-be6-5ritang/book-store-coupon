package com.nhnacademy.bookstorecoupon.userandcoupon.feignclient;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.BirthdayCouponTargetResponse;

/**
 * 사용자 생일 정보를 조회하는 Feign 클라이언트 인터페이스입니다.
 * <p>
 * 이 클라이언트는 외부 서비스에서 사용자 생일 정보를 가져오기 위해 사용됩니다.
 * </p>
 *
 * @FeignClient(name = "user-birthday-feign-client", url = "<a href="http://localhost:8083">...</a>")
 */
@FeignClient(name = "user-birthday-feign-client", url = "http://localhost:8083")
public interface UserBirthdayFeignClient {


	/**
	 * 주어진 날짜에 생일을 가진 사용자 목록을 조회합니다.
	 *
	 * @param date 조회할 날짜 (생일)
	 * @return 주어진 날짜에 생일을 가진 사용자 목록이 포함된 {@link ResponseEntity}
	 */
	@GetMapping("/api/users/birthday")
	ResponseEntity<List<BirthdayCouponTargetResponse>> getUsersWithBirthday(
		@RequestParam("date") LocalDate date);

}


