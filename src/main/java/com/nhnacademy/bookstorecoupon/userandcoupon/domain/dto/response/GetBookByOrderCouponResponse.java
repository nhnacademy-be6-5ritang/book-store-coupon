package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record GetBookByOrderCouponResponse(
	Long bookId,
	BigDecimal bookPrice,
	List<Long> categoryId
) {

}
