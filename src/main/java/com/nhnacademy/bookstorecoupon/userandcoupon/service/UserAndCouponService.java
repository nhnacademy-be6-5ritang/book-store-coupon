package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

/**
 * @author 이기훈
 * 사용자와 쿠폰 관련 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface UserAndCouponService {

    /**
     * 특정 사용자에게 웰컴 쿠폰을 발행합니다.
     *
     * @param userId 쿠폰을 발행할 사용자 ID
     */
    void createUserWelcomeCouponIssue(Long userId);

    /**
     * 특정 사용자에 대한 페이징된 쿠폰 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 특정 사용자에 대한 페이징된 쿠폰 목록
     */
    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByUserPaging(Long userId, Pageable pageable);

    /**
     * 관리자를 기준으로 페이징된 모든 사용자 쿠폰을 조회합니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @param type 정책 유형 (옵션)
     * @param userId 사용자 ID (옵션)
     * @return 관리자를 기준으로 페이징된 사용자 쿠폰 목록
     */
    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByManagerPaging(Pageable pageable, String type, Long userId);

    /**
     * 만료된 쿠폰을 처리합니다.
     */
    void findExpiredCoupons();

    /**
     * 생일 쿠폰을 발행합니다.
     */
    void issueBirthdayCoupon();

    /**
     * 특정 주문에 대해 사용할 수 있는 쿠폰을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param bookIds 도서 ID 목록 (옵션)
     * @param categoryIds 카테고리 ID 목록 (옵션)
     * @param bookPrice 도서 가격
     * @return 주어진 주문에 대해 사용할 수 있는 쿠폰 목록
     */
    List<UserAndCouponResponseDTO> findCouponByOrder(Long userId, List<Long> bookIds, List<Long> categoryIds, BigDecimal bookPrice);

    /**
     * 장바구니 주문에 대해 사용할 수 있는 쿠폰을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param bookDetails 도서 주문 정보 목록
     * @return 장바구니 주문에 대해 사용할 수 있는 쿠폰 목록
     */
    List<UserAndCouponResponseDTO> findCouponByCartOrder(Long userId, List<GetBookByOrderCouponResponse> bookDetails);

    /**
     * 결제 후 쿠폰을 사용됨 상태로 업데이트합니다.
     *
     * @param userAndCouponId 사용자 쿠폰 ID
     */
    void updateCouponAfterPayment(Long userAndCouponId);

    /**
     * 특정 쿠폰 ID로 쿠폰을 조회합니다.
     *
     * @param couponIds 쿠폰 ID
     * @return 특정 쿠폰 ID에 대한 쿠폰 정보
     */
    UserAndCouponOrderResponseDTO findUserAndCouponsById(Long couponIds);
}
