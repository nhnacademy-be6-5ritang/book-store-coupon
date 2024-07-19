package com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CouponPolicyRequestDTO(
	@NotNull @Positive @Digits(integer = 8, fraction = 2)
	@Schema(description = "최소 주문 금액", example = "1000.00")
	BigDecimal minOrderPrice,

	@Nullable @Positive @Digits(integer = 8, fraction = 2)
	@Schema(description = "할인 금액", example = "500.00")
	BigDecimal salePrice,

	@Nullable @Positive @Digits(integer = 0, fraction = 2)
	@Schema(description = "할인 비율", example = "0.15")
	BigDecimal saleRate,

	@Nullable @Positive @Digits(integer = 8, fraction = 2)
	@Schema(description = "최대 할인 금액", example = "1500.00")
	BigDecimal maxSalePrice,

	@NotBlank
	@Schema(description = "쿠폰 타입", example = "WELCOME")
	String type,

	@Nullable
	@Schema(description = "책 ID", example = "123456")
	Long bookId,

	@Nullable
	@Schema(description = "책 제목", example = "Effective Java")
	String bookTitle,

	@Nullable
	@Schema(description = "카테고리 ID", example = "78910")
	Long categoryId,

	@Nullable
	@Schema(description = "카테고리 이름", example = "Fiction")
	String categoryName
) {}


//
// public record CouponPolicyRequestDTO(@NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal minOrderPrice,
// 									 @Nullable @Positive @Digits(integer = 8, fraction = 2) BigDecimal salePrice,
// 									 @Nullable @Positive @Digits(integer = 0, fraction = 2) BigDecimal saleRate,
// 									 @Nullable @Positive @Digits(integer = 8, fraction = 2) BigDecimal maxSalePrice,
// 									 @NotBlank String type, @Nullable Long bookId, @Nullable String bookTitle, @Nullable Long categoryId, @Nullable String categoryName) {
// }
