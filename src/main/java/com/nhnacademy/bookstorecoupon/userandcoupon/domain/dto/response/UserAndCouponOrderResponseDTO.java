package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response;

import java.math.BigDecimal;

public record UserAndCouponOrderResponseDTO(
	Long id,


	BigDecimal minOrderPrice,
	BigDecimal salePrice,
	BigDecimal saleRate,
	BigDecimal maxSalePrice,
	String type

) {


}