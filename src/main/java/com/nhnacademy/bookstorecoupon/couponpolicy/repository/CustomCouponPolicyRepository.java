package com.nhnacademy.bookstorecoupon.couponpolicy.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;




/**
 * @author 이기훈
 * 쿠폰정책 테이블에 있는 정보를 querydsl을 통해 가져오는 custom repository
 */
public interface CustomCouponPolicyRepository {


	/**
	 * 쿠폰 정책 테이블에 있는 정보를 querydsl을 통해 가져오는 custom repository method
	 * @param pageable 페이지정보
	 * @param bookIdMap 도서관련 정보
	 * @param categoryIdMap 카테고리관련 정보
 	 * @return 쿠폰정책 응답 dto
	 */
	Page<CouponPolicyResponseDTO> findAllWithBooksAndCategories(Pageable pageable, Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);


	/**
	 * 해당 쿠폰타입에서 가장 최신의 정책을 불러오는 method
	 * @param type 쿠폰정책 타입
	 * @return 쿠폰정책 entity
	 */
	Optional<CouponPolicy> findLatestCouponPolicyByType(String type);
}
