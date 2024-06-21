package com.nhnacademy.bookstorecoupon.couponpolicy.service;

import java.util.List;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

public interface CouponPolicyService {
	CouponPolicyResponseDTO issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO);

	CouponPolicyResponseDTO issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO);

	CouponPolicyResponseDTO issueSpecificBookCoupon(Long bookId, CouponPolicyRequestDTO requestDTO);

	CouponPolicyResponseDTO issueSpecificCategoryCoupon(Long categoryId, CouponPolicyRequestDTO requestDTO);

	CouponPolicyResponseDTO issueDiscountCoupon(CouponPolicyRequestDTO requestDTO);

	List<CouponPolicyResponseDTO> getAllCouponPolicies();

	CouponPolicyResponseDTO getCouponPolicyById(Long id);

	CouponPolicyResponseDTO updateCouponPolicy(Long id, CouponPolicyRequestDTO requestDTO);

	void deleteCouponPolicy(Long id);

}
