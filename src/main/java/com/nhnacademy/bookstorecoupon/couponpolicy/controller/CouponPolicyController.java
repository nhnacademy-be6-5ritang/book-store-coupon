package com.nhnacademy.bookstorecoupon.couponpolicy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.impl.CouponPolicyServiceImpl;

@RestController
@RequestMapping("/coupons/policies")
public class CouponPolicyController {
	private final CouponPolicyServiceImpl couponPolicyService;

	public CouponPolicyController(CouponPolicyServiceImpl couponPolicyService) {
		this.couponPolicyService = couponPolicyService;
	}

	@PostMapping("/welcome")
	public ResponseEntity<CouponPolicyResponseDTO> issueWelcomeCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		CouponPolicyResponseDTO response = couponPolicyService.issueWelcomeCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/birthday")
	public ResponseEntity<CouponPolicyResponseDTO> issueBirthdayCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		CouponPolicyResponseDTO response = couponPolicyService.issueBirthdayCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/books/{bookId}")
	public ResponseEntity<CouponPolicyResponseDTO> issueSpecificBookCoupon(@PathVariable("bookId") Long bookId, @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		CouponPolicyResponseDTO response = couponPolicyService.issueSpecificBookCoupon(bookId, couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/categories/{categoryId}")
	public ResponseEntity<CouponPolicyResponseDTO> issueSpecificCategoryCoupon(@PathVariable("categoryId") Long categoryId, @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		CouponPolicyResponseDTO response = couponPolicyService.issueSpecificCategoryCoupon(categoryId, couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/sale")
	public ResponseEntity<CouponPolicyResponseDTO> issueDiscountCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		CouponPolicyResponseDTO response = couponPolicyService.issueDiscountCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<List<CouponPolicyResponseDTO>> getAllCouponPolicies() {
		List<CouponPolicyResponseDTO> policies = couponPolicyService.getAllCouponPolicies();
		return ResponseEntity.status(HttpStatus.OK).body(policies);
	}

	@GetMapping("/{couponPolicyId}")
	public ResponseEntity<CouponPolicyResponseDTO> getCouponPolicyById(@PathVariable("couponPolicyId") Long couponPolicyId) {
		CouponPolicyResponseDTO policy = couponPolicyService.getCouponPolicyById(couponPolicyId);
		return ResponseEntity.status(HttpStatus.OK).body(policy);
	}

	@PutMapping("/{couponPolicyId}")
	public ResponseEntity<CouponPolicyResponseDTO> updateCouponPolicy(@PathVariable Long couponPolicyId, @RequestBody CouponPolicyRequestDTO requestDTO) {
		CouponPolicyResponseDTO response = couponPolicyService.updateCouponPolicy(couponPolicyId, requestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{couponPolicyId}")
	public ResponseEntity<Void> deleteCouponPolicy(@PathVariable Long couponPolicyId) {
		couponPolicyService.deleteCouponPolicy(couponPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
