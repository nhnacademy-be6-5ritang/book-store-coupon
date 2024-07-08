package com.nhnacademy.bookstorecoupon.couponpolicy.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CustomCouponPolicyRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomCouponPolicyRepositoryImpl implements CustomCouponPolicyRepository {
	private final JPAQueryFactory queryFactory;

	public CustomCouponPolicyRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<CouponPolicyResponseDTO> findAllWithBooksAndCategories(Pageable pageable, Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap) {
		QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;





		// Fetch coupon policies with pagination and ordering
		List<CouponPolicyResponseDTO> couponPolicies = queryFactory
			.selectFrom(couponPolicy)
			.orderBy(couponPolicy.id.desc()) // Add orderBy for descending policy id
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
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
				.bookId(bookIdMap.get(policy.getId()) != null ? bookIdMap.get(policy.getId()).bookId : null)
				.bookTitle(bookIdMap.get(policy.getId()) != null ? bookIdMap.get(policy.getId()).bookTitle : null)
				.categoryId(categoryIdMap.get(policy.getId()) != null ? categoryIdMap.get(policy.getId()).categoryId : null)
				.categoryName(categoryIdMap.get(policy.getId()) != null ? categoryIdMap.get(policy.getId()).categoryName : null)
				.build()
			).collect(Collectors.toList());

		// Count total number of coupon policies
		long totalCount = Optional.ofNullable(queryFactory
				.select(couponPolicy.count())
				.from(couponPolicy)
				.fetchFirst())
			.orElse(0L);

		return new PageImpl<>(couponPolicies, pageable, totalCount);
	}

	@Override
	public Optional<CouponPolicy> findLatestCouponPolicyByType(String type) {
		QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
		return Optional.ofNullable(queryFactory
			.selectFrom(couponPolicy)
			.where(couponPolicy.type.eq(type))
			.orderBy(couponPolicy.id.desc())
			.fetchFirst());
	}
}