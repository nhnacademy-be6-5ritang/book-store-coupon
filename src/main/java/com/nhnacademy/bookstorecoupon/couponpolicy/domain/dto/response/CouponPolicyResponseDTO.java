package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CouponPolicyResponseDTO(
	Long id,
	BigDecimal minOrderPrice,
	BigDecimal salePrice,
	BigDecimal saleRate,
	BigDecimal maxSalePrice,
	String type,
	Boolean isUsed,
	Long bookId,
	String bookTitle,
	Long categoryId,
	String categoryName

) {

}
