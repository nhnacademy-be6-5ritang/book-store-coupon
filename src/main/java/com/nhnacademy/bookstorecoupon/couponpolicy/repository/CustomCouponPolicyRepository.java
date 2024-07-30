package com.nhnacademy.bookstorecoupon.couponpolicy.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

public interface CustomCouponPolicyRepository {
	Page<CouponPolicyResponseDTO> findAllWithBooksAndCategories(Pageable pageable, Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);
	Optional<CouponPolicy> findLatestCouponPolicyByType(String type);
}
