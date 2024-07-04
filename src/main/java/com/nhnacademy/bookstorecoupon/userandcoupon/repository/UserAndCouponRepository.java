package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

public interface UserAndCouponRepository extends JpaRepository<UserAndCoupon, Long>, CustomUserAndCouponRepository
	 {

	UserAndCoupon getByUserId(Long userId);
	List<UserAndCoupon> findByCouponPolicy(CouponPolicy couponPolicy);
}