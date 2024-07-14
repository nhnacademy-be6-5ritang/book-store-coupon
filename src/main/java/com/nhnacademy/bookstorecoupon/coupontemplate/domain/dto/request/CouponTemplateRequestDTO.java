package com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request;

import java.time.LocalDateTime;

public record CouponTemplateRequestDTO(
	Long couponPolicyId,


	LocalDateTime expiredDate,

	LocalDateTime issueDate,

	Long quantity
) {}