package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponPolicyRequestDTO {
	private BigDecimal minOrderPrice;
	private BigDecimal salePrice;
	private BigDecimal saleRate;
	private BigDecimal maxSalePrice;
	private String type;
}
