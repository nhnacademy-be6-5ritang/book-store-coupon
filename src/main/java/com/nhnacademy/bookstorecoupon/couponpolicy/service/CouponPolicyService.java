package com.nhnacademy.bookstorecoupon.couponpolicy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;

/**
 * @author 이기훈
 * 쿠폰 정책을 관리하는 서비스 레이어를 나타내는 인터페이스입니다.
 */
public interface CouponPolicyService {
	/**
	 * 주어진 요청 데이터에 기반하여 환영 쿠폰을 발급합니다.
	 *
	 * @param requestDTO 환영 쿠폰 발급에 필요한 데이터 전송 객체
	 */
	void issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO);

	/**
	 * 주어진 요청 데이터에 기반하여 생일 쿠폰을 발급합니다.
	 *
	 * @param requestDTO 생일 쿠폰 발급에 필요한 데이터 전송 객체
	 */
	void issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO);

	/**
	 * 주어진 요청 데이터에 기반하여 특정 도서에 대한 쿠폰을 발급합니다.
	 *
	 * @param requestDTO 특정 도서 쿠폰 발급에 필요한 데이터 전송 객체
	 */
	void issueSpecificBookCoupon(CouponPolicyRequestDTO requestDTO);

	/**
	 * 주어진 요청 데이터에 기반하여 특정 카테고리에 대한 쿠폰을 발급합니다.
	 *
	 * @param requestDTO 특정 카테고리 쿠폰 발급에 필요한 데이터 전송 객체
	 */
	void issueSpecificCategoryCoupon(CouponPolicyRequestDTO requestDTO);

	/**
	 * 주어진 요청 데이터에 기반하여 할인 쿠폰을 발급합니다.
	 *
	 * @param requestDTO 할인 쿠폰 발급에 필요한 데이터 전송 객체
	 */
	void issueDiscountCoupon(CouponPolicyRequestDTO requestDTO);

	/**
	 * 페이지네이션을 지원하여 모든 쿠폰 정책을 조회합니다.
	 *
	 * @param pageable 페이지 번호, 페이지 크기, 정렬 정보 등을 포함하는 페이지네이션 정보
	 * @return 쿠폰 정책 응답 데이터 전송 객체의 페이지
	 */
	Page<CouponPolicyResponseDTO> getAllCouponPolicies(Pageable pageable);

	/**
	 * 주어진 ID를 가진 기존 쿠폰 정책을 새 데이터로 업데이트합니다.
	 *
	 * @param id 업데이트할 쿠폰 정책의 ID
	 * @param requestDTO 쿠폰 정책에 대한 업데이트된 데이터 전송 객체
	 */
	void updateCouponPolicy(Long id, CouponPolicyUpdateRequestDTO requestDTO);
}
