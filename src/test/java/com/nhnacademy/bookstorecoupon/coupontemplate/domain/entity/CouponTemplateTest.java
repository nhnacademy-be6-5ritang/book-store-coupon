package com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.global.config.QuerydslConfig;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
class CouponTemplateTest {

	@Autowired
	private CouponTemplateRepository couponTemplateRepository;

	@Autowired
	private CouponPolicyRepository couponPolicyRepository;

	private CouponPolicy couponPolicy;

	@BeforeEach
	public void setUp() {
		couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(5000))
			.salePrice(BigDecimal.valueOf(10000))
			.saleRate(null)
			.maxSalePrice(null)
			.type("welcome")
			.build();
		couponPolicyRepository.save(couponPolicy);
	}

	@Test
	public void testCreateCouponTemplate() {
		// Given
		LocalDateTime now = LocalDateTime.now();
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(now.plusDays(30))
			.issueDate(now)
			.quantity(100L)
			.build();

		// When
		CouponTemplate savedCouponTemplate = couponTemplateRepository.save(couponTemplate);

		// Then
		assertThat(savedCouponTemplate).isNotNull();
		assertThat(savedCouponTemplate.getId()).isNotNull();
		assertThat(savedCouponTemplate.getCouponPolicy()).isEqualTo(couponPolicy);
		assertThat(savedCouponTemplate.getExpiredDate()).isEqualTo(now.plusDays(30));
		assertThat(savedCouponTemplate.getIssueDate()).isEqualTo(now);
		assertThat(savedCouponTemplate.getQuantity()).isEqualTo(100L);
	}

	@Test
	public void testUpdateCouponTemplate() {
		// Given
		LocalDateTime now = LocalDateTime.now();
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(now.plusDays(30))
			.issueDate(now)
			.quantity(100L)
			.build();
		CouponTemplate savedCouponTemplate = couponTemplateRepository.save(couponTemplate);

		// When
		savedCouponTemplate.update(200L);

		// Then
		assertThat(savedCouponTemplate).isNotNull();
		assertThat(savedCouponTemplate.getQuantity()).isEqualTo(200L);
	}
}