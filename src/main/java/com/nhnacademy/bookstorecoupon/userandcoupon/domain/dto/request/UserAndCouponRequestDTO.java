package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request;

import java.time.LocalDateTime;

public record UserAndCouponRequestDTO(
        Long couponId,
        Long userId,
        LocalDateTime usedDate,
        Boolean isUsed
) {}