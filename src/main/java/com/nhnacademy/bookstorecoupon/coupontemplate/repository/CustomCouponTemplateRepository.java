package com.nhnacademy.bookstorecoupon.coupontemplate.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;

/**
 * @author 이기훈
 * 쿠폰 템플릿과 관련된 사용자 정의 데이터 접근 레이어 인터페이스입니다.
 * 이 인터페이스는 사용자와 관리자가 페이징된 쿠폰 템플릿을 조회할 수 있도록 하는 메소드를 제공합니다.
 */
public interface CustomCouponTemplateRepository  {
	/**
	 * 사용자 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 번호와 페이지 크기 정보
	 * @param bookIdMap 책 ID와 관련된 쿠폰 정보 맵
	 * @param categoryIdMap 카테고리 ID와 관련된 쿠폰 정보 맵
	 * @return 페이징된 쿠폰 템플릿 목록
	 */
	Page<CouponTemplateResponseDTO> findAllTemplatesByUserPaging(Pageable pageable,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);

	/**
	 * 관리자 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 번호와 페이지 크기 정보
	 * @param bookIdMap 책 ID와 관련된 쿠폰 정보 맵
	 * @param categoryIdMap 카테고리 ID와 관련된 쿠폰 정보 맵
	 * @return 페이징된 쿠폰 템플릿 목록
	 */
	 Page<CouponTemplateResponseDTO> findAllTemplatesByManagerPaging(Pageable pageable,
		 Map<Long, BookCoupon.BookInfo> bookIdMap,
		 Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);
}