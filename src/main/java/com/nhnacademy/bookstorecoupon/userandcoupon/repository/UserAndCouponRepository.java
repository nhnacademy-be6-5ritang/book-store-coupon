package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

public interface UserAndCouponRepository extends JpaRepository<UserAndCoupon, Long>, CustomUserAndCouponRepository {

	List<UserAndCoupon> findByExpiredDateBeforeAndIsUsedIsFalse(LocalDateTime currentDateTime);

	List<UserAndCoupon> findByCouponPolicy(CouponPolicy couponPolicy);


}