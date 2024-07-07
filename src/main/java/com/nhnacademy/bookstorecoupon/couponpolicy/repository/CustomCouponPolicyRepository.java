package com.nhnacademy.bookstorecoupon.couponpolicy.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

public interface CustomCouponPolicyRepository {
	Page<CouponPolicyResponseDTO> findAllWithBooksAndCategories(Pageable pageable, Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);
}
