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

import com.nhnacademy.bookstorecoupon.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.service.CouponTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


/**
 * 쿠폰 템플릿과 관련된 API를 제공하는 컨트롤러입니다.
 * 쿠폰 템플릿 생성 및 조회를 위한 엔드포인트를 제공합니다.
 *
 * @author 이기훈
 */
@Tag(name = "CouponTemplate", description = "쿠폰템플릿관련 API")
@RestController
@RequestMapping("/coupons")
public class CouponTemplateController {

	@Autowired
	private final CouponTemplateService couponTemplateService;

	public CouponTemplateController(CouponTemplateService couponTemplateService) {
		this.couponTemplateService = couponTemplateService;
	}




	/**
	 * 새로운 쿠폰 템플릿을 생성합니다.
	 *
	 * @param requestDTO 쿠폰 템플릿 발행에 필요한 데이터 전송 객체
	 * @return  쿠폰 템플릿 생성 결과를 담고 있는 HTTP 응답
	 */
	@Operation(summary = "쿠폰 템플릿 생성", description = "새로운 쿠폰 템플릿을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "쿠폰 템플릿이 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createCouponTemplate(@Parameter(description = "쿠폰 템플릿 발행시 요청데이터", required = true) @Valid @RequestBody CouponTemplateRequestDTO requestDTO) {
		couponTemplateService.createCouponTemplate(requestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}


	/**
	 * 유저 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 번호와 페이지 크기 정보
	 * @return {@link ResponseEntity} 페이징된 쿠폰 템플릿 목록을 담고 있는 HTTP 응답
	 */
	@Operation(summary = "유저 페이징으로 모든 쿠폰 템플릿 조회", description = "유저 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "쿠폰 템플릿 조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@GetMapping("/issue")
	public ResponseEntity<Page<CouponTemplateResponseDTO>> getAllCouponTemplatesByUserPaging(@Parameter(description = "페이지 수, 페이지 사이즈", required = false) @PageableDefault(page = 1, size = 3)Pageable pageable) {
		Page<CouponTemplateResponseDTO> coupons = couponTemplateService.getAllCouponTemplatesByUserPaging(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(coupons);


	}

	/**
	 * 관리자 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 번호와 페이지 크기 정보
	 * @return {@link ResponseEntity} 페이징된 쿠폰 템플릿 목록을 담고 있는 HTTP 응답
	 */
	@Operation(summary = "관리자 페이징으로 모든 쿠폰 템플릿 조회", description = "관리자 기준으로 페이징된 모든 쿠폰 템플릿을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "쿠폰 템플릿 조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@GetMapping
	public ResponseEntity<Page<CouponTemplateResponseDTO>> getAllCouponTemplatesByManagerPaging(@Parameter(description = "페이지 수, 페이지 사이즈", required = false) @PageableDefault(page = 1, size = 3) Pageable pageable
		) {

		Page<CouponTemplateResponseDTO> coupons = couponTemplateService.getAllCouponTemplatesByManagerPaging(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(coupons);
	}




}
