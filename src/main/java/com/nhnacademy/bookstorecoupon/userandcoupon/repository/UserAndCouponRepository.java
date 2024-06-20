package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

public interface UserAndCouponRepository extends JpaRepository<UserAndCoupon, Long> {
}