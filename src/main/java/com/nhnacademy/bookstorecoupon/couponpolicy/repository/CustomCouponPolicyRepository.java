package com.nhnacademy.bookstorecoupon.couponpolicy.repository;

import java.util.List;
import java.util.Map;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

public interface CustomCouponPolicyRepository {
	List<CouponPolicyResponseDTO> findAllWithBooksAndCategories(Map<Long, Long> bookIdMap, Map<Long, Long> categoryIdMap);
}
