package com.nhnacademy.bookstorecoupon.coupontemplate.repository.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.QCouponTemplate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

class CustomCouponTemplateRepositoryImplUnitTest {

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private JPAQuery<Tuple> jpaQuery;

	@Mock
	private JPAQuery<Long> countQuery;

	@InjectMocks
	private CustomCouponTemplateRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindAllTemplatesByManagerPaging() {
		// Given
		QCouponTemplate qCouponTemplate = QCouponTemplate.couponTemplate;
		QCouponPolicy qCouponPolicy = QCouponPolicy.couponPolicy;

		// Mock Tuple objects
		Tuple tuple1 = mock(Tuple.class);
		when(tuple1.get(qCouponTemplate.id)).thenReturn(1L);
		when(tuple1.get(qCouponPolicy.id)).thenReturn(1L);
		when(tuple1.get(qCouponPolicy.minOrderPrice)).thenReturn(BigDecimal.valueOf(10000));
		when(tuple1.get(qCouponPolicy.salePrice)).thenReturn(BigDecimal.valueOf(5000));
		when(tuple1.get(qCouponPolicy.saleRate)).thenReturn(null);
		when(tuple1.get(qCouponPolicy.maxSalePrice)).thenReturn(null);
		when(tuple1.get(qCouponPolicy.type)).thenReturn("sale");
		when(tuple1.get(qCouponPolicy.isUsed)).thenReturn(true);
		when(tuple1.get(qCouponTemplate.expiredDate)).thenReturn(LocalDateTime.now().plusDays(30));
		when(tuple1.get(qCouponTemplate.issueDate)).thenReturn(LocalDateTime.now());
		when(tuple1.get(qCouponTemplate.quantity)).thenReturn(100L);

		Tuple tuple2 = mock(Tuple.class);
		when(tuple2.get(qCouponTemplate.id)).thenReturn(2L);
		when(tuple2.get(qCouponPolicy.id)).thenReturn(2L);
		when(tuple2.get(qCouponPolicy.minOrderPrice)).thenReturn(BigDecimal.valueOf(20000));
		when(tuple2.get(qCouponPolicy.salePrice)).thenReturn(BigDecimal.valueOf(10000));
		when(tuple2.get(qCouponPolicy.saleRate)).thenReturn(null);
		when(tuple2.get(qCouponPolicy.maxSalePrice)).thenReturn(null);
		when(tuple2.get(qCouponPolicy.type)).thenReturn("book");
		when(tuple2.get(qCouponPolicy.isUsed)).thenReturn(false);
		when(tuple2.get(qCouponTemplate.expiredDate)).thenReturn(LocalDateTime.now().plusDays(30));
		when(tuple2.get(qCouponTemplate.issueDate)).thenReturn(LocalDateTime.now());
		when(tuple2.get(qCouponTemplate.quantity)).thenReturn(50L);

		List<Tuple> tuples = List.of(tuple1, tuple2);

		when(queryFactory.select(
			qCouponTemplate.id,
			qCouponPolicy.id,
			qCouponPolicy.minOrderPrice,
			qCouponPolicy.salePrice,
			qCouponPolicy.saleRate,
			qCouponPolicy.maxSalePrice,
			qCouponPolicy.type,
			qCouponPolicy.isUsed,
			qCouponTemplate.expiredDate,
			qCouponTemplate.issueDate,
			qCouponTemplate.quantity
		)).thenReturn(jpaQuery);
		when(jpaQuery.from(qCouponTemplate)).thenReturn(jpaQuery);
		when(jpaQuery.join(qCouponTemplate.couponPolicy, qCouponPolicy)).thenReturn(jpaQuery);
		when(jpaQuery.orderBy(qCouponTemplate.id.desc())).thenReturn(jpaQuery);
		when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
		when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
		when(jpaQuery.fetch()).thenReturn(tuples);

		// Mock for counting templates
		when(queryFactory.select(qCouponTemplate.id.count())).thenReturn(countQuery);
		when(countQuery.from(qCouponTemplate)).thenReturn(countQuery);
		when(countQuery.fetchOne()).thenReturn(2L);

		// Prepare test data
		Pageable pageable = Pageable.ofSize(10);
		Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();

		// When
		Page<CouponTemplateResponseDTO> result = repository.findAllTemplatesByManagerPaging(pageable, bookIdMap,
			categoryIdMap);

		// Then
		assertEquals(2, result.getTotalElements());
		assertEquals(2, result.getContent().size());

		List<CouponTemplateResponseDTO> expectedTemplates = tuples.stream()
			.map(tuple -> new CouponTemplateResponseDTO(
				tuple.get(qCouponTemplate.id),
				tuple.get(qCouponPolicy.id),
				tuple.get(qCouponPolicy.minOrderPrice),
				tuple.get(qCouponPolicy.salePrice),
				tuple.get(qCouponPolicy.saleRate),
				tuple.get(qCouponPolicy.maxSalePrice),
				tuple.get(qCouponPolicy.type),
				tuple.get(qCouponPolicy.isUsed),
				null, // Assume bookIdMap is empty for this test
				null,
				null, // Assume categoryIdMap is empty for this test
				null,
				tuple.get(qCouponTemplate.expiredDate),
				tuple.get(qCouponTemplate.issueDate),
				tuple.get(qCouponTemplate.quantity)
			))
			.collect(Collectors.toList());

		assertEquals(expectedTemplates, result.getContent());

		verify(queryFactory).select(
			qCouponTemplate.id,
			qCouponPolicy.id,
			qCouponPolicy.minOrderPrice,
			qCouponPolicy.salePrice,
			qCouponPolicy.saleRate,
			qCouponPolicy.maxSalePrice,
			qCouponPolicy.type,
			qCouponPolicy.isUsed,
			qCouponTemplate.expiredDate,
			qCouponTemplate.issueDate,
			qCouponTemplate.quantity
		);
		verify(jpaQuery).from(qCouponTemplate);
		verify(jpaQuery).join(qCouponTemplate.couponPolicy, qCouponPolicy);
		verify(jpaQuery).orderBy(qCouponTemplate.id.desc());
		verify(jpaQuery).offset(anyLong());
		verify(jpaQuery).limit(anyLong());
		verify(jpaQuery).fetch();
		verify(queryFactory).select(qCouponTemplate.id.count());
		verify(countQuery).from(qCouponTemplate);
		verify(countQuery).fetchOne();
	}

}