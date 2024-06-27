package com.nhnacademy.bookstorecoupon.coupontemplate.controller;

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

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.service.CouponTemplateService;

@RestController
@RequestMapping("/coupons")
public class CouponTemplateController {

	@Autowired
	private final CouponTemplateService couponTemplateService;

	public CouponTemplateController(CouponTemplateService couponTemplateService) {
		this.couponTemplateService = couponTemplateService;
	}

	@PostMapping
	public ResponseEntity<CouponTemplateResponseDTO> createCouponTemplate(@RequestBody CouponTemplateRequestDTO requestDTO) {
		CouponTemplateResponseDTO couponTemplateResponseDTO = couponTemplateService.createCouponTemplate(requestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(couponTemplateResponseDTO);
	}



	@GetMapping("/issue")
	public ResponseEntity<Page<CouponTemplateResponseDTO>> getAllCouponTemplatePaging(Pageable pageable) {
		Page<CouponTemplateResponseDTO> coupons = couponTemplateService.getAllCouponTemplatePaging(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(coupons);
	}


	@GetMapping
	public ResponseEntity<List<CouponTemplateResponseDTO>> getAllCouponTemplates() {
		List<CouponTemplateResponseDTO> coupons = couponTemplateService.getAllCouponTemplates();
		return ResponseEntity.status(HttpStatus.OK).body(coupons);
	}



	// @GetMapping("/{id}")
	// public ResponseEntity<CouponResponseDTO> getCouponById(@PathVariable Long id) {
	// 	CouponResponseDTO coupon = couponService.getCouponById(id);
	// 	return ResponseEntity.status(HttpStatus.OK).body(coupon);
	// }


}
