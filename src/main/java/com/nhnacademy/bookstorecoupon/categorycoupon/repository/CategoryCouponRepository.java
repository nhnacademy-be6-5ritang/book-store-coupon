package com.nhnacademy.bookstorecoupon.categorycoupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;

public interface CategoryCouponRepository extends JpaRepository<CategoryCoupon, Long>, CustomCategoryCouponRepository {
}
