package com.nhnacademy.bookstorecoupon.userandcoupon.feignclient;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.BirthdayCouponTargetResponse;

@FeignClient(name = "user-birthday-feign-client", url = "http://localhost:8083")
public interface UserBirthdayFeignClient {


	@GetMapping("/api/users/birthday")
	ResponseEntity<List<BirthdayCouponTargetResponse>> getUsersWithBirthday(
		@RequestParam("date") LocalDate date);

}


