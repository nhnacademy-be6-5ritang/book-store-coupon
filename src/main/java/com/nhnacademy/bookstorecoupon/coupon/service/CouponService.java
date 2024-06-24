package com.nhnacademy.bookstorecoupon.coupon.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.coupon.domain.dto.request.CouponRequestDTO;
import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;

public interface CouponService {

    CouponResponseDTO createCoupon(CouponRequestDTO requestDTO);
    List<CouponResponseDTO> getAllCoupons();
    Page<CouponResponseDTO> getAllCouponPaging(Pageable pageable);
    CouponResponseDTO getCouponById(Long id);
}