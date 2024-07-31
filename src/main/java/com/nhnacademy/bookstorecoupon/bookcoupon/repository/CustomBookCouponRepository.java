package com.nhnacademy.bookstorecoupon.bookcoupon.repository;

import java.util.Map;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;


/**
 * @author 이기훈
 * 도서 쿠폰 테이블에 있는 정보를 querydsl을 통해 가져오는 custom repository
 */

public interface CustomBookCouponRepository {


	/**
	 * 도서 쿠폰 테이블에 있는 정보를 querydsl을 통해 가져오는 custom repository
	 * @return 쿠폰정책 아이디, 북 아이디, 도서제목
	 */
	Map<Long, BookCoupon.BookInfo> fetchBookIdMap();
}
