package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    UserAndCouponResponseDTO createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO);

    UserAndCouponResponseDTO updateUserAndCoupon(Long userId);


    // Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(Pageable pageable);
    Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(Long userId, String type,Pageable pageable);

    Page<UserAndCouponResponseDTO> getUserAndCouponByIdPaging(Long userId, Pageable pageable);

}
