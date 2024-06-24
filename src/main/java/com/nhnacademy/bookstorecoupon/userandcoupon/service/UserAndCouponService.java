package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import java.util.List;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    UserAndCouponResponseDTO createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO);

    UserAndCouponResponseDTO updateUserAndCoupon(String userEmail);

    List<UserAndCouponResponseDTO> getAllUserAndCoupons();

    List<UserAndCouponResponseDTO> getUserAndCouponById(String userEmail);

}
