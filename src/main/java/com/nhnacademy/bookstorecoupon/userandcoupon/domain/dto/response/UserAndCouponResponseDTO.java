package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserAndCouponResponseDTO(
	Long id,
	Long userId,

	LocalDateTime usedDate,

	Boolean isUsed,

	LocalDateTime expiredDate,

	LocalDateTime issueDate,

	BigDecimal minOrderPrice,
	BigDecimal salePrice,
	BigDecimal saleRate,
	BigDecimal maxSalePrice,
	String type,
	Boolean policyIsUsed,
	Long bookId,
	Long categoryId


) {


}