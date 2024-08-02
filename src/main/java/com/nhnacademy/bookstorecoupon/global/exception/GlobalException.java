package com.nhnacademy.bookstorecoupon.global.exception;

import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

/**
 * @author 김태환
 * GlobalException은 애플리케이션의 전역 예외를 처리하기 위한 사용자 정의 예외 클래스입니다.
 */
@Getter
public class GlobalException extends RuntimeException {
	private final ErrorStatus errorStatus;

	public GlobalException(ErrorStatus errorStatus) {
		this.errorStatus = errorStatus;
	}
}
