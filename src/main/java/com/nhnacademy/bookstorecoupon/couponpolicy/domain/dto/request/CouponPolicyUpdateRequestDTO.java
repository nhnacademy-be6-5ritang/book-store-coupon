package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request;

import java.math.BigDecimal;

public record CouponPolicyUpdateRequestDTO(BigDecimal minOrderPrice, BigDecimal salePrice, BigDecimal saleRate,
										   BigDecimal maxSalePrice) {
}