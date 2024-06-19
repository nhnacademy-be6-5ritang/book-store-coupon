package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersAndCouponsRepository extends JpaRepository<UserAndCoupon, Long> {
	List<UserAndCoupon> findByUserId(Long userId);
	List<UserAndCoupon> findByUserIdAndIsUsed(Long userId, Boolean isUsed);
	List<UsersAndCoupons> findByUserIdAndIsUsedFalse(Long userId);
	List<UsersAndCoupons> findByUserIdAndIsUsedTrue(Long userId);
	Optional<UsersAndCoupons> findByUserIdAndCouponId(Long userId, Long couponId);
}

