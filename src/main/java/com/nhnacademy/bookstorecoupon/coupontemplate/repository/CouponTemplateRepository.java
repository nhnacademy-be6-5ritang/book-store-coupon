package com.nhnacademy.bookstorecoupon.coupontemplate.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long> {
	@Query("SELECT c FROM CouponTemplate c WHERE c.couponPolicy.type IN (:types)")
	Page<CouponTemplate> findByCouponPolicyTypes(@Param("types") List<String> types, Pageable pageable);
}