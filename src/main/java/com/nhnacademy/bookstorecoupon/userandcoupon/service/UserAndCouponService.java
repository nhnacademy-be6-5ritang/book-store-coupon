package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    UserAndCouponResponseDTO createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO);

    UserAndCouponResponseDTO updateUserAndCoupon(String userEmail);

    // List<UserAndCouponResponseDTO> getAllUserAndCoupons();

    Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(Pageable pageable);

    // List<UserAndCouponResponseDTO> getUserAndCouponById(String userEmail);
    Page<UserAndCouponResponseDTO> getUserAndCouponByIdPaging(String userEmail, Pageable pageable);

}
