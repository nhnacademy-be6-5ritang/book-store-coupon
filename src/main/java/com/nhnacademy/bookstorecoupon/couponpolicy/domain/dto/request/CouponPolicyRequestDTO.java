package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request;

import java.math.BigDecimal;

public record CouponPolicyRequestDTO(BigDecimal minOrderPrice, BigDecimal salePrice, BigDecimal saleRate,
									 BigDecimal maxSalePrice, String type, Long bookId, String bookTitle, Long categoryId, String categoryName) {
}