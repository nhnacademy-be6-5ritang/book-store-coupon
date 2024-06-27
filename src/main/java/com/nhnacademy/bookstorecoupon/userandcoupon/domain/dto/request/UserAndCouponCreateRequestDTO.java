package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request;

public record UserAndCouponCreateRequestDTO(
		Long userId,
		Boolean isUsed

) {}