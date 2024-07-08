package com.nhnacademy.bookstorecoupon.categorycoupon.repository;

import java.util.Map;

import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;

public interface CustomCategoryCouponRepository {
	Map<Long, CategoryCoupon.CategoryInfo> fetchCategoryIdMap();
	}