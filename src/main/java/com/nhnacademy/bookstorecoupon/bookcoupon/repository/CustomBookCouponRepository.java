package com.nhnacademy.bookstorecoupon.bookcoupon.repository;

import java.util.Map;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;

public interface CustomBookCouponRepository {
	Map<Long, BookCoupon.BookInfo> fetchBookIdMap();
}
