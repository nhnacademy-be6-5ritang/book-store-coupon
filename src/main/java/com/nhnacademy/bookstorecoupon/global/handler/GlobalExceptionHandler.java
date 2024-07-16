package com.nhnacademy.bookstorecoupon.global.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorStatus> processValidationError(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		StringBuilder errorMessageBuilder = new StringBuilder();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errorMessageBuilder.append(fieldError.getField())
				.append(" 에러: ")
				.append(fieldError.getDefaultMessage())
				.append(", 입력된 값: ")
				.append(fieldError.getRejectedValue())
				.append("; ");
		}

		// 오류 메시지 생성
		String errorMessage = errorMessageBuilder.toString();

		// ErrorStatus 객체 생성
		ErrorStatus errorStatus = ErrorStatus.from(
			errorMessage,
			HttpStatus.BAD_REQUEST,
			LocalDateTime.now()
		);

		return new ResponseEntity<>(errorStatus, HttpStatus.BAD_REQUEST);

	}

}
