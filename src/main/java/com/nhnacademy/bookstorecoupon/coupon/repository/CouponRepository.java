package com.nhnacademy.bookstorecoupon.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.coupon.domain.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}