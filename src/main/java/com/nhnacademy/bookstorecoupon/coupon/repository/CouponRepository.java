package com.nhnacademy.bookstorecoupon.coupon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nhnacademy.bookstorecoupon.coupon.domain.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	@Query("SELECT c FROM Coupon c WHERE c.couponPolicy.type IN (:types)")
	Page<Coupon> findByCouponPolicyTypes(@Param("types") List<String> types, Pageable pageable);
}