package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

/**
 * {@link UserAndCoupon} 엔티티에 대한 JPA 리포지토리입니다.
 * <p>
 * 이 리포지토리는 사용자와 쿠폰 간의 관계를 관리하며, 사용자와 쿠폰에 대한 CRUD 연산을 지원합니다.
 * 또한 커스텀 리포지토리 인터페이스 {@link CustomUserAndCouponRepository}를 확장하여 커스텀 쿼리 메소드를 제공합니다.
 * </p>
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