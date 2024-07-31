package com.nhnacademy.bookstorecoupon.global.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponTemplateInsufficientQuantity;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;

class CouponIssuanceListenerTest {

	@Mock
	private UserAndCouponRepository userAndCouponRepository;

	@Mock
	private CouponTemplateRepository couponTemplateRepository;

	@InjectMocks
	private CouponIssuanceListener couponIssuanceListener;

	private final ObjectMapper objectMapper = new ObjectMapper()
		.findAndRegisterModules()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private final Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private Message createMockMessage(CouponIssuanceMessage message) {
		try {
			byte[] messageBody = objectMapper.writeValueAsBytes(message);
			MessageProperties messageProperties = new MessageProperties();
			return new Message(messageBody, messageProperties);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create mock message", e);
		}
	}

	@Test
	void testOnMessage_SuccessfulProcessing() {
		// Arrange
		Long couponId = 1L;
		Long userId = 1L;
		CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, userId);

		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(1000))
			.salePrice(BigDecimal.valueOf(100))
			.saleRate(null)
			.maxSalePrice(null)
			.type("sale")
			.build();

		CouponTemplate couponTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(LocalDateTime.now().plusDays(30))
			.issueDate(LocalDateTime.now())
			.quantity(10L)
			.build();

		Message mockMessage = createMockMessage(message);

		// Act
		couponIssuanceListener.onMessage(mockMessage);

		// Assert
		verify(userAndCouponRepository, times(1)).save(any(UserAndCoupon.class));
		verify(couponTemplateRepository, times(1)).save(couponTemplate);
		assertEquals(9L, couponTemplate.getQuantity()); // Assert quantity is decreased
	}

	@Test
	void testOnMessage_CouponTemplateNotFound() {
		// Arrange
		Long couponId = 1L;
		Long userId = 1L;
		CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, userId);

		Message mockMessage = createMockMessage(message);

		// Mock behavior for repository
		when(couponTemplateRepository.findById(couponId)).thenReturn(Optional.empty());

		// Act & Assert
		RuntimeException thrown = assertThrows(
			RuntimeException.class,
			() -> couponIssuanceListener.onMessage(mockMessage)
		);

		assertTrue(thrown.getCause() instanceof CouponTemplateNotFoundException);
		assertEquals(
			"해당 쿠폰템플릿 아이디 '1'는 존재하지 않습니다.",
			thrown.getCause().getMessage()
		);
	}

	@Test
	void testOnMessage_CouponTemplateInsufficientQuantity() {
		// Arrange
		Long couponId = 1L;
		Long userId = 1L;
		CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, userId);

		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(1000))
			.salePrice(BigDecimal.valueOf(100))
			.saleRate(null)
			.maxSalePrice(null)
			.type("sale")
			.build();

		CouponTemplate couponTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(LocalDateTime.now().plusDays(30))
			.issueDate(LocalDateTime.now())
			.quantity(0L)
			.build();

		Message mockMessage = createMockMessage(message);

		// Act & Assert
		RuntimeException thrown = assertThrows(
			RuntimeException.class,
			() -> couponIssuanceListener.onMessage(mockMessage)
		);

		assertTrue(thrown.getCause() instanceof CouponTemplateInsufficientQuantity);
		assertEquals(
			"해당 쿠폰템플릿 아이디 '1'의 발급수량이 부족합니다.",
			thrown.getCause().getMessage()
		);
	}

	@Test
	void testOnMessage_MessageNullHandling() {
		// Arrange
		Message mockMessage = new Message(new byte[0], new MessageProperties());

		// Act & Assert
		RuntimeException thrown = assertThrows(
			RuntimeException.class,
			() -> couponIssuanceListener.onMessage(mockMessage)
		);

		assertEquals("issuanceMessage is null", thrown.getMessage());
	}

	@Test
	void testOnMessage_CouponTemplateRepositoryThrowsException() {
		// Arrange
		Long couponId = 1L;
		Long userId = 1L;
		CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, userId);

		Message mockMessage = createMockMessage(message);

		// Mock behavior for repository
		when(couponTemplateRepository.findById(couponId)).thenThrow(new RuntimeException("Database error"));

		// Act & Assert
		RuntimeException thrown = assertThrows(
			RuntimeException.class,
			() -> couponIssuanceListener.onMessage(mockMessage)
		);

		assertTrue(thrown.getMessage().contains("Failed to process message"));
	}

	@Test
	void testOnMessage_UserAndCouponRepositoryThrowsException() {
		// Arrange
		Long couponId = 1L;
		Long userId = 1L;
		CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, userId);

		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(1000))
			.salePrice(BigDecimal.valueOf(100))
			.saleRate(null)
			.maxSalePrice(null)
			.type("sale")
			.build();

		CouponTemplate couponTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(LocalDateTime.now().plusDays(30))
			.issueDate(LocalDateTime.now())
			.quantity(10L)
			.build();

		Message mockMessage = createMockMessage(message);

		// Mock behavior for repositories
		when(couponTemplateRepository.findById(couponId)).thenReturn(Optional.of(couponTemplate));
		doThrow(new RuntimeException("Database error")).when(userAndCouponRepository).save(any(UserAndCoupon.class));

		// Act & Assert
		RuntimeException thrown = assertThrows(
			RuntimeException.class,
			() -> couponIssuanceListener.onMessage(mockMessage)
		);

		assertTrue(thrown.getMessage().contains("Failed to process message"));
	}
}