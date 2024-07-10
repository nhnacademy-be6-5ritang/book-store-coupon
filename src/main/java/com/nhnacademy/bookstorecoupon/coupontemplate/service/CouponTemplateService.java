package com.nhnacademy.bookstorecoupon.coupontemplate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;

public interface CouponTemplateService {

	void createCouponTemplate(CouponTemplateRequestDTO requestDTO);

	Page<CouponTemplateResponseDTO> getAllCouponTemplatesByManagerPaging(Pageable pageable);

	Page<CouponTemplateResponseDTO> getAllCouponTemplatesByUserPaging(Pageable pageable);

	void issueBirthdayTemplate();

	void issueWelcomeTemplate();

	void issueTemplateByType(String type);
}