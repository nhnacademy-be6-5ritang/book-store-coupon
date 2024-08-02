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



/**
 * @author 이기훈
 * 글로벌 예외를 처리하는 클래스입니다.
 * 이 클래스는 애플리케이션 전역에서 발생하는 예외를 처리하여 적절한 HTTP 응답을 반환합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * MethodArgumentNotValidException 예외를 처리합니다.
	 * 요청 파라미터의 유효성 검증 실패 시 발생하는 예외를 처리합니다.
	 * 오류 메시지를 생성하여  ErrorStatus 객체로 응답합니다.
	 *
	 * @param exception 발생한  MethodArgumentNotValidException
	 * @return ResponseEntity containing ErrorStatus with validation error details
	 */
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

	/**
	 * GlobalException 예외를 처리합니다.
	 * <p>
	 * 애플리케이션의 전역적인 예외를 처리하고, 해당 예외에 대한 ErrorStatus 객체를 응답합니다.
	 * </p>
	 *
	 * @param ex 발생한 GlobalException
	 * @return ResponseEntity containing ErrorStatus with global error details
	 */
	@ExceptionHandler({GlobalException.class})
	public ResponseEntity<ErrorStatus> handleGlobalException(GlobalException ex) {
		ErrorStatus errorStatus=ex.getErrorStatus();

		log.error("글로벌 에러: {}", ex.getMessage());
		return new ResponseEntity<>(errorStatus, errorStatus.getStatus());
	}

	/**
	 * 모든 {@link Exception} 예외를 처리합니다.
	 * <p>
	 * 처리되지 않은 예외를 포괄적으로 처리하여 500 상태 코드와 함께 응답합니다.
	 * </p>
	 *
	 * @param ex 발생한 {@link Exception}
	 * @return {@link ResponseEntity} with HTTP status 500
	 */
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Void> handleException(Exception ex) {


		log.error("서버에러: {}", ex.getMessage());
		return ResponseEntity.status(500).build();
	}

}
