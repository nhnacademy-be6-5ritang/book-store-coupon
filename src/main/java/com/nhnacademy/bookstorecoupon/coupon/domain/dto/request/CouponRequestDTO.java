package com.nhnacademy.bookstorecoupon.coupon.domain.dto.request;

import java.time.LocalDateTime;

public record CouponRequestDTO(
	Long couponPolicyId,
	LocalDateTime expiredDate,
	LocalDateTime issueDate
) {}