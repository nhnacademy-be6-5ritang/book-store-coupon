package com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;

public record CouponTemplateResponseDTO(
        Long id,
        CouponPolicyResponseDTO couponPolicy,


		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime expiredDate,


		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime issueDate
) {

	public static CouponTemplateResponseDTO fromCouponTemplate(CouponTemplate couponTemplate) {
		return new CouponTemplateResponseDTO(
			couponTemplate.getId(),
			CouponPolicyResponseDTO.fromCouponPolicy(couponTemplate.getCouponPolicy()),
			couponTemplate.getExpiredDate(),
			couponTemplate.getIssueDate()

		);
	}


}