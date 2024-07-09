package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response;

import java.time.LocalDate;

public record BirthdayCouponTargetResponse(
	Long userId,
	LocalDate birth){

}