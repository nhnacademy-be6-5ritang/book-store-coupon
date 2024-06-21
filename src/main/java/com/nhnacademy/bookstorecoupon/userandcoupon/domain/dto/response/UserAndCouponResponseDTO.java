package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;

public record UserAndCouponResponseDTO(
        Long id,
        CouponResponseDTO coupon,
        Long userId,

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime usedDate,
        Boolean isUsed
) {}