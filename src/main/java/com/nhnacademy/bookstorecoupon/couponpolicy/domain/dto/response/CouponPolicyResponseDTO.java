package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponPolicyResponseDTO {
	private BigDecimal minOrderPrice;
	private BigDecimal salePrice;
	private BigDecimal saleRate;
	private BigDecimal maxSalePrice;
	private String type;
}
