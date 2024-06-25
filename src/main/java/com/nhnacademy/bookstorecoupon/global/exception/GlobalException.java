package com.nhnacademy.bookstorecoupon.global.exception;

import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
	private final ErrorStatus errorStatus;

	public GlobalException(ErrorStatus errorStatus) {
		this.errorStatus = errorStatus;
	}
}
