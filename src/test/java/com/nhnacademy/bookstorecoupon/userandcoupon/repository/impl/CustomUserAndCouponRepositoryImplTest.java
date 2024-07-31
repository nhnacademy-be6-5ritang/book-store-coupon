package com.nhnacademy.bookstorecoupon.userandcoupon.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.config.QuerydslTestConfig;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomUserAndCouponRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	private CustomUserAndCouponRepositoryImpl repository;



	@BeforeEach
	void setUp() {
		repository = new CustomUserAndCouponRepositoryImpl(entityManager);
	}


	@Test
	void testFindAllByUserPaging() {
		// 데이터 설정
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("book")
			.build();



		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		entityManager.persist(couponPolicy);

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.build();

		UserAndCoupon userAndCoupon2 = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.build();
		entityManager.persist(userAndCoupon);
		entityManager.persist(userAndCoupon2);

		Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
		bookIdMap.put(1L, new BookCoupon.BookInfo(1L, "Book Title"));

		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();
		Pageable pageable = PageRequest.of(0, 10);





		// 테스트 실행
		Page<UserAndCouponResponseDTO> result = repository.findAllByUserPaging(pageable, 1L, bookIdMap, categoryIdMap);

		// 검증
		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		UserAndCouponResponseDTO dto = result.getContent().get(0);
		assertEquals(2L, dto.id());
		assertEquals(1L, dto.userId());
		assertNotNull(dto.expiredDate());
		assertNotNull(dto.issueDate());
		assertEquals(0, BigDecimal.valueOf(100).compareTo(dto.minOrderPrice()));
		assertEquals(0, BigDecimal.valueOf(10).compareTo(dto.salePrice()));
		assertEquals("book", dto.type());
		assertTrue(dto.policyIsUsed());
		assertEquals(1L, dto.bookId());
		assertEquals("Book Title", dto.bookTitle());


	}

	@Test
	void testFindAllByManagerPaging() {
		// 데이터 설정
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("welcome")
			.build();





		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		entityManager.persist(couponPolicy);

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.build();

		UserAndCoupon userAndCoupon2 = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.build();

		entityManager.persist(userAndCoupon);
		entityManager.persist(userAndCoupon2);

		Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();
		Pageable pageable = PageRequest.of(0, 10);

		// 테스트 실행
		Page<UserAndCouponResponseDTO> result = repository.findAllByManagerPaging(pageable, "welcome", 1L, bookIdMap, categoryIdMap);

		// 검증
		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		UserAndCouponResponseDTO dto = result.getContent().get(0);
		assertEquals(2L, dto.id());
		assertEquals(1L, dto.userId());
		assertNotNull(dto.expiredDate());
		assertNotNull(dto.issueDate());
		assertEquals(0, BigDecimal.valueOf(100).compareTo(dto.minOrderPrice()));
		assertEquals(0, BigDecimal.valueOf(10).compareTo(dto.salePrice()));
		assertEquals("welcome", dto.type());
		assertTrue(dto.policyIsUsed());
		assertNull(dto.bookId());
		assertNull(dto.bookTitle());
		assertNull(dto.categoryId());
		assertNull(dto.categoryName());
	}

	@Test
	void testFindCouponByOrder() {
		// 데이터 설정
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("book")
			.build();


		CouponPolicy couponPolicy2 = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("welcome")
			.build();

		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		entityManager.persist(couponPolicy);
		entityManager.persist(couponPolicy2);

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.expiredDate(LocalDateTime.now().plusDays(365))
			.issueDate(LocalDateTime.now().minusDays(10))
			.build();



		UserAndCoupon userAndCoupon2 = UserAndCoupon.builder()
			.couponPolicy(couponPolicy2)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.expiredDate(LocalDateTime.now().plusDays(365))
			.issueDate(LocalDateTime.now().minusDays(10))
			.build();
		entityManager.persist(userAndCoupon);
		entityManager.persist(userAndCoupon2);

		Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
		bookIdMap.put(1L, new BookCoupon.BookInfo(1L, "Book Title"));


		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();


		List<Long> bookIds = Collections.singletonList(1L);
		List<Long> categoryIds = Collections.emptyList();
		BigDecimal bookPrice = BigDecimal.valueOf(100);

		// 테스트 실행
		List<UserAndCouponResponseDTO> results = repository.findCouponByOrder(
			1L,
			bookIdMap,
			categoryIdMap,
			bookIds,
			categoryIds,
			bookPrice
		);

		// 검증
		assertNotNull(results);
		assertEquals(2, results.size());
		UserAndCouponResponseDTO dto = results.get(0);
		assertEquals(2L, dto.id());
		assertEquals(1L, dto.userId());
		assertNotNull(dto.expiredDate());
		assertNotNull(dto.issueDate());
		assertEquals(0, BigDecimal.valueOf(100).compareTo(dto.minOrderPrice()));
		assertEquals(0, BigDecimal.valueOf(10).compareTo(dto.salePrice()));
		assertEquals("welcome", dto.type());
		assertTrue(dto.policyIsUsed());

	}

	@Test
	void testFindCouponByCartOrder() {
		// 데이터 설정
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("book")
			.build();


		CouponPolicy couponPolicy2 = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("welcome")
			.build();

		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		couponPolicy.update(BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, true);
		entityManager.persist(couponPolicy);
		entityManager.persist(couponPolicy2);

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.expiredDate(LocalDateTime.now().plusDays(365))
			.issueDate(LocalDateTime.now().minusDays(10))
			.build();



		UserAndCoupon userAndCoupon2 = UserAndCoupon.builder()
			.couponPolicy(couponPolicy2)
			.userId(1L)
			.expiredDate(LocalDateTime.now().plusDays(1))
			.issueDate(LocalDateTime.now())
			.isUsed(false)
			.usedDate(null)
			.expiredDate(LocalDateTime.now().plusDays(365))
			.issueDate(LocalDateTime.now().minusDays(10))
			.build();
		entityManager.persist(userAndCoupon);
		entityManager.persist(userAndCoupon2);

		Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
		bookIdMap.put(1L, new BookCoupon.BookInfo(1L, "Book Title"));


		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();


		// GetBookByOrderCouponResponse 리스트 생성
		List<GetBookByOrderCouponResponse> bookResponses = Collections.singletonList(
			new GetBookByOrderCouponResponse(1L, BigDecimal.valueOf(100), Collections.emptyList())
		);

		// 테스트 실행
		List<UserAndCouponResponseDTO> results = repository.findCouponByCartOrder(
			1L,
			bookIdMap,
			categoryIdMap,
			bookResponses
		);

		// 검증
		assertNotNull(results);
		assertEquals(2, results.size());
		UserAndCouponResponseDTO dto = results.get(0);
		assertEquals(2L, dto.id());
		assertEquals(1L, dto.userId());
		assertNotNull(dto.expiredDate());
		assertNotNull(dto.issueDate());
		assertEquals(0, BigDecimal.valueOf(100).compareTo(dto.minOrderPrice()));
		assertEquals(0, BigDecimal.valueOf(10).compareTo(dto.salePrice()));
		assertEquals("welcome", dto.type());
		assertTrue(dto.policyIsUsed());
	}
}