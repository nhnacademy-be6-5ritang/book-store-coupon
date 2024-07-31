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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


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

		log.error("validation 에러: {}", exception.getMessage());
		return new ResponseEntity<>(errorStatus, HttpStatus.BAD_REQUEST);

	}



	@ExceptionHandler({GlobalException.class})
	public ResponseEntity<ErrorStatus> handleGlobalException(GlobalException ex) {
		ErrorStatus errorStatus=ex.getErrorStatus();

		log.error("글로벌 에러: {}", ex.getMessage());
		return new ResponseEntity<>(errorStatus, errorStatus.getStatus());
	}



	@ExceptionHandler({Exception.class})
	public ResponseEntity<Void> handleException(Exception ex) {


		log.error("서버에러: {}", ex.getMessage());
		return ResponseEntity.status(500).build();
	}

}
