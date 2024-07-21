package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CouponPolicyRequestDTOTest {

	private static ValidatorFactory factory;
	private static Validator validator;

	@BeforeAll
	public static void init() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testValidDTO() {
		CouponPolicyRequestDTO dto = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(100),  // salePrice
			null,                      // saleRate
			null,                      // maxSalePrice
			"book",                // type
			1L,                        // bookId
			"Book Title",              // bookTitle
			null,                      // categoryId
			null                       // categoryName
		);

		Set<ConstraintViolation<CouponPolicyRequestDTO>> violations = validator.validate(dto);

		assertTrue(violations.isEmpty(), "유효성테스트 통과");
	}

	@Test
	public void testInvalidDTO_SaleRate() {
		CouponPolicyRequestDTO dto = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			null,  // salePrice
			BigDecimal.valueOf(10.00),   // saleRate
			BigDecimal.valueOf(100),                      // maxSalePrice
			"sale",                // type
			null,                        // bookId
			null,              // bookTitle
			null,                      // categoryId
			null                       // categoryName
		);

		Set<ConstraintViolation<CouponPolicyRequestDTO>> violations = validator.validate(dto);

		assertEquals(1, violations.size(), "There should be one constraint violation");
		ConstraintViolation<CouponPolicyRequestDTO> violation = violations.iterator().next();
		assertEquals("숫자 값이 한계를 초과합니다(<0 자리>.<2 자리> 예상)", violation.getMessage());
	}


	@Test
	public void testInvalidDTO_MissingType() {
		CouponPolicyRequestDTO dto = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(1000),                      // salePrice
			null,                      // saleRate
			null,                      // maxSalePrice
			"",                        // type (Invalid: Empty)
			null,                      // bookId
			null,                      // bookTitle
			null,                      // categoryId
			null                       // categoryName
		);

		Set<ConstraintViolation<CouponPolicyRequestDTO>> violations = validator.validate(dto);

		assertEquals(1, violations.size(), "There should be one constraint violation");
		ConstraintViolation<CouponPolicyRequestDTO> violation = violations.iterator().next();
		assertEquals("공백일 수 없습니다", violation.getMessage());
	}
}
