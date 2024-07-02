package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

public record CouponPolicyResponseDTO2(
	Long couponPolicyId,
	BigDecimal minOrderPrice,
	BigDecimal salePrice,
	BigDecimal saleRate,
	BigDecimal maxSalePrice,
	String type,
	Boolean isUsed,
	Long bookId,
	Long categoryId
) {
	public static CouponPolicyResponseDTO2 fromCouponPolicy(CouponPolicy policy, Long bookId, Long categoryId) {
		return new CouponPolicyResponseDTO2(
			policy.getId(),
			policy.getMinOrderPrice(),
			policy.getSalePrice(),
			policy.getSaleRate(),
			policy.getMaxSalePrice(),
			policy.getType(),
			policy.getIsUsed(),
			bookId,
			categoryId
		);
	}


}
