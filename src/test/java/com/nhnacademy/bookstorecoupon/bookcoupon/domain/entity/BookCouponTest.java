package com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.global.config.QuerydslConfig;

@DataJpaTest
@Import(QuerydslConfig.class)
public class BookCouponTest {

	@Autowired
	private BookCouponRepository bookCouponRepository;

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
			.type("book")
			.build();
		couponPolicyRepository.save(couponPolicy);
	}

	@Test
	public void testCreateBookCoupon() {
		// Given
		BookCoupon bookCoupon = BookCoupon.builder()
			.couponPolicy(couponPolicy)
			.bookId(123L)
			.bookTitle("Test Book")
			.build();

		// When
		BookCoupon savedBookCoupon = bookCouponRepository.save(bookCoupon);

		// Then
		assertThat(savedBookCoupon).isNotNull();
		assertThat(savedBookCoupon.getId()).isNotNull();
		assertThat(savedBookCoupon.getBookId()).isEqualTo(123L);
		assertThat(savedBookCoupon.getBookTitle()).isEqualTo("Test Book");
		assertThat(savedBookCoupon.getCouponPolicy()).isEqualTo(couponPolicy);
	}
}