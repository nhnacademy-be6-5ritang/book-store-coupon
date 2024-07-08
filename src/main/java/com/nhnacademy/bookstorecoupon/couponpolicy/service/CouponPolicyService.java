package com.nhnacademy.bookstorecoupon.couponpolicy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

public interface CouponPolicyService {
	void issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO);

	void issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO);

	void issueSpecificBookCoupon(CouponPolicyRequestDTO requestDTO);

	void issueSpecificCategoryCoupon(CouponPolicyRequestDTO requestDTO);

	void issueDiscountCoupon(CouponPolicyRequestDTO requestDTO);

	Page<CouponPolicyResponseDTO> getAllCouponPolicies(Pageable pageable);



	void updateCouponPolicy(Long id, CouponPolicyUpdateRequestDTO requestDTO);


}
