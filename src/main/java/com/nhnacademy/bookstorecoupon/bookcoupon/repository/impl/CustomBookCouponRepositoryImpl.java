package com.nhnacademy.bookstorecoupon.bookcoupon.repository.impl;

import java.util.Map;
import java.util.stream.Collectors;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.QBookCoupon;
import com.nhnacademy.bookstorecoupon.bookcoupon.repository.CustomBookCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBookCouponRepositoryImpl implements CustomBookCouponRepository {
	private final JPAQueryFactory queryFactory;
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public Map<Long, BookCoupon.BookInfo> fetchBookIdMap() {
		QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
		QBookCoupon bookCoupon = QBookCoupon.bookCoupon;

		return queryFactory
			.select(bookCoupon.couponPolicy.id, bookCoupon.bookId, bookCoupon.bookTitle)
			.from(bookCoupon)
			.join(bookCoupon.couponPolicy, couponPolicy)
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(0, Long.class),
				tuple -> new BookCoupon.BookInfo(tuple.get(1, Long.class), tuple.get(2, String.class))
			));
	}

}