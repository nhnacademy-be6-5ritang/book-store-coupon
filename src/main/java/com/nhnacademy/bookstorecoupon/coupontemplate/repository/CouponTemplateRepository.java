package com.nhnacademy.bookstorecoupon.coupontemplate.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long> {

	Page<CouponTemplate> findByCouponPolicyIsUsedTrueAndCouponPolicyTypeIn(List<String> types, Pageable pageable);
}