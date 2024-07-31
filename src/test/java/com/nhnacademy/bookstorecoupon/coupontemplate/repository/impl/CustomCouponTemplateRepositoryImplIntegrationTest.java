package com.nhnacademy.bookstorecoupon.coupontemplate.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.config.QuerydslTestConfig;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslTestConfig.class)
public class CustomCouponTemplateRepositoryImplIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    private CustomCouponTemplateRepositoryImpl customCouponTemplateRepository;

    @BeforeEach
    void setUp() {
        customCouponTemplateRepository = new CustomCouponTemplateRepositoryImpl(queryFactory);

        // Create CouponPolicy
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .minOrderPrice(BigDecimal.valueOf(1000))
            .salePrice(BigDecimal.valueOf(100))
            .saleRate(null)
            .maxSalePrice(null)
            .type("book")
            .build();


        CouponPolicy couponPolicy2 = CouponPolicy.builder()
            .minOrderPrice(BigDecimal.valueOf(1000))
            .salePrice(BigDecimal.valueOf(100))
            .saleRate(null)
            .maxSalePrice(null)
            .type("sale")
            .build();


        CouponPolicy couponPolicy3 = CouponPolicy.builder()
            .minOrderPrice(BigDecimal.valueOf(1000))
            .salePrice(BigDecimal.valueOf(100))
            .saleRate(null)
            .maxSalePrice(null)
            .type("category")
            .build();

        entityManager.persist(couponPolicy);
        entityManager.persist(couponPolicy2);
        entityManager.persist(couponPolicy3);

        // Create BookCoupon
        BookCoupon bookCoupon = BookCoupon.builder()
            .couponPolicy(couponPolicy)
            .bookId(123L)
            .bookTitle("Sample Book")
            .build();
        entityManager.persist(bookCoupon);

        // Create CategoryCoupon
        CategoryCoupon categoryCoupon = CategoryCoupon.builder()
            .couponPolicy(couponPolicy2)
            .categoryId(456L)
            .categoryName("Sample Category")
            .build();
        entityManager.persist(categoryCoupon);

        // Create CouponTemplate
        CouponTemplate couponTemplate = CouponTemplate.builder()
            .couponPolicy(couponPolicy)
            .expiredDate(LocalDateTime.now().plusMonths(1))
            .issueDate(LocalDateTime.now())
            .quantity(100L)
            .build();

        CouponTemplate couponTemplate2 = CouponTemplate.builder()
            .couponPolicy(couponPolicy2)
            .expiredDate(LocalDateTime.now().plusMonths(1))
            .issueDate(LocalDateTime.now())
            .quantity(100L)
            .build();

        CouponTemplate couponTemplate3 = CouponTemplate.builder()
            .couponPolicy(couponPolicy3)
            .expiredDate(LocalDateTime.now().plusMonths(1))
            .issueDate(LocalDateTime.now())
            .quantity(0L)
            .build();
        entityManager.persist(couponTemplate);
        entityManager.persist(couponTemplate2);
        entityManager.persist(couponTemplate3);


    }

    @AfterEach
    void resetAutoIncrementId() {
        entityManager.createNativeQuery("ALTER TABLE coupons_templates ALTER COLUMN coupon_template_id RESTART WITH 1")
            .executeUpdate();
    }

    @Test
    void testFindAllTemplatesByManagerPaging() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
        Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();

        // When
        Page<CouponTemplateResponseDTO> result = customCouponTemplateRepository.findAllTemplatesByManagerPaging(
            pageable, bookIdMap, categoryIdMap);

        // Then
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
    }

    @Test
    void testFindAllTemplatesByUserPaging() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        Map<Long, BookCoupon.BookInfo> bookIdMap = new HashMap<>();
        Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = new HashMap<>();

        // When
        Page<CouponTemplateResponseDTO> result = customCouponTemplateRepository.findAllTemplatesByUserPaging(pageable,
            bookIdMap, categoryIdMap);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }
}