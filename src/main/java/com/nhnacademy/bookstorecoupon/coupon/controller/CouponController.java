package com.nhnacademy.bookstorecoupon.coupon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.coupon.domain.dto.request.CouponRequestDTO;
import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;
import com.nhnacademy.bookstorecoupon.coupon.service.CouponService;

@RestController
@RequestMapping("/coupons")
public class CouponController {

	@Autowired
	private final CouponService couponService;

	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@PostMapping
	public ResponseEntity<CouponResponseDTO> createCoupon(@RequestBody CouponRequestDTO requestDTO) {
		CouponResponseDTO couponResponseDTO = couponService.createCoupon(requestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(couponResponseDTO);
	}



	@GetMapping("/issue")
	public ResponseEntity<Page<CouponResponseDTO>> getAllCouponPaging(Pageable pageable) {
		Page<CouponResponseDTO> coupons = couponService.getAllCouponPaging(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(coupons);
	}


	@GetMapping
	public ResponseEntity<List<CouponResponseDTO>> getAllCoupons() {
		List<CouponResponseDTO> coupons = couponService.getAllCoupons();
		return ResponseEntity.status(HttpStatus.OK).body(coupons);
	}



	// @GetMapping("/{id}")
	// public ResponseEntity<CouponResponseDTO> getCouponById(@PathVariable Long id) {
	// 	CouponResponseDTO coupon = couponService.getCouponById(id);
	// 	return ResponseEntity.status(HttpStatus.OK).body(coupon);
	// }


}
