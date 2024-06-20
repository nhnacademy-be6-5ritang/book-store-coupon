package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;

public record UserAndCouponResponseDTO(
        Long id,
        CouponResponseDTO coupon,
        Long userId,
        LocalDateTime usedDate,
        Boolean isUsed
) {}