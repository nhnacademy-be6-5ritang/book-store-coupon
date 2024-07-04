
package com.nhnacademy.bookstorecoupon.userandcoupon.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.QCouponPolicy;
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


    @Override
    public Page<UserAndCouponResponseDTO> findAllByUserPaging(Pageable pageable, Long userId, Map<Long, Long> bookIdMap, Map<Long, Long> categoryIdMap) {
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
                    .and(QUserAndCoupon.userAndCoupon.userId.eq(userId)) // 필터링 조건 추가
            )
            .orderBy(QUserAndCoupon.userAndCoupon.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // Map tuples to DTOs
        List<UserAndCouponResponseDTO> results = tuples.stream()
            .map(tuple -> new UserAndCouponResponseDTO(
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
                bookIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id), null), // Fetch bookId from map
                categoryIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id), null) // Fetch categoryId from map
            ))
            .collect(Collectors.toList());


        long totalCount = Optional.ofNullable(queryFactory
            .select(QUserAndCoupon.userAndCoupon.id.count())
            .from(QUserAndCoupon.userAndCoupon)
            .where(  QUserAndCoupon.userAndCoupon.isUsed.eq(false)
                .and(QUserAndCoupon.userAndCoupon.userId.eq(userId)))
            .fetchOne()).orElse(0L);


        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<UserAndCouponResponseDTO> findAllByManagerPaging(Pageable pageable, String type, Long userId, Map<Long, Long> bookIdMap, Map<Long, Long> categoryIdMap) {
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
            .map(tuple -> new UserAndCouponResponseDTO(
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
                bookIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id), null), // Fetch bookId from map
                categoryIdMap.getOrDefault(tuple.get(QCouponPolicy.couponPolicy.id), null) // Fetch categoryId from map
            ))
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


}
