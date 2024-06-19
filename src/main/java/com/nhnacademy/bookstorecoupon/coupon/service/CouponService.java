package com.nhnacademy.bookstorecoupon.coupon.service;

import com.example.demo.dto.UserCouponDTO;
import com.example.demo.entity.UsersAndCoupons;

import java.util.List;

public interface CouponService {
    List<UserCouponDTO> getAvailableCoupons(Long userId);
    List<UserCouponDTO> getUsedCoupons(Long userId);
}
