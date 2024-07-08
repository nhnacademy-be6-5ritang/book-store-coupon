package com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CouponTemplateResponseDTO(
	Long id,
	Long couponPolicyId,
	BigDecimal minOrderPrice,
	BigDecimal salePrice,
	BigDecimal saleRate,
	BigDecimal maxSalePrice,
	String type,
	Boolean isUsed,
	Long bookId,
	String bookTitle,
	Long categoryId,
	String categoryName,
	LocalDateTime expiredDate,
	LocalDateTime issueDate
) {

}
