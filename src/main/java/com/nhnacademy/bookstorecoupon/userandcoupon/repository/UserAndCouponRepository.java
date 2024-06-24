package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

public interface UserAndCouponRepository extends JpaRepository<UserAndCoupon, Long> {
	List<UserAndCoupon> findAllByUserEmail(String userEmail);
	UserAndCoupon findByUserEmail(String userEmail);
}