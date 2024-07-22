package com.nhnacademy.bookstorecoupon.couponpolicy.repository.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

class CustomCouponPolicyRepositoryImplTest {

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private JPAQuery<CouponPolicy> jpaQuery;

	@InjectMocks
	private CustomCouponPolicyRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindAllWithBooksAndCategories() {
		// Given
		QCouponPolicy qCouponPolicy = QCouponPolicy.couponPolicy;

		// Mock JPAQuery
		when(queryFactory.selectFrom(qCouponPolicy)).thenReturn(jpaQuery);
		when(jpaQuery.orderBy(qCouponPolicy.id.desc())).thenReturn(jpaQuery);
		when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
		when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);

		// Mock for fetching coupon policies
		List<CouponPolicy> couponPolicies = List.of(
			CouponPolicy.builder()
				.minOrderPrice(BigDecimal.valueOf(10000))
				.salePrice(BigDecimal.valueOf(50000))
				.saleRate(null)
				.maxSalePrice(null)
				.type("sale")
				.build(),
			CouponPolicy.builder()
				.minOrderPrice(BigDecimal.valueOf(20000))
				.salePrice(BigDecimal.valueOf(10000))
				.saleRate(null)
				.maxSalePrice(null)
				.type("book")
				.build()
		);

		when(jpaQuery.fetch()).thenReturn(couponPolicies);

		// Mock for counting coupon policies
		// Create a separate JPAQuery object for count query
		JPAQuery<Long> countQuery = mock(JPAQuery.class);
		when(queryFactory.select(qCouponPolicy.count())).thenReturn(countQuery);
		when(countQuery.from(qCouponPolicy)).thenReturn(countQuery);
		when(countQuery.fetchFirst()).thenReturn(2L);

		// Prepare test data
		Pageable pageable = Pageable.ofSize(10);
		Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();

		// When
		Page<CouponPolicyResponseDTO> result = repository.findAllWithBooksAndCategories(pageable, bookIdMap, categoryIdMap);

		// Then
		assertEquals(2, result.getTotalElements());
		assertEquals(2, result.getContent().size());
		verify(queryFactory).selectFrom(qCouponPolicy);
		verify(jpaQuery).orderBy(qCouponPolicy.id.desc());
		verify(jpaQuery).offset(anyLong());
		verify(jpaQuery).limit(anyLong());
		verify(jpaQuery).fetch();
		verify(queryFactory).select(qCouponPolicy.count());
		verify(countQuery).from(qCouponPolicy);
		verify(countQuery).fetchFirst();
	}

	@Test
	void testFindLatestCouponPolicyByType() {
		// Given
		QCouponPolicy qCouponPolicy = QCouponPolicy.couponPolicy;

		// Mock JPAQuery
		when(queryFactory.selectFrom(qCouponPolicy)).thenReturn(jpaQuery);
		when(jpaQuery.where((Predicate)any())).thenReturn(jpaQuery);
		when(jpaQuery.orderBy(qCouponPolicy.id.desc())).thenReturn(jpaQuery);

		// Mock for fetching latest coupon policy
		CouponPolicy latestCouponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(10000))
			.salePrice(BigDecimal.valueOf(5000))
			.saleRate(null)
			.maxSalePrice(null)
			.type("sale")
			.build();

		when(jpaQuery.fetchFirst()).thenReturn(latestCouponPolicy);

		// When
		Optional<CouponPolicy> result = repository.findLatestCouponPolicyByType("sale");

		// Then
		assertEquals(Optional.of(latestCouponPolicy), result);
		verify(queryFactory).selectFrom(qCouponPolicy);
		verify(jpaQuery).where((Predicate)any());
		verify(jpaQuery).orderBy(qCouponPolicy.id.desc());
		verify(jpaQuery).fetchFirst();
	}
}
