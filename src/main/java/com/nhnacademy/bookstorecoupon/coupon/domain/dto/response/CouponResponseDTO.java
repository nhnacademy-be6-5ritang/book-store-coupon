package com.nhnacademy.bookstorecoupon.coupon.domain.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

public record CouponResponseDTO(
        Long id,
        CouponPolicyResponseDTO couponPolicy,
        LocalDateTime expiredDate,
        LocalDateTime issueDate
) {}