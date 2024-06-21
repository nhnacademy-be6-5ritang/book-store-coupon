package com.nhnacademy.bookstorecoupon.coupon.service;

import java.util.List;

import com.nhnacademy.bookstorecoupon.coupon.domain.dto.request.CouponRequestDTO;
import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;

public interface CouponService {

    CouponResponseDTO createCoupon(CouponRequestDTO requestDTO);
    List<CouponResponseDTO> getAllCoupons();
    CouponResponseDTO getCouponById(Long id);
}