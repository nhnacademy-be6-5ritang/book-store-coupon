package com.nhnacademy.bookstorecoupon.coupontemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.service.CouponTemplateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/coupons")
public class CouponTemplateController {

	@Autowired
	private final CouponTemplateService couponTemplateService;

	public CouponTemplateController(CouponTemplateService couponTemplateService) {
		this.couponTemplateService = couponTemplateService;
	}

	@PostMapping
	public ResponseEntity<Void> createCouponTemplate(@Valid @RequestBody CouponTemplateRequestDTO requestDTO) {
		couponTemplateService.createCouponTemplate(requestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}



	@GetMapping("/issue")
	public ResponseEntity<Page<CouponTemplateResponseDTO>> getAllCouponTemplatesByUserPaging(@PageableDefault(page = 1, size = 3)Pageable pageable) {
		Page<CouponTemplateResponseDTO> coupons = couponTemplateService.getAllCouponTemplatesByUserPaging(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(coupons);


	}


	@GetMapping
	public ResponseEntity<Page<CouponTemplateResponseDTO>> getAllCouponTemplatesByManagerPaging(@PageableDefault(page = 1, size = 3) Pageable pageable
		) {

		Page<CouponTemplateResponseDTO> coupons = couponTemplateService.getAllCouponTemplatesByManagerPaging(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(coupons);
	}




}
