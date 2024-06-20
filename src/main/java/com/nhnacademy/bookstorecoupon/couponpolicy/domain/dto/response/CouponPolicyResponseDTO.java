package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response;

import java.math.BigDecimal;

public record CouponPolicyResponseDTO(BigDecimal minOrderPrice, BigDecimal salePrice, BigDecimal saleRate,
									  BigDecimal maxSalePrice, String type) {
}