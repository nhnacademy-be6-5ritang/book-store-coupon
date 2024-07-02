package com.nhnacademy.bookstorecoupon.bookcoupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

public interface BookCouponRepository extends JpaRepository<BookCoupon, Long> {

	BookCoupon findByCouponPolicy(CouponPolicy couponPolicy);
}


