package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import java.util.List;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    UserAndCouponResponseDTO createUserAndCoupon(UserAndCouponRequestDTO requestDTO);

    UserAndCouponResponseDTO updateUserAndCoupon(Long id, UserAndCouponRequestDTO requestDTO);

    List<UserAndCouponResponseDTO> getAllUserAndCoupons();

    UserAndCouponResponseDTO getUserAndCouponById(Long id);

}
