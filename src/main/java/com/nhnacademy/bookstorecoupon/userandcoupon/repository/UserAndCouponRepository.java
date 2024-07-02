package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

public interface UserAndCouponRepository extends JpaRepository<UserAndCoupon, Long>,
	JpaSpecificationExecutor<UserAndCoupon> {

	UserAndCoupon getByUserId(Long userId);
	List<UserAndCoupon> findByCouponPolicy(CouponPolicy couponPolicy);
	Page<UserAndCoupon> findByUserIdAndIsUsedFalse(Long userId, Pageable pageable);
}