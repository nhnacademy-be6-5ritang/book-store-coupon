package com.nhnacademy.bookstorecoupon.coupontemplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long>, CustomCouponTemplateRepository{

}