package com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CouponTemplateRequestDTO(
	@NotNull
	@Positive
	Long couponPolicyId,

	@NotNull
	LocalDateTime expiredDate,

	@NotNull
	LocalDateTime issueDate,

	@NotNull
	@Positive
	Long quantity
) {}