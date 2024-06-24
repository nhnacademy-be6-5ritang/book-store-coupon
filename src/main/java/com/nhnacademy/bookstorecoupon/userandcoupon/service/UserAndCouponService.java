package com.nhnacademy.bookstorecoupon.userandcoupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface UserAndCouponService {


    UserAndCouponResponseDTO createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO);

    UserAndCouponResponseDTO updateUserAndCoupon(String userEmail);


    // Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(Pageable pageable);
    Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(String userEmail, String type,Pageable pageable);

    Page<UserAndCouponResponseDTO> getUserAndCouponByIdPaging(String userEmail, Pageable pageable);

}
