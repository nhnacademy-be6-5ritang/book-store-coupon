package com.nhnacademy.bookstorecoupon.couponpolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {
}