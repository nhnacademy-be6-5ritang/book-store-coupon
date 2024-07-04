package com.nhnacademy.bookstorecoupon.categorycoupon.repository.impl;

import java.util.Map;
import java.util.stream.Collectors;

import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.QCategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CustomCategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomCategoryCouponRepositoryImpl implements CustomCategoryCouponRepository {
	private final JPAQueryFactory queryFactory;


	public CustomCategoryCouponRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Map<Long, Long> fetchCategoryIdMap() {
		QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
		QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;

		return queryFactory
			.select(categoryCoupon.couponPolicy.id, categoryCoupon.categoryId)
			.from(categoryCoupon)
			.join(categoryCoupon.couponPolicy, couponPolicy)
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(0, Long.class),
				tuple -> tuple.get(1, Long.class)
			));
	}
}
