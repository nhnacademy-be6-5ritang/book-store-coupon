package com.nhnacademy.bookstorecoupon.coupontemplate.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;

public interface CustomCouponTemplateRepository  {
	Page<CouponTemplateResponseDTO> findAllTemplatesByUserPaging(Pageable pageable,
		Map<Long, Long> bookIdMap,
		Map<Long, Long> categoryIdMap);
	 Page<CouponTemplateResponseDTO> findAllTemplatesByManagerPaging(Pageable pageable,
		 Map<Long, Long> bookIdMap,
		 Map<Long, Long> categoryIdMap);
}