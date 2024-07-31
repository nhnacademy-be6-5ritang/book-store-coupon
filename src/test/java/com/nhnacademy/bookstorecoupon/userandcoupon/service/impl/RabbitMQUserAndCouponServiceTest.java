package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.UserCouponValidationException;

class RabbitMQUserAndCouponServiceTest {

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private RabbitMQUserAndCouponService rabbitMQUserAndCouponService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateUserAndCoupon_ValidInputs() {
		// Arrange
		Long couponId = 1L;
		Long userId = 1L;
		CouponIssuanceMessage expectedMessage = new CouponIssuanceMessage(couponId, userId);

		// Act
		rabbitMQUserAndCouponService.createUserAndCoupon(couponId, userId);

		// Assert
		verify(rabbitTemplate, times(1)).convertAndSend(
			eq("5ritang.coupon.exchange"),
			eq("5ritang.coupon.key"),
			Optional.ofNullable(argThat(message ->
				message instanceof CouponIssuanceMessage &&
					((CouponIssuanceMessage)message).getCouponId().equals(couponId) &&
					((CouponIssuanceMessage)message).getUserId().equals(userId)
			))
		);
	}

	@Test
	void testCreateUserAndCoupon_NullCouponId() {
		// Arrange
		Long userId = 1L;

		// Act & Assert
		UserCouponValidationException exception = assertThrows(
			UserCouponValidationException.class,
			() -> rabbitMQUserAndCouponService.createUserAndCoupon(null, userId)
		);

		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals("쿠폰아이디와 사용자아이디가 필요합니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, errorStatus.getStatus());
		assertNotNull(errorStatus.getTimestamp());
	}

	@Test
	void testCreateUserAndCoupon_NullUserId() {
		// Arrange
		Long couponId = 1L;

		// Act & Assert
		UserCouponValidationException exception = assertThrows(
			UserCouponValidationException.class,
			() -> rabbitMQUserAndCouponService.createUserAndCoupon(couponId, null)
		);

		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals("쿠폰아이디와 사용자아이디가 필요합니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, errorStatus.getStatus());
		assertNotNull(errorStatus.getTimestamp());
	}

	@Test
	void testCreateUserAndCoupon_NullCouponIdAndUserId() {
		// Act & Assert
		UserCouponValidationException exception = assertThrows(
			UserCouponValidationException.class,
			() -> rabbitMQUserAndCouponService.createUserAndCoupon(null, null)
		);

		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals("쿠폰아이디와 사용자아이디가 필요합니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, errorStatus.getStatus());
		assertNotNull(errorStatus.getTimestamp());
	}
}