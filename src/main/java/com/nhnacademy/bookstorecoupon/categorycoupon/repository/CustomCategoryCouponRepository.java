package com.nhnacademy.bookstorecoupon.categorycoupon.repository;

import java.util.Map;

public interface CustomCategoryCouponRepository {
	Map<Long, Long> fetchCategoryIdMap();
	}