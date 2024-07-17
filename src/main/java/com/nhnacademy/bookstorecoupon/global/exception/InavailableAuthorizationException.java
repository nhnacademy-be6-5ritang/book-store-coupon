package com.nhnacademy.bookstorecoupon.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

public class InavailableAuthorizationException extends GlobalException {
	public InavailableAuthorizationException() {
		super(ErrorStatus.from(
			"자네는 해당 요청에 대한 권한이 없습니다.",
			HttpStatus.FORBIDDEN,
			LocalDateTime.now()
		));
	}
}
