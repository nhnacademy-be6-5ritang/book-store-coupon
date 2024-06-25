package com.nhnacademy.bookstorecoupon.global.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nhnacademy.bookstorecoupon.global.exception.GlobalException;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorStatus> handleExceptionGlobally(GlobalException ex) {
		ErrorStatus errorStatus=ex.getErrorStatus();

		return new ResponseEntity<>(errorStatus, errorStatus.getStatus());
	}
}
