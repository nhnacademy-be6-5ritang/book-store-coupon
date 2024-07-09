package com.nhnacademy.bookstorecoupon.coupontemplate.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.QCouponTemplate;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CustomCouponTemplateRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomCouponTemplateRepositoryImpl implements CustomCouponTemplateRepository {
	private final JPAQueryFactory queryFactory;

	public CustomCouponTemplateRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<CouponTemplateResponseDTO> findAllTemplatesByManagerPaging(Pageable pageable,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap) {

		// 페이징을 적용하여 데이터 조회
		List<Tuple> tuples = queryFactory
			.select(
				QCouponTemplate.couponTemplate.id,
				QCouponPolicy.couponPolicy.id,
				QCouponPolicy.couponPolicy.minOrderPrice,
				QCouponPolicy.couponPolicy.salePrice,
				QCouponPolicy.couponPolicy.saleRate,
				QCouponPolicy.couponPolicy.maxSalePrice,
				QCouponPolicy.couponPolicy.type,
				QCouponPolicy.couponPolicy.isUsed,
				QCouponTemplate.couponTemplate.expiredDate,
				QCouponTemplate.couponTemplate.issueDate
			)
			.from(QCouponTemplate.couponTemplate)
			.join(QCouponTemplate.couponTemplate.couponPolicy, QCouponPolicy.couponPolicy)
			.orderBy(QCouponTemplate.couponTemplate.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 튜플을 DTO로 매핑
		List<CouponTemplateResponseDTO> templates = tuples.stream()
			.map(tuple ->
			{
				BookCoupon.BookInfo bookInfo = bookIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id), null);
				CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.getOrDefault(
					tuple.get(QCouponPolicy.couponPolicy.id), null);

			return	new CouponTemplateResponseDTO(
					tuple.get(QCouponTemplate.couponTemplate.id),
					tuple.get(QCouponPolicy.couponPolicy.id),
					tuple.get(QCouponPolicy.couponPolicy.minOrderPrice),
					tuple.get(QCouponPolicy.couponPolicy.salePrice),
					tuple.get(QCouponPolicy.couponPolicy.saleRate),
					tuple.get(QCouponPolicy.couponPolicy.maxSalePrice),
					tuple.get(QCouponPolicy.couponPolicy.type),
					tuple.get(QCouponPolicy.couponPolicy.isUsed),
					(bookInfo != null) ? bookInfo.bookId : null, // Check for null before accessing fields
					(bookInfo != null) ? bookInfo.bookTitle : null,
					(categoryInfo != null) ? categoryInfo.categoryId : null,
					(categoryInfo != null) ? categoryInfo.categoryName : null,
					tuple.get(QCouponTemplate.couponTemplate.expiredDate),
					tuple.get(QCouponTemplate.couponTemplate.issueDate)
				);
			})
			.collect(Collectors.toList());

				// 전체 카운트 조회
				long totalCount = Optional.ofNullable(queryFactory
					.select(QCouponTemplate.couponTemplate.id.count())
					.from(QCouponTemplate.couponTemplate)
					.fetchOne()).orElse(0L);

				return new PageImpl<>(templates, pageable, totalCount);
			}

		@Override
		public Page<CouponTemplateResponseDTO> findAllTemplatesByUserPaging (Pageable pageable,
			Map < Long, BookCoupon.BookInfo > bookIdMap,
			Map < Long, CategoryCoupon.CategoryInfo > categoryIdMap){
			List<String> types = List.of("category", "book", "sale");

			// Fetch tuples
			List<Tuple> tuples = queryFactory
				.select(
					QCouponTemplate.couponTemplate.id,
					QCouponPolicy.couponPolicy.id,
					QCouponPolicy.couponPolicy.minOrderPrice,
					QCouponPolicy.couponPolicy.salePrice,
					QCouponPolicy.couponPolicy.saleRate,
					QCouponPolicy.couponPolicy.maxSalePrice,
					QCouponPolicy.couponPolicy.type,
					QCouponPolicy.couponPolicy.isUsed,
					QCouponTemplate.couponTemplate.expiredDate,
					QCouponTemplate.couponTemplate.issueDate
				)
				.from(QCouponTemplate.couponTemplate)
				.join(QCouponTemplate.couponTemplate.couponPolicy, QCouponPolicy.couponPolicy)
				.where(QCouponPolicy.couponPolicy.isUsed.isTrue()
					.and(QCouponPolicy.couponPolicy.type.in(types)))
				.orderBy(QCouponTemplate.couponTemplate.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

			// Map tuples to DTOs
			List<CouponTemplateResponseDTO> templates = tuples.stream()
				.map(tuple ->
				{

					BookCoupon.BookInfo bookInfo = bookIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id),
						null);
					CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.getOrDefault(
						tuple.get(QCouponPolicy.couponPolicy.id), null);

					return new CouponTemplateResponseDTO(
						tuple.get(QCouponTemplate.couponTemplate.id),
						tuple.get(QCouponPolicy.couponPolicy.id),
						tuple.get(QCouponPolicy.couponPolicy.minOrderPrice),
						tuple.get(QCouponPolicy.couponPolicy.salePrice),
						tuple.get(QCouponPolicy.couponPolicy.saleRate),
						tuple.get(QCouponPolicy.couponPolicy.maxSalePrice),
						tuple.get(QCouponPolicy.couponPolicy.type),
						tuple.get(QCouponPolicy.couponPolicy.isUsed),
						(bookInfo != null) ? bookInfo.bookId : null, // Check for null before accessing fields
						(bookInfo != null) ? bookInfo.bookTitle : null,
						(categoryInfo != null) ? categoryInfo.categoryId : null,
						(categoryInfo != null) ? categoryInfo.categoryName : null,
						tuple.get(QCouponTemplate.couponTemplate.expiredDate),
						tuple.get(QCouponTemplate.couponTemplate.issueDate)
					);
				})
				.collect(Collectors.toList());

			// Count total
			long totalCount = Optional.ofNullable(queryFactory
				.select(QCouponTemplate.couponTemplate.id.count())
				.from(QCouponTemplate.couponTemplate)
				.join(QCouponTemplate.couponTemplate.couponPolicy, QCouponPolicy.couponPolicy)
				.where(QCouponPolicy.couponPolicy.isUsed.isTrue()
					.and(QCouponPolicy.couponPolicy.type.in(types)))
				.fetchOne()).orElse(0L);

			return new PageImpl<>(templates, pageable, totalCount);
		}


	//
	// @Override
	// public Optional<CouponTemplate> findLatestBirthdayCouponTemplate() {
	// 	QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
	// 	return Optional.ofNullable(queryFactory
	// 		.selectFrom(couponTemplate)
	// 		.where(couponTemplate.couponPolicy.type.eq("birthday"))
	// 		.orderBy(couponTemplate.couponPolicy.id.desc())
	// 		.fetchFirst());
	// }
	}