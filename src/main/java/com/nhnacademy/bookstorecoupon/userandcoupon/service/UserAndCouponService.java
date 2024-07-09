package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    void createUserAndCoupon(Long couponId, Long userId,UserAndCouponCreateRequestDTO requestDTO);

    void createUserWelcomeCouponIssue(Long userId,UserAndCouponCreateRequestDTO requestDTO);



    // void findExpiredCoupons();

    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByUserPaging(Long userId, Pageable pageable);
    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByManagerPaging(Pageable pageable, String type, Long userId);
    // void issueBirthdayCoupon();

}
