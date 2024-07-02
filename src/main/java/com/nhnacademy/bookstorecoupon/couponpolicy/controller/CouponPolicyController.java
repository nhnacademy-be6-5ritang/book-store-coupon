package com.nhnacademy.bookstorecoupon.couponpolicy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO2;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.impl.CouponPolicyServiceImpl;

@RestController
@RequestMapping("/coupons/policies")
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

	@PostMapping("/books/{bookId}")
	public ResponseEntity<Void> issueSpecificBookCoupon(@PathVariable("bookId") Long bookId, @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueSpecificBookCoupon(bookId, couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/categories/{categoryId}")
	public ResponseEntity<Void> issueSpecificCategoryCoupon(@PathVariable("categoryId") Long categoryId, @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueSpecificCategoryCoupon(categoryId, couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/sale")
	public ResponseEntity<Void> issueDiscountCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueDiscountCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<List<CouponPolicyResponseDTO2>> getAllCouponPolicies() {
		List<CouponPolicyResponseDTO2> policies = couponPolicyService.getAllCouponPolicies();
		return ResponseEntity.status(HttpStatus.OK).body(policies);
	}



	@PatchMapping("/{couponPolicyId}")
	public ResponseEntity<Void> updateCouponPolicy(@PathVariable Long couponPolicyId, @RequestBody CouponPolicyUpdateRequestDTO requestDTO) {
		couponPolicyService.updateCouponPolicy(couponPolicyId, requestDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
