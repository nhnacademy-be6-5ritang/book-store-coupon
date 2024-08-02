package com.nhnacademy.bookstorecoupon.coupontemplate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;

/**
 * @author 이기훈
 * 쿠폰 템플릿과 관련된 서비스 인터페이스입니다.
 * 이 인터페이스는 쿠폰 템플릿을 생성하고, 페이징된 쿠폰 템플릿 목록을 관리하는 기능을 제공합니다.
 */

public interface CouponTemplateService {

	/**
	 * 새로운 쿠폰 템플릿을 생성합니다.
	 *
	 * @param requestDTO 생성할 쿠폰 템플릿의 요청 데이터
	 */
	void createCouponTemplate(CouponTemplateRequestDTO requestDTO);

	/**
	 * 관리자를 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 번호와 페이지 크기 정보
	 * @return 관리자가 조회할 수 있는 페이징된 쿠폰 템플릿 목록
	 */
	Page<CouponTemplateResponseDTO> getAllCouponTemplatesByManagerPaging(Pageable pageable);

	/**
	 * 사용자를 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 번호와 페이지 크기 정보
	 * @return 사용자가 조회할 수 있는 페이징된 쿠폰 템플릿 목록
	 */
	Page<CouponTemplateResponseDTO> getAllCouponTemplatesByUserPaging(Pageable pageable);

}