package com.nhnacademy.bookstorecoupon.coupon.domain.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponDTO {
	private Long policyId;
	private LocalDateTime issueDate;
	private LocalDateTime expiredDate;
	private String policyType;
	private String description;
}
