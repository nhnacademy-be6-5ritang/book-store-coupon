package com.nhnacademy.bookstorecoupon.couponpolicy.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

public interface CustomCouponPolicyRepository {
	Page<CouponPolicyResponseDTO> findAllWithBooksAndCategories(Pageable pageable, Map<Long, Long> bookIdMap, Map<Long, Long> categoryIdMap);
}
