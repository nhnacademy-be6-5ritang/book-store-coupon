package com.nhnacademy.bookstorecoupon.couponpolicy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.impl.CouponPolicyServiceImpl;

@RestController
@RequestMapping("/coupon/policy")
public class CouponPolicyController {
	private final CouponPolicyServiceImpl couponPolicyService;

	public CouponPolicyController(CouponPolicyServiceImpl couponPolicyService) {
		this.couponPolicyService = couponPolicyService;
	}

	@PostMapping("/welcome")
	public ResponseEntity<Void> issueWelcomeCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueWelcomeCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/birthday")
	public ResponseEntity<Void> issueBirthdayCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueBirthdayCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/book/{bookId}")
	public ResponseEntity<Void> issueSpecificBookCoupon(@PathVariable("bookId") Long bookId,@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueSpecificBookCoupon(bookId, couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/category/{categoryId}")
	public ResponseEntity<Void> issueSpecificCategoryCoupon(@PathVariable("categoryId") Long categoryId,@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueSpecificCategoryCoupon(categoryId, couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/sale")
	public ResponseEntity<Void> issueDiscountCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		 couponPolicyService.issueDiscountCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}