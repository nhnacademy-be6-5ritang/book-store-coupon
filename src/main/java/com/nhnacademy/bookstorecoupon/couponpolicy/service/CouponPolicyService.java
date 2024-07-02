package com.nhnacademy.bookstorecoupon.couponpolicy.service;

import java.util.List;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

public interface CouponPolicyService {
	void issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO);

	void issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO);

	void issueSpecificBookCoupon(Long bookId, CouponPolicyRequestDTO requestDTO);

	void issueSpecificCategoryCoupon(Long categoryId, CouponPolicyRequestDTO requestDTO);

	void issueDiscountCoupon(CouponPolicyRequestDTO requestDTO);

	List<CouponPolicyResponseDTO> getAllCouponPolicies();

	CouponPolicyResponseDTO getCouponPolicyById(Long id);

	void updateCouponPolicy(Long id, CouponPolicyUpdateRequestDTO requestDTO);


}
