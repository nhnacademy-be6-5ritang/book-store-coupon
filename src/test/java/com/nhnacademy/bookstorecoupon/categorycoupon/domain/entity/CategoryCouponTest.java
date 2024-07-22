package com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.global.config.QuerydslConfig;

@DataJpaTest
@Import(QuerydslConfig.class)
class CategoryCouponTest {

	@Autowired
	private CategoryCouponRepository categoryCouponRepository;

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
			.type("category")
			.build();
		couponPolicyRepository.save(couponPolicy);
	}

	@Test
	public void testCreateCategoryCoupon() {
		// Given
		CategoryCoupon categoryCoupon = CategoryCoupon.builder()
			.couponPolicy(couponPolicy)
			.categoryId(456L)
			.categoryName("Test Category")
			.build();

		// When
		CategoryCoupon savedCategoryCoupon = categoryCouponRepository.save(categoryCoupon);

		// Then
		assertThat(savedCategoryCoupon).isNotNull();
		assertThat(savedCategoryCoupon.getId()).isNotNull();
		assertThat(savedCategoryCoupon.getCategoryId()).isEqualTo(456L);
		assertThat(savedCategoryCoupon.getCategoryName()).isEqualTo("Test Category");
		assertThat(savedCategoryCoupon.getCouponPolicy()).isEqualTo(couponPolicy);
	}
}