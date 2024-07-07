package com.nhnacademy.bookstorecoupon.couponpolicy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.CouponPolicyService;

@RestController
@RequestMapping("/coupons/policies")
public class CouponPolicyController {
	private final CouponPolicyService couponPolicyService;

	public CouponPolicyController(CouponPolicyService couponPolicyService) {
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

	@PostMapping("/books")
	public ResponseEntity<Void> issueSpecificBookCoupon( @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueSpecificBookCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/categories")
	public ResponseEntity<Void> issueSpecificCategoryCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueSpecificCategoryCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/sale")
	public ResponseEntity<Void> issueDiscountCoupon(@RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		couponPolicyService.issueDiscountCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<Page<CouponPolicyResponseDTO>> getAllCouponPolicies(@PageableDefault(page = 1, size = 3) Pageable pageable) {
		Page<CouponPolicyResponseDTO> policies = couponPolicyService.getAllCouponPolicies(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(policies);
	}



	@PatchMapping("/{couponPolicyId}")
	public ResponseEntity<Void> updateCouponPolicy(@PathVariable Long couponPolicyId, @RequestBody CouponPolicyUpdateRequestDTO requestDTO) {
		couponPolicyService.updateCouponPolicy(couponPolicyId, requestDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
