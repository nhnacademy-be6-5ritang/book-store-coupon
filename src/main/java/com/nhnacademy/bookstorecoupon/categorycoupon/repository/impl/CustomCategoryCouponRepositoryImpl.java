package com.nhnacademy.bookstorecoupon.categorycoupon.repository.impl;

import java.util.Map;
import java.util.stream.Collectors;

import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.QCategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CustomCategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCategoryCouponRepositoryImpl implements CustomCategoryCouponRepository {
	private final JPAQueryFactory queryFactory;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public Map<Long, CategoryCoupon.CategoryInfo> fetchCategoryIdMap() {
		QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
		QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;

		return queryFactory
			.select(categoryCoupon.couponPolicy.id, categoryCoupon.categoryId, categoryCoupon.categoryName)
			.from(categoryCoupon)
			.join(categoryCoupon.couponPolicy, couponPolicy)
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(0, Long.class),
				tuple -> new CategoryCoupon.CategoryInfo(tuple.get(1, Long.class), tuple.get(2, String.class))
			));
	}

}