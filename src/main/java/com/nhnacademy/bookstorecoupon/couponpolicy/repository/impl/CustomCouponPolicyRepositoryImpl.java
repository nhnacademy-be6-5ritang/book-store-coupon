package com.nhnacademy.bookstorecoupon.couponpolicy.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CustomCouponPolicyRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomCouponPolicyRepositoryImpl implements CustomCouponPolicyRepository {
	private final JPAQueryFactory queryFactory;

	public CustomCouponPolicyRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}


	@Override
	public List<CouponPolicyResponseDTO> findAllWithBooksAndCategories(Map<Long, Long> bookIdMap, Map<Long, Long> categoryIdMap) {
		QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;

		return queryFactory
			.selectFrom(couponPolicy)
			.fetch()
			.stream()
			.map(policy -> CouponPolicyResponseDTO.builder()
				.id(policy.getId())
				.minOrderPrice(policy.getMinOrderPrice())
				.salePrice(policy.getSalePrice())
				.saleRate(policy.getSaleRate())
				.maxSalePrice(policy.getMaxSalePrice())
				.type(policy.getType())
				.isUsed(policy.getIsUsed())
				.bookId(bookIdMap.get(policy.getId()))  // Single bookId
				.categoryId(categoryIdMap.get(policy.getId()))  // Single categoryId
				.build()
			).collect(Collectors.toList());
	}
}
