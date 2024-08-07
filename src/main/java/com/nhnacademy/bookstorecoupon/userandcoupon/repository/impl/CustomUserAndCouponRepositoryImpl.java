package com.nhnacademy.bookstorecoupon.userandcoupon.repository.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.QUserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.CustomUserAndCouponRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class CustomUserAndCouponRepositoryImpl implements CustomUserAndCouponRepository {
	private final JPAQueryFactory queryFactory;

	public CustomUserAndCouponRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public Page<UserAndCouponResponseDTO> findAllByUserPaging(Pageable pageable, Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap) {
		// Fetch tuples
		List<Tuple> tuples = queryFactory
			.select(
				QUserAndCoupon.userAndCoupon.id,
				QUserAndCoupon.userAndCoupon.userId,
				QUserAndCoupon.userAndCoupon.usedDate,
				QUserAndCoupon.userAndCoupon.isUsed,
				QUserAndCoupon.userAndCoupon.expiredDate,
				QUserAndCoupon.userAndCoupon.issueDate,
				QCouponPolicy.couponPolicy.id,
				QCouponPolicy.couponPolicy.minOrderPrice,
				QCouponPolicy.couponPolicy.salePrice,
				QCouponPolicy.couponPolicy.saleRate,
				QCouponPolicy.couponPolicy.maxSalePrice,
				QCouponPolicy.couponPolicy.type,
				QCouponPolicy.couponPolicy.isUsed
			)
			.from(QUserAndCoupon.userAndCoupon)
			.join(QUserAndCoupon.userAndCoupon.couponPolicy, QCouponPolicy.couponPolicy)
			.where(
				QUserAndCoupon.userAndCoupon.isUsed.eq(false)
					.and(QUserAndCoupon.userAndCoupon.userId.eq(userId))
					.and(QUserAndCoupon.userAndCoupon.expiredDate.after(LocalDateTime.now()))// 필터링 조건 추가
			)
			.orderBy(QUserAndCoupon.userAndCoupon.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Map tuples to DTOs
		List<UserAndCouponResponseDTO> results = tuples.stream()
			.map(tuple -> {
				BookCoupon.BookInfo bookInfo = bookIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id), null);
				CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.getOrDefault(
					tuple.get(QCouponPolicy.couponPolicy.id), null);

				return new UserAndCouponResponseDTO(
					tuple.get(QUserAndCoupon.userAndCoupon.id),
					tuple.get(QUserAndCoupon.userAndCoupon.userId),
					tuple.get(QUserAndCoupon.userAndCoupon.usedDate),
					tuple.get(QUserAndCoupon.userAndCoupon.isUsed),
					tuple.get(QUserAndCoupon.userAndCoupon.expiredDate),
					tuple.get(QUserAndCoupon.userAndCoupon.issueDate),
					tuple.get(QCouponPolicy.couponPolicy.minOrderPrice),
					tuple.get(QCouponPolicy.couponPolicy.salePrice),
					tuple.get(QCouponPolicy.couponPolicy.saleRate),
					tuple.get(QCouponPolicy.couponPolicy.maxSalePrice),
					tuple.get(QCouponPolicy.couponPolicy.type),
					tuple.get(QCouponPolicy.couponPolicy.isUsed),
					(bookInfo != null) ? bookInfo.bookId : null, // Check for null before accessing fields
					(bookInfo != null) ? bookInfo.bookTitle : null,
					(categoryInfo != null) ? categoryInfo.categoryId : null,
					(categoryInfo != null) ? categoryInfo.categoryName : null
				);
			})
			.collect(Collectors.toList());

		long totalCount = Optional.ofNullable(queryFactory
			.select(QUserAndCoupon.userAndCoupon.id.count())
			.from(QUserAndCoupon.userAndCoupon)
			.where(QUserAndCoupon.userAndCoupon.isUsed.eq(false)
				.and(QUserAndCoupon.userAndCoupon.userId.eq(userId))
				.and(QUserAndCoupon.userAndCoupon.expiredDate.after(LocalDateTime.now())))
			.fetchOne()).orElse(0L);

		return new PageImpl<>(results, pageable, totalCount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<UserAndCouponResponseDTO> findAllByManagerPaging(Pageable pageable, String type, Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap) {
		// Define where clause
		BooleanExpression whereClause = QUserAndCoupon.userAndCoupon.isNotNull();
		if (type != null && !type.isEmpty()) {
			whereClause = whereClause.and(QCouponPolicy.couponPolicy.type.eq(type));
		}
		if (userId != null) {
			whereClause = whereClause.and(QUserAndCoupon.userAndCoupon.userId.eq(userId));
		}

		// Fetch tuples
		List<Tuple> tuples = queryFactory
			.select(
				QUserAndCoupon.userAndCoupon.id,
				QUserAndCoupon.userAndCoupon.userId,
				QUserAndCoupon.userAndCoupon.usedDate,
				QUserAndCoupon.userAndCoupon.isUsed,
				QUserAndCoupon.userAndCoupon.expiredDate,
				QUserAndCoupon.userAndCoupon.issueDate,
				QCouponPolicy.couponPolicy.id,
				QCouponPolicy.couponPolicy.minOrderPrice,
				QCouponPolicy.couponPolicy.salePrice,
				QCouponPolicy.couponPolicy.saleRate,
				QCouponPolicy.couponPolicy.maxSalePrice,
				QCouponPolicy.couponPolicy.type,
				QCouponPolicy.couponPolicy.isUsed
			)
			.from(QUserAndCoupon.userAndCoupon)
			.join(QUserAndCoupon.userAndCoupon.couponPolicy, QCouponPolicy.couponPolicy)
			.where(whereClause)
			.orderBy(QUserAndCoupon.userAndCoupon.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Map tuples to DTOs
		List<UserAndCouponResponseDTO> results = tuples.stream()
			.map(tuple -> {
				BookCoupon.BookInfo bookInfo = bookIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id), null);
				CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.getOrDefault(
					tuple.get(QCouponPolicy.couponPolicy.id), null);

				return new UserAndCouponResponseDTO(
					tuple.get(QUserAndCoupon.userAndCoupon.id),
					tuple.get(QUserAndCoupon.userAndCoupon.userId),
					tuple.get(QUserAndCoupon.userAndCoupon.usedDate),
					tuple.get(QUserAndCoupon.userAndCoupon.isUsed),
					tuple.get(QUserAndCoupon.userAndCoupon.expiredDate),
					tuple.get(QUserAndCoupon.userAndCoupon.issueDate),
					tuple.get(QCouponPolicy.couponPolicy.minOrderPrice),
					tuple.get(QCouponPolicy.couponPolicy.salePrice),
					tuple.get(QCouponPolicy.couponPolicy.saleRate),
					tuple.get(QCouponPolicy.couponPolicy.maxSalePrice),
					tuple.get(QCouponPolicy.couponPolicy.type),
					tuple.get(QCouponPolicy.couponPolicy.isUsed),
					(bookInfo != null) ? bookInfo.bookId : null, // Check for null before accessing fields
					(bookInfo != null) ? bookInfo.bookTitle : null,
					(categoryInfo != null) ? categoryInfo.categoryId : null,
					(categoryInfo != null) ? categoryInfo.categoryName : null
				);
			})
			.collect(Collectors.toList());

		// Count total
		long totalCount = Optional.ofNullable(queryFactory
			.select(QUserAndCoupon.userAndCoupon.id.count())
			.from(QUserAndCoupon.userAndCoupon)
			.join(QUserAndCoupon.userAndCoupon.couponPolicy, QCouponPolicy.couponPolicy)
			.where(whereClause)
			.fetchOne()).orElse(0L);

		return new PageImpl<>(results, pageable, totalCount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserAndCouponResponseDTO> findCouponByOrder(
		Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap,
		List<Long> bookIds,
		List<Long> categoryIds,
		BigDecimal bookPrice
	) {
		// Define the base where clause
		BooleanExpression whereClause = QUserAndCoupon.userAndCoupon.isUsed.eq(false)
			.and(QUserAndCoupon.userAndCoupon.userId.eq(userId))
			.and(QCouponPolicy.couponPolicy.isUsed.eq(true))
			.and(QCouponPolicy.couponPolicy.minOrderPrice.loe(bookPrice))
			.and(QUserAndCoupon.userAndCoupon.expiredDate.after(LocalDateTime.now()));

		// Fetch tuples
		List<Tuple> tuples = queryFactory
			.select(
				QUserAndCoupon.userAndCoupon.id,
				QUserAndCoupon.userAndCoupon.userId,
				QUserAndCoupon.userAndCoupon.usedDate,
				QUserAndCoupon.userAndCoupon.isUsed,
				QUserAndCoupon.userAndCoupon.expiredDate,
				QUserAndCoupon.userAndCoupon.issueDate,
				QCouponPolicy.couponPolicy.id,
				QCouponPolicy.couponPolicy.minOrderPrice,
				QCouponPolicy.couponPolicy.salePrice,
				QCouponPolicy.couponPolicy.saleRate,
				QCouponPolicy.couponPolicy.maxSalePrice,
				QCouponPolicy.couponPolicy.type,
				QCouponPolicy.couponPolicy.isUsed
			)
			.from(QUserAndCoupon.userAndCoupon)
			.join(QUserAndCoupon.userAndCoupon.couponPolicy, QCouponPolicy.couponPolicy)
			.where(whereClause)
			.orderBy(QUserAndCoupon.userAndCoupon.id.desc())
			.fetch();

		// Map tuples to DTOs
		List<UserAndCouponResponseDTO> results = tuples.stream()
			.map(tuple -> {
				Long couponPolicyId = tuple.get(QCouponPolicy.couponPolicy.id);
				String couponType = tuple.get(QCouponPolicy.couponPolicy.type);

				BookCoupon.BookInfo bookInfo = bookIdMap.get(couponPolicyId);
				CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.get(couponPolicyId);

				Long bookId = (bookInfo != null) ? bookInfo.bookId : null;
				Long categoryId = (categoryInfo != null) ? categoryInfo.categoryId : null;

				// 필터링 조건 추가
				boolean matchesBookCoupon = couponType.equals("book") && bookId != null && bookIds.contains(bookId);
				boolean matchesCategoryCoupon =
					couponType.equals("category") && categoryId != null && categoryIds.contains(categoryId);
				boolean matchesOtherCoupons =
					couponType.equals("welcome") || couponType.equals("birthday") || couponType.equals("sale");

				if (matchesBookCoupon || matchesCategoryCoupon || matchesOtherCoupons) {
					return new UserAndCouponResponseDTO(
						tuple.get(QUserAndCoupon.userAndCoupon.id),
						tuple.get(QUserAndCoupon.userAndCoupon.userId),
						tuple.get(QUserAndCoupon.userAndCoupon.usedDate),
						tuple.get(QUserAndCoupon.userAndCoupon.isUsed),
						tuple.get(QUserAndCoupon.userAndCoupon.expiredDate),
						tuple.get(QUserAndCoupon.userAndCoupon.issueDate),
						tuple.get(QCouponPolicy.couponPolicy.minOrderPrice),
						tuple.get(QCouponPolicy.couponPolicy.salePrice),
						tuple.get(QCouponPolicy.couponPolicy.saleRate),
						tuple.get(QCouponPolicy.couponPolicy.maxSalePrice),
						tuple.get(QCouponPolicy.couponPolicy.type),
						tuple.get(QCouponPolicy.couponPolicy.isUsed),
						(bookId != null) ? bookInfo.bookId : null,
						(bookInfo != null) ? bookInfo.bookTitle : null,
						(categoryId != null) ? categoryInfo.categoryId : null,
						(categoryInfo != null) ? categoryInfo.categoryName : null
					);
				} else {
					return null; // 필터 조건에 맞지 않으면 null 반환
				}
			})
			.filter(Objects::nonNull) // null인 요소 제거
			.collect(Collectors.toList());

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserAndCouponResponseDTO> findCouponByCartOrder(
		Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap,
		List<GetBookByOrderCouponResponse> bookDetails
	) {
		// Extract bookIds, categoryIds, and bookPrice from bookDetails
		List<Long> bookIds = bookDetails.stream()
			.map(GetBookByOrderCouponResponse::bookId)
			.toList();

		List<Long> categoryIds = bookDetails.stream()
			.flatMap(response -> response.categoryId().stream())
			.toList();

		BigDecimal bookPrice = bookDetails.stream()
			.map(GetBookByOrderCouponResponse::bookPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		// Define the base where clause
		BooleanExpression whereClause = QUserAndCoupon.userAndCoupon.isUsed.eq(false)
			.and(QUserAndCoupon.userAndCoupon.userId.eq(userId))
			.and(QCouponPolicy.couponPolicy.isUsed.eq(true))
			.and(QCouponPolicy.couponPolicy.minOrderPrice.loe(bookPrice))
			.and(QUserAndCoupon.userAndCoupon.expiredDate.after(LocalDateTime.now()));

		// Fetch tuples
		List<Tuple> tuples = queryFactory
			.select(
				QUserAndCoupon.userAndCoupon.id,
				QUserAndCoupon.userAndCoupon.userId,
				QUserAndCoupon.userAndCoupon.usedDate,
				QUserAndCoupon.userAndCoupon.isUsed,
				QUserAndCoupon.userAndCoupon.expiredDate,
				QUserAndCoupon.userAndCoupon.issueDate,
				QCouponPolicy.couponPolicy.id,
				QCouponPolicy.couponPolicy.minOrderPrice,
				QCouponPolicy.couponPolicy.salePrice,
				QCouponPolicy.couponPolicy.saleRate,
				QCouponPolicy.couponPolicy.maxSalePrice,
				QCouponPolicy.couponPolicy.type,
				QCouponPolicy.couponPolicy.isUsed
			)
			.from(QUserAndCoupon.userAndCoupon)
			.join(QUserAndCoupon.userAndCoupon.couponPolicy, QCouponPolicy.couponPolicy)
			.where(whereClause)
			.orderBy(QUserAndCoupon.userAndCoupon.id.desc())
			.fetch();

		// Map tuples to DTOs and filter duplicates
		List<UserAndCouponResponseDTO> results = tuples.stream()
			.map(tuple -> {
				Long couponPolicyId = tuple.get(QCouponPolicy.couponPolicy.id);
				String couponType = tuple.get(QCouponPolicy.couponPolicy.type);

				BookCoupon.BookInfo bookInfo = bookIdMap.get(couponPolicyId);
				CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.get(couponPolicyId);

				Long bookId = (bookInfo != null) ? bookInfo.bookId : null;
				Long categoryId = (categoryInfo != null) ? categoryInfo.categoryId : null;

				// 필터링 조건 추가
				boolean matchesBookCoupon = couponType.equals("book") && bookId != null && bookIds.contains(bookId);
				boolean matchesCategoryCoupon =
					couponType.equals("category") && categoryId != null && categoryIds.contains(categoryId);
				boolean matchesOtherCoupons =
					couponType.equals("welcome") || couponType.equals("birthday") || couponType.equals("sale");

				if ((matchesBookCoupon || matchesCategoryCoupon || matchesOtherCoupons)) {

					return new UserAndCouponResponseDTO(
						tuple.get(QUserAndCoupon.userAndCoupon.id),
						tuple.get(QUserAndCoupon.userAndCoupon.userId),
						tuple.get(QUserAndCoupon.userAndCoupon.usedDate),
						tuple.get(QUserAndCoupon.userAndCoupon.isUsed),
						tuple.get(QUserAndCoupon.userAndCoupon.expiredDate),
						tuple.get(QUserAndCoupon.userAndCoupon.issueDate),
						tuple.get(QCouponPolicy.couponPolicy.minOrderPrice),
						tuple.get(QCouponPolicy.couponPolicy.salePrice),
						tuple.get(QCouponPolicy.couponPolicy.saleRate),
						tuple.get(QCouponPolicy.couponPolicy.maxSalePrice),
						tuple.get(QCouponPolicy.couponPolicy.type),
						tuple.get(QCouponPolicy.couponPolicy.isUsed),
						(bookId != null) ? bookInfo.bookId : null,
						(bookInfo != null) ? bookInfo.bookTitle : null,
						(categoryId != null) ? categoryInfo.categoryId : null,
						(categoryInfo != null) ? categoryInfo.categoryName : null
					);
				} else {
					return null; // 필터 조건에 맞지 않거나 중복된 쿠폰은 null 반환
				}
			})
			.filter(Objects::nonNull) // null인 요소 제거
			.collect(Collectors.toList());

		return results;
	}

}
