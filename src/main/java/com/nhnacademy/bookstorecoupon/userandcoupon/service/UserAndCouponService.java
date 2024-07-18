package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    // void createUserAndCoupon(Long couponId, Long userId);

    void createUserWelcomeCouponIssue(Long userId);





    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByUserPaging(Long userId, Pageable pageable);
    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByManagerPaging(Pageable pageable, String type, Long userId);

    void findExpiredCoupons();
    void issueBirthdayCoupon();


    List<UserAndCouponResponseDTO> findCouponByOrder(Long userId, List<Long> bookIds, List<Long> categoryIds, BigDecimal bookPrice);

    List<UserAndCouponResponseDTO> findCouponByCartOrder(Long userId, List<GetBookByOrderCouponResponse> bookDetails);

    void updateCouponAfterPayment(Long userAndCouponId);


    UserAndCouponOrderResponseDTO findUserAndCouponsById(Long couponIds);
}
