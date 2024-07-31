package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

/**
 * 사용자와 쿠폰 관련 데이터에 대한 커스텀 리포지토리 인터페이스입니다.
 * <p>
 * 이 인터페이스는 사용자와 쿠폰 관련 데이터를 조회하기 위한 메소드들을 정의하고 있습니다.
 * </p>
 */
public interface CustomUserAndCouponRepository {

	/**
	 * 주어진 사용자 ID와 페이징 정보를 기준으로 사용자 쿠폰을 조회합니다.
	 *
	 * @param pageable 페이징 정보 (페이지 번호와 페이지 크기)
	 * @param userId 사용자 ID
	 * @param bookIdMap 도서 ID와 도서 정보를 매핑한 맵
	 * @param categoryIdMap 카테고리 ID와 카테고리 정보를 매핑한 맵
	 * @return 사용자 쿠폰에 대한 페이징된 결과가 포함된 {@link Page}
	 */

	Page<UserAndCouponResponseDTO> findAllByUserPaging(Pageable pageable, Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);

	/**
	 * 관리자 기준으로 페이징된 사용자 쿠폰을 조회합니다.
	 *
	 * @param pageable 페이징 정보 (페이지 번호와 페이지 크기)
	 * @param type 쿠폰 정책 타입
	 * @param userId (선택적) 사용자 ID
	 * @param bookIdMap 도서 ID와 도서 정보를 매핑한 맵
	 * @param categoryIdMap 카테고리 ID와 카테고리 정보를 매핑한 맵
	 * @return 사용자 쿠폰에 대한 페이징된 결과가 포함된 {@link Page}
	 */
	Page<UserAndCouponResponseDTO> findAllByManagerPaging(Pageable pageable, String type, Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);

	/**
	 * 주문에 대한 쿠폰을 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @param bookIdMap 도서 ID와 도서 정보를 매핑한 맵
	 * @param categoryIdMap 카테고리 ID와 카테고리 정보를 매핑한 맵
	 * @param bookIds (선택적) 도서 ID 목록
	 * @param categoryIds (선택적) 카테고리 ID 목록
	 * @param bookPrice 도서 가격
	 * @return 주문에 대한 쿠폰 목록
	 */
	List<UserAndCouponResponseDTO> findCouponByOrder(Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap,
		List<Long> bookIds, List<Long> categoryIds,
		BigDecimal bookPrice);

	/**
	 * 장바구니 주문에 대한 쿠폰을 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @param bookIdMap 도서 ID와 도서 정보를 매핑한 맵
	 * @param categoryIdMap 카테고리 ID와 카테고리 정보를 매핑한 맵
	 * @param bookDetails (선택적) 도서 주문 정보
	 * @return 장바구니 주문에 대한 쿠폰 목록
	 */
	List<UserAndCouponResponseDTO> findCouponByCartOrder(
		Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap,
		List<GetBookByOrderCouponResponse> bookDetails
	);
}