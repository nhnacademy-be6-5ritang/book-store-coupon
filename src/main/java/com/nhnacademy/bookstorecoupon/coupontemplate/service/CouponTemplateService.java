package com.nhnacademy.bookstorecoupon.coupontemplate.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;

public interface CouponTemplateService {

    void createCouponTemplate(CouponTemplateRequestDTO requestDTO);
    List<CouponTemplateResponseDTO> getAllCouponTemplates();
    Page<CouponTemplateResponseDTO> getAllCouponTemplatePaging(Pageable pageable);
    CouponTemplateResponseDTO getCouponTemplateById(Long id);
}