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

public class CouponPolicyUpdateRequestDTOTest {

	private static ValidatorFactory factory;
	private static Validator validator;

	@BeforeAll
	public static void init() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testValidDTO() {
		CouponPolicyUpdateRequestDTO dto = new CouponPolicyUpdateRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(100),  // salePrice
			null,                      // saleRate
			null,                      // maxSalePrice
			true                       // isUsed
		);

		Set<ConstraintViolation<CouponPolicyUpdateRequestDTO>> violations = validator.validate(dto);

		assertTrue(violations.isEmpty(), "There should be no constraint violations");
	}

	@Test
	public void testInvalidDTO_SaleRate() {
		CouponPolicyUpdateRequestDTO dto = new CouponPolicyUpdateRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			null,
			BigDecimal.valueOf(10.00),   // saleRate
			BigDecimal.valueOf(500),  // maxSalePrice
			true                       // isUsed
		);

		Set<ConstraintViolation<CouponPolicyUpdateRequestDTO>> violations = validator.validate(dto);

		assertEquals(1, violations.size(), "There should be one constraint violation");
		ConstraintViolation<CouponPolicyUpdateRequestDTO> violation = violations.iterator().next();
		assertEquals("숫자 값이 한계를 초과합니다(<0 자리>.<2 자리> 예상)", violation.getMessage());
	}


	@Test
	public void testInvalidDTO_MissingMinOrderPrice() {
		CouponPolicyUpdateRequestDTO dto = new CouponPolicyUpdateRequestDTO(
			null,                      // minOrderPrice
			BigDecimal.valueOf(100),  // salePrice
			null,                      // saleRate
			null,                      // maxSalePrice
			true                       // isUsed
		);

		Set<ConstraintViolation<CouponPolicyUpdateRequestDTO>> violations = validator.validate(dto);

		assertEquals(1, violations.size(), "There should be one constraint violation");
		ConstraintViolation<CouponPolicyUpdateRequestDTO> violation = violations.iterator().next();
		assertEquals("널이어서는 안됩니다", violation.getMessage());
	}

	@Test
	public void testInvalidDTO_MissingIsUsed() {
		CouponPolicyUpdateRequestDTO dto = new CouponPolicyUpdateRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(100),  // salePrice
			null,                      // saleRate
			null,                      // maxSalePrice
			null                       // isUsed (Invalid: Null)
		);

		Set<ConstraintViolation<CouponPolicyUpdateRequestDTO>> violations = validator.validate(dto);

		assertEquals(1, violations.size(), "There should be one constraint violation");
		ConstraintViolation<CouponPolicyUpdateRequestDTO> violation = violations.iterator().next();
		assertEquals("널이어서는 안됩니다", violation.getMessage());
	}
}