package com.nhnacademy.bookstorecoupon.bookcoupon.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.config.QuerydslTestConfig;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslTestConfig.class) // QueryDSL 설정 파일 임포트
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스 연결
public class CustomBookCouponRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private CustomBookCouponRepositoryImpl customBookCouponRepository;

	@BeforeEach
	void setUp() {
		customBookCouponRepository = new CustomBookCouponRepositoryImpl(jpaQueryFactory);

		// Create CouponPolicy
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(1000))
			.salePrice(BigDecimal.valueOf(100))
			.saleRate(null)
			.maxSalePrice(null)
			.type("book")
			.build();
		entityManager.persist(couponPolicy);

		// Create BookCoupons
		BookCoupon bookCoupon1 = BookCoupon.builder()
			.couponPolicy(couponPolicy)
			.bookId(123L)
			.bookTitle("Sample Book 1")
			.build();
		entityManager.persist(bookCoupon1);



		// Flush and clear to ensure persistence context is updated
		entityManager.flush();
		entityManager.clear();
	}





	@Test
	void testFetchBookIdMap() {


		// When
		Map<Long, BookCoupon.BookInfo> bookIdMap = customBookCouponRepository.fetchBookIdMap();

		// Then
		assertThat(bookIdMap).hasSize(1); // Expecting one BookCoupon entry

		// Verify the specific book ID
		assertThat(bookIdMap).containsKey(1L); // The key should be 123L

		// Verify the details of the BookInfo
		BookCoupon.BookInfo expectedBookInfo = new BookCoupon.BookInfo(123L, "Sample Book 1");
		BookCoupon.BookInfo actualBookInfo = bookIdMap.get(1L);

		assertThat(actualBookInfo.bookId).isEqualTo(expectedBookInfo.bookId);
		assertThat(actualBookInfo.bookTitle).isEqualTo(expectedBookInfo.bookTitle);
	}
}