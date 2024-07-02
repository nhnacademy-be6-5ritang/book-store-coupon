package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

public record CouponPolicyResponseDTO(Long couponPolicyId, BigDecimal minOrderPrice, BigDecimal salePrice, BigDecimal saleRate,
									  BigDecimal maxSalePrice, String type, Boolean isUsed) {

	public static CouponPolicyResponseDTO fromCouponPolicy(CouponPolicy policy) {
		return new CouponPolicyResponseDTO(
			policy.getId(),
			policy.getMinOrderPrice(),
			policy.getSalePrice(),
			policy.getSaleRate(),
			policy.getMaxSalePrice(),
			policy.getType(),
			policy.getIsUsed()
			);
	}
}
