package com.nhnacademy.bookstorecoupon.couponpolicy.controller;

import java.time.LocalDateTime;

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
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyValidationException;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/coupons/policies")
public class CouponPolicyController {
	private final CouponPolicyService couponPolicyService;

	public CouponPolicyController(CouponPolicyService couponPolicyService) {
		this.couponPolicyService = couponPolicyService;
	}

	@PostMapping("/welcome")
	public ResponseEntity<Void> issueWelcomeCoupon(@Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		couponPolicyService.issueWelcomeCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/birthday")
	public ResponseEntity<Void> issueBirthdayCoupon(@Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		couponPolicyService.issueBirthdayCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/books")
	public ResponseEntity<Void> issueSpecificBookCoupon(@Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		if (couponPolicyRequestDTO.bookId()==null && couponPolicyRequestDTO.bookTitle() == null){
			ErrorStatus errorStatus = ErrorStatus.from("북 쿠폰 발행시 책 아이디와 책 제목이 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);
		}
		couponPolicyService.issueSpecificBookCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/categories")
	public ResponseEntity<Void> issueSpecificCategoryCoupon(@Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		if (couponPolicyRequestDTO.categoryId()==null && couponPolicyRequestDTO.categoryName() == null){
			ErrorStatus errorStatus = ErrorStatus.from("카테고리 쿠폰 발행시 카테고리 아이디와 카테고리 이름이 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);
		}
		couponPolicyService.issueSpecificCategoryCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/sale")
	public ResponseEntity<Void> issueDiscountCoupon(@Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		couponPolicyService.issueDiscountCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<Page<CouponPolicyResponseDTO>> getAllCouponPolicies(@PageableDefault(page = 1, size = 3) Pageable pageable) {
		Page<CouponPolicyResponseDTO> policies = couponPolicyService.getAllCouponPolicies(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(policies);
	}



	@PatchMapping("/{couponPolicyId}")
	public ResponseEntity<Void> updateCouponPolicy(@PathVariable Long couponPolicyId, @Valid @RequestBody CouponPolicyUpdateRequestDTO requestDTO) {
		if ((requestDTO.salePrice() == null && requestDTO.saleRate() == null && requestDTO.maxSalePrice() == null) ||
			(requestDTO.salePrice() != null && requestDTO.saleRate() != null  && requestDTO.maxSalePrice() != null)) {
			ErrorStatus errorStatus = ErrorStatus.from("해당 할인률, 최대가격과 할인가격은 동시에 작성할 수 없습니다", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);

		}
		couponPolicyService.updateCouponPolicy(couponPolicyId, requestDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}


	// SalePrice와 SaleRate 유효성 검사 메서드 추가
	private void validateSaleFields(CouponPolicyRequestDTO requestDTO) {
		if ((requestDTO.salePrice() == null && requestDTO.saleRate() == null && requestDTO.maxSalePrice() == null) ||
			(requestDTO.salePrice() != null && requestDTO.saleRate() != null  && requestDTO.maxSalePrice() != null)) {
			ErrorStatus errorStatus = ErrorStatus.from("해당 할인률, 최대가격과 할인가격은 동시에 작성할 수 없습니다", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);
		}
	}


}
