package com.nhnacademy.bookstorecoupon.couponpolicy.service;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;

public interface CouponPolicyService {
	void issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO);
	void issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO);
	void issueSpecificBookCoupon(Long bookId, CouponPolicyRequestDTO requestDTO);
	void issueSpecificCategoryCoupon(Long categoryId, CouponPolicyRequestDTO requestDTO);
	void issueDiscountCoupon(CouponPolicyRequestDTO requestDTO);
}
