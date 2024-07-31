// package com.nhnacademy.bookstorecoupon.global.listener;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.amqp.core.Message;
// import org.springframework.amqp.core.MessageProperties;
// import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//
// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
// import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
// import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
// import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;
// import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
// import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;
//
// class CouponIssuanceListenerTest {
//
// 	@Mock
// 	private UserAndCouponRepository userAndCouponRepository;
//
// 	@Mock
// 	private CouponTemplateRepository couponTemplateRepository;
//
// 	@InjectMocks
// 	private CouponIssuanceListener couponIssuanceListener;
//
// 	private final ObjectMapper objectMapper = new ObjectMapper()
// 		.findAndRegisterModules()
// 		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
// 	private final Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
//
// 	@BeforeEach
// 	void setUp() {
// 		MockitoAnnotations.openMocks(this);
// 	}
//
// 	private Message createMockMessage(CouponIssuanceMessage message) {
// 		try {
// 			byte[] messageBody = objectMapper.writeValueAsBytes(message);
// 			MessageProperties messageProperties = new MessageProperties();
// 			return new Message(messageBody, messageProperties);
// 		} catch (Exception e) {
// 			throw new RuntimeException("Failed to create mock message", e);
// 		}
// 	}
//
//
//
//
// 	@Test
// 	void testOnMessage_CouponTemplateRepositoryThrowsException() {
// 		// Arrange
// 		Long couponId = 1L;
// 		Long userId = 1L;
// 		CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, userId);
//
// 		Message mockMessage = createMockMessage(message);
//
// 		// Mock behavior for repository
// 		when(couponTemplateRepository.findById(couponId)).thenThrow(new RuntimeException("Database error"));
//
// 		// Act & Assert
// 		RuntimeException thrown = assertThrows(
// 			RuntimeException.class,
// 			() -> couponIssuanceListener.onMessage(mockMessage)
// 		);
//
// 		assertTrue(thrown.getMessage().contains("Failed to process message"));
// 	}
//
// 	@Test
// 	void testOnMessage_UserAndCouponRepositoryThrowsException() {
// 		// Arrange
// 		Long couponId = 1L;
// 		Long userId = 1L;
// 		CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, userId);
//
// 		CouponPolicy couponPolicy = CouponPolicy.builder()
// 			.minOrderPrice(BigDecimal.valueOf(1000))
// 			.salePrice(BigDecimal.valueOf(100))
// 			.saleRate(null)
// 			.maxSalePrice(null)
// 			.type("sale")
// 			.build();
//
// 		CouponTemplate couponTemplate = CouponTemplate.builder()
// 			.couponPolicy(couponPolicy)
// 			.expiredDate(LocalDateTime.now().plusDays(30))
// 			.issueDate(LocalDateTime.now())
// 			.quantity(10L)
// 			.build();
//
// 		Message mockMessage = createMockMessage(message);
//
// 		// Mock behavior for repositories
// 		when(couponTemplateRepository.findById(couponId)).thenReturn(Optional.of(couponTemplate));
// 		doThrow(new RuntimeException("Database error")).when(userAndCouponRepository).save(any(UserAndCoupon.class));
//
// 		// Act & Assert
// 		RuntimeException thrown = assertThrows(
// 			RuntimeException.class,
// 			() -> couponIssuanceListener.onMessage(mockMessage)
// 		);
//
// 		assertTrue(thrown.getMessage().contains("Failed to process message"));
// 	}
// }