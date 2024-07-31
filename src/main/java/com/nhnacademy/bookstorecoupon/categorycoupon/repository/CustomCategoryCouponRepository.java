package com.nhnacademy.bookstorecoupon.categorycoupon.repository;

import java.util.Map;

import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;



/**
 * @author 이기훈
 * 카테고리 쿠폰 테이블에 있는 정보를 querydsl을 통해 가져오는 custom repository
 */
public interface CustomCategoryCouponRepository {

	/**
	 * 카테고리 쿠폰 테이블에 있는 정보를 querydsl을 통해 가져오는 custom repository
	 * @return 쿠폰정책 아이디, 카테고리 아이디, 카테고리 이름
	 */
	Map<Long, CategoryCoupon.CategoryInfo> fetchCategoryIdMap();
	}