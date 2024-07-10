package com.nhnacademy.bookstorecoupon.auth.jwt.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.auth.annotation.CurrentUser;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstorecoupon.auth.jwt.service.TokenService;

import lombok.RequiredArgsConstructor;

// authenticated test 를 위한 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/info")
public class TokenController {
	private final TokenService tokenService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getUserInfo(@CurrentUser CurrentUserDetails user) {
		return ResponseEntity.status(HttpStatus.OK).body(tokenService.getUserInfo(user));
	}
}
