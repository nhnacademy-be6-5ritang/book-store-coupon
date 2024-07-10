package com.nhnacademy.bookstorecoupon.coupontemplate.repository.impl;

import static com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
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
				couponPolicy.id,
				couponPolicy.minOrderPrice,
				couponPolicy.salePrice,
				couponPolicy.saleRate,
				couponPolicy.maxSalePrice,
				couponPolicy.type,
				couponPolicy.isUsed,
				QCouponTemplate.couponTemplate.expiredDate,
				QCouponTemplate.couponTemplate.issueDate
			)
			.from(QCouponTemplate.couponTemplate)
			.join(QCouponTemplate.couponTemplate.couponPolicy, couponPolicy)
			.orderBy(QCouponTemplate.couponTemplate.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 튜플을 DTO로 매핑
		List<CouponTemplateResponseDTO> templates = tuples.stream()
			.map(tuple ->
			{
				BookCoupon.BookInfo bookInfo = bookIdMap.getOrDefault(tuple.get(couponPolicy.id), null);
				CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.getOrDefault(
					tuple.get(couponPolicy.id), null);

			return	new CouponTemplateResponseDTO(
					tuple.get(QCouponTemplate.couponTemplate.id),
					tuple.get(couponPolicy.id),
					tuple.get(couponPolicy.minOrderPrice),
					tuple.get(couponPolicy.salePrice),
					tuple.get(couponPolicy.saleRate),
					tuple.get(couponPolicy.maxSalePrice),
					tuple.get(couponPolicy.type),
					tuple.get(couponPolicy.isUsed),
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
					couponPolicy.id,
					couponPolicy.minOrderPrice,
					couponPolicy.salePrice,
					couponPolicy.saleRate,
					couponPolicy.maxSalePrice,
					couponPolicy.type,
					couponPolicy.isUsed,
					QCouponTemplate.couponTemplate.expiredDate,
					QCouponTemplate.couponTemplate.issueDate
				)
				.from(QCouponTemplate.couponTemplate)
				.join(QCouponTemplate.couponTemplate.couponPolicy, couponPolicy)
				.where(couponPolicy.isUsed.isTrue()
					.and(couponPolicy.type.in(types)))
				.orderBy(QCouponTemplate.couponTemplate.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

			// Map tuples to DTOs
			List<CouponTemplateResponseDTO> templates = tuples.stream()
				.map(tuple ->
				{

					BookCoupon.BookInfo bookInfo = bookIdMap.getOrDefault(tuple.get(couponPolicy.id),
						null);
					CategoryCoupon.CategoryInfo categoryInfo = categoryIdMap.getOrDefault(
						tuple.get(couponPolicy.id), null);

					return new CouponTemplateResponseDTO(
						tuple.get(QCouponTemplate.couponTemplate.id),
						tuple.get(couponPolicy.id),
						tuple.get(couponPolicy.minOrderPrice),
						tuple.get(couponPolicy.salePrice),
						tuple.get(couponPolicy.saleRate),
						tuple.get(couponPolicy.maxSalePrice),
						tuple.get(couponPolicy.type),
						tuple.get(couponPolicy.isUsed),
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
				.join(QCouponTemplate.couponTemplate.couponPolicy, couponPolicy)
				.where(couponPolicy.isUsed.isTrue()
					.and(couponPolicy.type.in(types)))
				.fetchOne()).orElse(0L);

			return new PageImpl<>(templates, pageable, totalCount);
		}




	// @Override
	// public Optional<CouponTemplate> findLatestCouponTemplate(String type) {
	// 	QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
	// 	return Optional.ofNullable(queryFactory
	// 		.selectFrom(couponTemplate)
	// 		.where(couponTemplate.couponPolicy.type.eq(type)
	// 			.and(couponPolicy.isUsed.isTrue()))
	// 		.orderBy(couponTemplate.id.desc())
	// 		.fetchFirst());
	//
	// }
	}