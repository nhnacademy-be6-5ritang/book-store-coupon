package com.nhnacademy.bookstorecoupon.categorycoupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

public interface CategoryCouponRepository extends JpaRepository<CategoryCoupon, Long> {
	CategoryCoupon findByCouponPolicy(CouponPolicy couponPolicy);

}
