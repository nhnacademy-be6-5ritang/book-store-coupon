package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    void createUserAndCoupon(Long couponId, Long userId);

    void createUserWelcomeCouponIssue(Long userId);





    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByUserPaging(Long userId, Pageable pageable);
    Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByManagerPaging(Pageable pageable, String type, Long userId);

    void findExpiredCoupons();
    void issueBirthdayCoupon();


    List<UserAndCouponResponseDTO> findCouponByOrder(Long userId, List<String> bookTitles ,List<String> categoryNames);

}
