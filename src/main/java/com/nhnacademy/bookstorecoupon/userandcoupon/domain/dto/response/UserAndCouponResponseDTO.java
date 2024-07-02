package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

public record UserAndCouponResponseDTO(
	Long id,
	CouponPolicyResponseDTO couponPolicyResponseDTO,
	Long userId,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime usedDate,

	Boolean isUsed,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime expiredDate,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime issueDate

) {

	public static UserAndCouponResponseDTO fromUserAndCoupon(UserAndCoupon savedUserAndCoupon) {
		return new UserAndCouponResponseDTO(
			savedUserAndCoupon.getId(),
			CouponPolicyResponseDTO.fromCouponPolicy(savedUserAndCoupon.getCouponPolicy()),
			savedUserAndCoupon.getUserId(),
			savedUserAndCoupon.getUsedDate(),
			savedUserAndCoupon.getIsUsed(),
			savedUserAndCoupon.getExpiredDate(),
			savedUserAndCoupon.getIssueDate()
		);
	}
}