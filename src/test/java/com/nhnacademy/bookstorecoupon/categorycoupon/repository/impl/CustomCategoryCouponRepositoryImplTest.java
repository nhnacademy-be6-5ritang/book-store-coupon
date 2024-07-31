package com.nhnacademy.bookstorecoupon.categorycoupon.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.config.QuerydslTestConfig;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomCategoryCouponRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private CustomCategoryCouponRepositoryImpl customCategoryCouponRepository;

	@BeforeEach
	void setUp() {
		customCategoryCouponRepository = new CustomCategoryCouponRepositoryImpl(jpaQueryFactory);

		// Create CouponPolicy
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(1000))
			.salePrice(BigDecimal.valueOf(100))
			.saleRate(null)
			.maxSalePrice(null)
			.type("category")
			.build();
		entityManager.persist(couponPolicy);

		// Create CategoryCoupons
		CategoryCoupon categoryCoupon1 = CategoryCoupon.builder()
			.couponPolicy(couponPolicy)
			.categoryId(456L)
			.categoryName("Sample Category 1")
			.build();
		entityManager.persist(categoryCoupon1);

		// Flush and clear to ensure persistence context is updated
		entityManager.flush();
		entityManager.clear();
	}

	@AfterEach
	void resetAutoIncrementId() {

		entityManager.createQuery("DELETE FROM CategoryCoupon").executeUpdate();
		entityManager.createQuery("DELETE FROM CouponPolicy").executeUpdate();
	}



	@Test
	void testFetchCategoryIdMap() {
		// When
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = customCategoryCouponRepository.fetchCategoryIdMap();

		// Then
		assertThat(categoryIdMap).hasSize(1); // Expecting one CategoryCoupon entry

		// Verify the specific category ID
		assertThat(categoryIdMap).containsKey(1L); // The key should be 456L

		// Verify the details of the CategoryInfo
		CategoryCoupon.CategoryInfo expectedCategoryInfo = new CategoryCoupon.CategoryInfo(456L, "Sample Category 1");
		CategoryCoupon.CategoryInfo actualCategoryInfo = categoryIdMap.get(1L);

		assertThat(actualCategoryInfo.categoryId).isEqualTo(expectedCategoryInfo.categoryId);
		assertThat(actualCategoryInfo.categoryName).isEqualTo(expectedCategoryInfo.categoryName);
	}
}
