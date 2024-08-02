package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

/**
 * @author 이기훈
 * 엔티티에 대한 JPA 리포지토리입니다.
 */
public interface UserAndCouponRepository extends JpaRepository<UserAndCoupon, Long>, CustomUserAndCouponRepository {

	/**
	 * 현재 시간 기준으로 만료된 쿠폰 중 사용되지 않은 쿠폰을 조회합니다.
	 *
	 * @param currentDateTime 현재 날짜와 시간
	 * @return 만료된 시간 이전이며 사용되지 않은 쿠폰 목록
	 */
	List<UserAndCoupon> findByExpiredDateBeforeAndIsUsedIsFalse(LocalDateTime currentDateTime);

	/**
	 * 특정 쿠폰 정책에 연결된 쿠폰을 조회합니다.
	 *
	 * @param couponPolicy 쿠폰 정책 엔티티
	 * @return 특정 쿠폰 정책에 연결된 쿠폰 목록
	 */
	List<UserAndCoupon> findByCouponPolicy(CouponPolicy couponPolicy);


}