package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request;

public record UserAndCouponCreateRequestDTO(
		String userEmail,
		Boolean isUsed
) {}