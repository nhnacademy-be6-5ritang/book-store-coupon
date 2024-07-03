package com.nhnacademy.bookstorecoupon.bookcoupon.repository;

import java.util.Map;

public interface CustomBookCouponRepository {
	Map<Long, Long> fetchBookIdMap();
}
