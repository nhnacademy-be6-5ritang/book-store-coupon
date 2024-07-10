package com.nhnacademy.bookstorecoupon.coupontemplate.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;

public interface CustomCouponTemplateRepository  {
	Page<CouponTemplateResponseDTO> findAllTemplatesByUserPaging(Pageable pageable,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);
	 Page<CouponTemplateResponseDTO> findAllTemplatesByManagerPaging(Pageable pageable,
		 Map<Long, BookCoupon.BookInfo> bookIdMap,
		 Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);


	// Optional<CouponTemplate> findLatestCouponTemplate(String type);
}