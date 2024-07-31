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

import com.nhnacademy.bookstorecoupon.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyValidationException;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Tag(name = "CouponPolicy", description = "쿠폰 정책관련 API")
@RestController
@RequestMapping("/coupons/policies")
public class CouponPolicyController {
	private final CouponPolicyService couponPolicyService;

	public CouponPolicyController(CouponPolicyService couponPolicyService) {
		this.couponPolicyService = couponPolicyService;
	}

	@Operation(
		summary = "웰컴쿠폰정책 생성",
		description = "웰컴쿠폰정책을 생성합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "웰컴쿠폰정책이 성공적으로 발행되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/welcome")
	public ResponseEntity<Void> issueWelcomeCoupon(
		@Parameter(description = "웰컴쿠폰정책 발행 요청 데이터", required = true) @Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		couponPolicyService.issueWelcomeCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	@Operation(
		summary = "생일쿠폰정책 생성",
		description = "생일쿠폰정책을 생성합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "생일쿠폰정책이 성공적으로 발행되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/birthday")
	public ResponseEntity<Void> issueBirthdayCoupon(
		@Parameter(description = "생일쿠폰정책 발행 요청 데이터", required = true) @Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		couponPolicyService.issueBirthdayCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}


	@Operation(summary = "도서 쿠폰 발행", description = "특정 도서에 대한 도서쿠폰을 발행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "도서쿠폰이 성공적으로 발행되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/books")
	public ResponseEntity<Void> issueSpecificBookCoupon(
		@Parameter(description = "도서쿠폰정책 발행 요청 데이터", required = true) @Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		if (couponPolicyRequestDTO.bookId()==null || couponPolicyRequestDTO.bookTitle() == null){
			ErrorStatus errorStatus = ErrorStatus.from("북 쿠폰 발행시 책 아이디와 책 제목이 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);
		}
		couponPolicyService.issueSpecificBookCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "카테고리 쿠폰 발행", description = "특정 카테고리에 대한 카테고리쿠폰을 발행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "카테고리쿠폰이 성공적으로 발행되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/categories")
	public ResponseEntity<Void> issueSpecificCategoryCoupon(
		@Parameter(description = "카테고리쿠폰정책 발행 요청 데이터", required = true) @Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		if (couponPolicyRequestDTO.categoryId()==null || couponPolicyRequestDTO.categoryName() == null){
			ErrorStatus errorStatus = ErrorStatus.from("카테고리 쿠폰 발행시 카테고리 아이디와 카테고리 이름이 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);
		}
		couponPolicyService.issueSpecificCategoryCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}


	@Operation(summary = "할인 쿠폰 발행", description = "할인 쿠폰을 발행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "할인쿠폰이 성공적으로 발행되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/sale")
	public ResponseEntity<Void> issueDiscountCoupon(
		@Parameter(description = "할인쿠폰정책 발행 요청 데이터", required = true) @Valid @RequestBody CouponPolicyRequestDTO couponPolicyRequestDTO) {
		validateSaleFields(couponPolicyRequestDTO);
		couponPolicyService.issueDiscountCoupon(couponPolicyRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "모든 쿠폰 정책 조회", description = "페이징을 통해 모든 쿠폰 정책을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@GetMapping
	public ResponseEntity<Page<CouponPolicyResponseDTO>> getAllCouponPolicies(@Parameter(description = "페이지 수, 페이지 사이즈", required = false) @PageableDefault(page = 1, size = 3) Pageable pageable) {
		Page<CouponPolicyResponseDTO> policies = couponPolicyService.getAllCouponPolicies(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(policies);
	}



	@Operation(summary = "쿠폰 정책 업데이트", description = "쿠폰 정책을 업데이트합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 업데이트되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@PatchMapping("/{couponPolicyId}")
	public ResponseEntity<Void> updateCouponPolicy(
		@Parameter(description = "쿠폰 정책 아이디", required = true) @PathVariable Long couponPolicyId, @Parameter(description = "쿠폰정책 변경 요청 데이터", required = true)  @Valid @RequestBody CouponPolicyUpdateRequestDTO requestDTO) {
		if ((requestDTO.salePrice() == null && requestDTO.saleRate() == null && requestDTO.maxSalePrice() == null) ||
			(requestDTO.salePrice() != null && requestDTO.saleRate() != null  && requestDTO.maxSalePrice() != null)||
			(requestDTO.salePrice() != null && requestDTO.saleRate() != null  && requestDTO.maxSalePrice() == null)||
		(requestDTO.salePrice() != null && requestDTO.saleRate() == null  && requestDTO.maxSalePrice() != null)) {
			ErrorStatus errorStatus = ErrorStatus.from("해당 할인률, 최대가격과 할인가격은 동시에 작성할 수 없습니다", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);

		}
		couponPolicyService.updateCouponPolicy(couponPolicyId, requestDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}


	// SalePrice와 SaleRate 유효성 검사 메서드 추가
	private void validateSaleFields(CouponPolicyRequestDTO requestDTO) {
		if ((requestDTO.salePrice() == null && requestDTO.saleRate() == null && requestDTO.maxSalePrice() == null) ||
			(requestDTO.salePrice() != null && requestDTO.saleRate() != null  && requestDTO.maxSalePrice() != null)||
			(requestDTO.salePrice() != null && requestDTO.saleRate() != null  && requestDTO.maxSalePrice() == null)||
			(requestDTO.salePrice() != null && requestDTO.saleRate() == null  && requestDTO.maxSalePrice() != null)) {
			ErrorStatus errorStatus = ErrorStatus.from("해당 할인률, 최대가격과 할인가격은 동시에 작성할 수 없습니다", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyValidationException(errorStatus);
		}
	}


}
