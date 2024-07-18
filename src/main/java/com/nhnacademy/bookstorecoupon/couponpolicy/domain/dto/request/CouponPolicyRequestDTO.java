package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request;

import java.math.BigDecimal;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CouponPolicyRequestDTO(@NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal minOrderPrice,
									 @Nullable @Positive @Digits(integer = 8, fraction = 2) BigDecimal salePrice,
									 @Nullable @Positive @Digits(integer = 0, fraction = 2) BigDecimal saleRate,
									 @Nullable @Positive @Digits(integer = 8, fraction = 2) BigDecimal maxSalePrice,
									 @NotBlank String type,  @Nullable Long bookId,   @Nullable String bookTitle, @Nullable Long categoryId,  @Nullable String categoryName) {
}