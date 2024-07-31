package com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.global.config.QuerydslConfig;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
class CouponPolicyTest {

	@Autowired
	private CouponPolicyRepository couponPolicyRepository;

	private CouponPolicy couponPolicy;

	@BeforeEach
	public void setUp() {
		couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(5000))
			.salePrice(null)
			.saleRate(BigDecimal.valueOf(0.12))
			.maxSalePrice(BigDecimal.valueOf(20000))
			.type("sale")
			.build();
		couponPolicyRepository.save(couponPolicy);
	}

	@Test
	public void testCreateCouponPolicy() {
		// Given
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(5000))
			.salePrice(BigDecimal.valueOf(10000))
			.saleRate(null)
			.maxSalePrice(null)
			.type("welcome")
			.build();

		// When
		CouponPolicy savedCouponPolicy = couponPolicyRepository.save(couponPolicy);

		// Then
		assertThat(savedCouponPolicy).isNotNull();
		assertThat(savedCouponPolicy.getId()).isNotNull();
		assertThat(savedCouponPolicy.getMinOrderPrice()).isEqualTo(BigDecimal.valueOf(5000));
		assertThat(savedCouponPolicy.getSalePrice()).isEqualTo(BigDecimal.valueOf(10000));
		assertThat(savedCouponPolicy.getSaleRate()).isNull();
		assertThat(savedCouponPolicy.getMaxSalePrice()).isNull();
		assertThat(savedCouponPolicy.getType()).isEqualTo("welcome");
	}

	@Test
	public void testUpdateCouponPolicy() {
		// Given
		BigDecimal newMinOrderPrice = BigDecimal.valueOf(15000);
		BigDecimal newSaleRate = BigDecimal.valueOf(0.2);
		BigDecimal newMaxSalePrice = BigDecimal.valueOf(22000);
		Boolean newIsUsed = false;

		// When
		couponPolicy.update(newMinOrderPrice, null, newSaleRate, newMaxSalePrice, newIsUsed);
		CouponPolicy updatedCouponPolicy = couponPolicyRepository.save(couponPolicy);

		// Then
		assertThat(updatedCouponPolicy).isNotNull();
		assertThat(updatedCouponPolicy.getMinOrderPrice()).isEqualTo(newMinOrderPrice);
		assertThat(updatedCouponPolicy.getSalePrice()).isNull();
		assertThat(updatedCouponPolicy.getSaleRate()).isEqualTo(newSaleRate);
		assertThat(updatedCouponPolicy.getMaxSalePrice()).isEqualTo(newMaxSalePrice);
		assertThat(updatedCouponPolicy.getIsUsed()).isEqualTo(newIsUsed);
	}

	@Test
	public void testCreateFromRequestDTO() {
		// Given
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(5000),
			BigDecimal.valueOf(10000),
			null,
			null,
			"welcome",
			null, // Optional field, so it can be null
			null,
			null,
			null
		);

		// When
		CouponPolicy createdCouponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		// Then
		assertThat(createdCouponPolicy).isNotNull();
		assertThat(createdCouponPolicy.getMinOrderPrice()).isEqualTo(requestDTO.minOrderPrice());
		assertThat(createdCouponPolicy.getSalePrice()).isEqualTo(requestDTO.salePrice());
		assertThat(createdCouponPolicy.getSaleRate()).isEqualTo(requestDTO.saleRate());
		assertThat(createdCouponPolicy.getMaxSalePrice()).isEqualTo(requestDTO.maxSalePrice());
		assertThat(createdCouponPolicy.getType()).isEqualTo(requestDTO.type());
	}
}