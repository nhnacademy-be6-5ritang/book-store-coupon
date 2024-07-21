package com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.global.config.QuerydslConfig;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;

@DataJpaTest
@Import(QuerydslConfig.class)
public class UserAndCouponTest {

	@Autowired
	private UserAndCouponRepository userAndCouponRepository;

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
	public void testCreateUserAndCoupon() {
		// Given
		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(123L)
			.usedDate(null)
			.isUsed(false)
			.expiredDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
			.issueDate(LocalDateTime.now())
			.build();

		// When
		UserAndCoupon savedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

		// Then
		assertThat(savedUserAndCoupon).isNotNull();
		assertThat(savedUserAndCoupon.getId()).isNotNull();
		assertThat(savedUserAndCoupon.getCouponPolicy()).isEqualTo(couponPolicy);
		assertThat(savedUserAndCoupon.getUserId()).isEqualTo(123L);
		assertThat(savedUserAndCoupon.getUsedDate()).isNull();
		assertThat(savedUserAndCoupon.getIsUsed()).isFalse();
		assertThat(savedUserAndCoupon.getExpiredDate()).isEqualTo(LocalDateTime.of(2024, 12, 31, 23, 59, 59));
		assertThat(savedUserAndCoupon.getIssueDate()).isNotNull();
	}

	@Test
	public void testUpdateUserAndCoupon() {
		// Given
		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(123L)
			.usedDate(null)
			.isUsed(false)
			.expiredDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
			.issueDate(LocalDateTime.now())
			.build();
		UserAndCoupon savedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

		LocalDateTime newUsedDate = LocalDateTime.now();
		Boolean newIsUsed = true;

		// When
		savedUserAndCoupon.update(newUsedDate, newIsUsed);

		// Then
		assertThat(savedUserAndCoupon).isNotNull();
		assertThat(savedUserAndCoupon.getUsedDate()).isEqualTo(newUsedDate);
		assertThat(savedUserAndCoupon.getIsUsed()).isTrue();
	}
}