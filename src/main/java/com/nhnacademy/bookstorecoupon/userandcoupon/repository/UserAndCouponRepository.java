package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

public interface UserAndCouponRepository extends JpaRepository<UserAndCoupon, Long>,
	JpaSpecificationExecutor<UserAndCoupon> {
	List<UserAndCoupon> findAllByUserEmail(String userEmail);
	Page<UserAndCoupon> findByUserEmail(String userEmail, Pageable pageable);
	UserAndCoupon getByUserEmail(String userEmail);
}