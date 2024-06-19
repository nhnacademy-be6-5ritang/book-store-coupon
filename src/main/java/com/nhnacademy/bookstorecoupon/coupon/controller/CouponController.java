package com.nhnacademy.bookstorecoupon.coupon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.coupon.dto.CouponDTO;
import com.nhnacademy.bookstorecoupon.coupon.service.impl.CouponServiceImpl;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

	private final CouponServiceImpl couponServiceImpl;

	public CouponController(CouponServiceImpl couponServiceImpl) {
		this.couponServiceImpl = couponServiceImpl;
	}

	@GetMapping("/available")
	public List<CouponDTO> getAvailableCoupons(@RequestParam Long userId) {
		return couponServiceImpl.getAvailableCouponsForUser(userId);
	}

	@GetMapping("/used")
	public List<CouponUsageDTO> getUsedCoupons(@RequestParam Long userId) {
		return couponServiceImpl.getUsedCouponsForUser(userId);
	}

	@PostMapping("/apply")
	public void applyCoupon(@RequestParam Long userId, @RequestParam Long couponId) {
		couponServiceImpl.applyCoupon(userId, couponId);
	}

	@PostMapping("/expire")
	public void expireCoupons() {
		couponServiceImpl.expireOldCoupons();
	}


}

