package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstorecoupon.auth.annotation.CurrentUser;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.UserCouponValidationException;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.impl.RabbitMQUserAndCouponService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 사용자와 쿠폰 관련 API를 제공하는 컨트롤러 클래스입니다.
 * <p>
 * 이 클래스는 사용자에게 쿠폰을 발행하거나 조회하는 등의 기능을 제공합니다.
 * </p>
 *
 * @author 이기훈
 */
@RestController
@RequestMapping("/coupons")
@Tag(name = "UserAndCoupon", description = "사용자 쿠폰관련 API")
public class UserAndCouponController {

    private final UserAndCouponService userAndCouponService;
    private final RabbitMQUserAndCouponService rabbitMQUserAndCouponService;

    public UserAndCouponController(UserAndCouponService userAndCouponService, RabbitMQUserAndCouponService rabbitMQUserAndCouponService) {
        this.userAndCouponService = userAndCouponService;
        this.rabbitMQUserAndCouponService = rabbitMQUserAndCouponService;
    }


    /**
     * 특정 쿠폰 ID로 사용자에게 쿠폰을 발행합니다.
     *
     * @param couponId 쿠폰 ID
     * @param currentUser 현재 인증된 사용자 정보
     * @return 쿠폰 발행 결과
     */
    @Operation(summary = "사용자와 쿠폰 생성", description = "특정 쿠폰 ID로 사용자에게 쿠폰을 발행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "쿠폰이 성공적으로 발행되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @AuthorizeRole({"COUPON_ADMIN", "MEMBER", "HEAD_ADMIN"})
    @PostMapping("/{couponId}")
    public ResponseEntity<Void> createUserAndCoupon(@Parameter(description = "쿠폰아이디", required = true) @PathVariable("couponId") Long couponId, @Parameter(description = "유저 아이디 가져오는 용도", required = true) @CurrentUser CurrentUserDetails currentUser) {
        Long userId= currentUser.getUserId();
       rabbitMQUserAndCouponService.createUserAndCoupon(couponId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    /**
     * 특정 사용자에게 웰컴 쿠폰을 발행합니다.
     *
     * @param userId 사용자 ID
     * @return 웰컴 쿠폰 발행 결과
     */
    @Operation(summary = "웰컴 쿠폰 생성", description = "특정 사용자에게 웰컴 쿠폰을 발행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "웰컴 쿠폰이 성공적으로 발행되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PostMapping("/coupon/welcome")
    public ResponseEntity<Void> createUserWelcomeCouponIssue(@Parameter(description = "사용자 ID", required = true) Long userId) {
        if (userId == null) {
            ErrorStatus errorStatus = ErrorStatus.from("유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }
        userAndCouponService.createUserWelcomeCouponIssue(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }




    /**
     * 특정 사용자 기준으로 페이징된 모든 쿠폰을 조회합니다.
     *
     * @param currentUser 현재 인증된 사용자 정보
     * @param pageable 페이지 요청 정보
     * @return 페이징된 사용자 쿠폰 목록
     */
    @Operation(summary = "마이페이지 쿠폰 조회", description = "특정 사용자 기준으로 페이징된 모든 쿠폰을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @AuthorizeRole({"COUPON_ADMIN", "MEMBER", "HEAD_ADMIN"})
    @GetMapping("/users/user")
    public ResponseEntity<Page<UserAndCouponResponseDTO>> getAllUserAndCouponsByUserPaging(@Parameter(description = "유저 아이디 가져오는 용도", required = true) @CurrentUser CurrentUserDetails currentUser, @Parameter(description = "페이지 수, 페이지 사이즈", required = false) @PageableDefault(page = 1, size = 4) Pageable pageable) {
        Long userId= currentUser.getUserId();
        if (userId == null) {
            ErrorStatus errorStatus = ErrorStatus.from( "유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }
        Page<UserAndCouponResponseDTO> coupons = userAndCouponService.getAllUsersAndCouponsByUserPaging(
           userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }

    /**
     * 관리자 기준으로 페이징된 모든 사용자 쿠폰을 조회합니다.
     *
     * @param pageable 페이지 요청 정보
     * @param type 정책 타입 (선택 사항)
     * @param userId 사용자 ID (선택 사항)
     * @return 페이징된 사용자 쿠폰 목록
     */
    @Operation(summary = "관리자 기준 페이징된 사용자 쿠폰 조회", description = "관리자 기준으로 페이징된 모든 사용자 쿠폰을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
    @GetMapping("/users")
    public ResponseEntity<Page<UserAndCouponResponseDTO>> getAllUsersAndCouponsByManagerPaging(
        @Parameter(description = "페이지 수, 페이지 사이즈", required = false)   @PageableDefault(page = 1, size = 4) Pageable pageable,
        @Parameter(description = "정책타입", required = false)  @RequestParam(required = false) String type,
        @Parameter(description = "유저 아이디", required = false) @RequestParam(required = false) Long userId
    ) {

        Page<UserAndCouponResponseDTO> coupons = userAndCouponService.getAllUsersAndCouponsByManagerPaging(pageable, type, userId);
        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }


    /**
     * 특정 주문에 대한 쿠폰을 조회합니다.
     *
     * @param currentUserDetails 현재 인증된 사용자 정보
     * @param bookIds 도서 ID 목록 (선택 사항)
     * @param categoryIds 카테고리 ID 목록 (선택 사항)
     * @param bookPrice 도서 가격
     * @return 특정 주문에 대한 쿠폰 목록
     */
    @Operation(summary = "단건 주문에 대한 쿠폰 조회", description = "특정 주문에 대한 쿠폰을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @AuthorizeRole({"COUPON_ADMIN", "MEMBER", "HEAD_ADMIN"})
    @GetMapping("/users/order")
    public ResponseEntity<List<UserAndCouponResponseDTO>> findCouponByOrder(
        @Parameter(description = "유저 아이디 가져오는 용도", required = true) @CurrentUser CurrentUserDetails currentUserDetails,
        @Parameter(description = "도서 아이디", required = false) @RequestParam(required = false) List<Long> bookIds,
        @Parameter(description = "카테고리 아이디", required = false) @RequestParam(required = false) List<Long> categoryIds,
        @Parameter(description = "도서 가격", required = true) @RequestParam BigDecimal bookPrice) {

        if (currentUserDetails.getUserId() == null) {
            ErrorStatus errorStatus = ErrorStatus.from("유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }

        List<UserAndCouponResponseDTO> coupons = userAndCouponService.findCouponByOrder(currentUserDetails.getUserId(), bookIds, categoryIds, bookPrice);

        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }



    /**
     * 특정 장바구니 주문에 대한 쿠폰을 조회합니다.
     *
     * @param currentUserDetails 현재 인증된 사용자 정보
     * @param bookDetails 도서 주문 정보 (선택 사항)
     * @return 특정 장바구니 주문에 대한 쿠폰 목록
     */
    @Operation(summary = "장바구니 주문에 대한 쿠폰 조회", description = "특정 장바구니 주문에 대한 쿠폰을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @AuthorizeRole({"COUPON_ADMIN", "MEMBER", "HEAD_ADMIN"})
    @PostMapping("/users/order/carts")
    public ResponseEntity<List<UserAndCouponResponseDTO>> findCouponByCartOrder(
        @Parameter(description = "유저 아이디 가져오는 용도", required = true) @CurrentUser CurrentUserDetails currentUserDetails,
        @Parameter(description = "도서 주문 정보", required = false) @RequestBody(required = false) List<GetBookByOrderCouponResponse> bookDetails
       ) {

        if (currentUserDetails.getUserId() == null) {
            ErrorStatus errorStatus = ErrorStatus.from("유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }

        List<UserAndCouponResponseDTO> coupons = userAndCouponService.findCouponByCartOrder(currentUserDetails.getUserId(),bookDetails);

        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }


    /**
     * 특정 사용자 쿠폰 ID로 쿠폰을 결제 후 사용됨 처리합니다.
     *
     * @param userAndCouponId 사용자 쿠폰 ID
     * @return 쿠폰 사용됨 처리 결과
     */
    @Operation(summary = "결제 후 쿠폰 사용됨처리", description = "특정 사용자 쿠폰 ID로 쿠폰을 결제 후 사용됨 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "쿠폰이 성공적으로 업데이트되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @AuthorizeRole({"COUPON_ADMIN", "MEMBER", "HEAD_ADMIN"})
    @PatchMapping("/users/payment/{userAndCouponId}")
    public ResponseEntity<Void> updateCouponAfterPayment(
        @Parameter(description = "사용자 쿠폰아이디", required = true) @PathVariable("userAndCouponId") Long userAndCouponId) {

        if (userAndCouponId == null) {
            ErrorStatus errorStatus = ErrorStatus.from("사용자 쿠폰 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }


         userAndCouponService.updateCouponAfterPayment(userAndCouponId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }



    /**
     * 특정 쿠폰 ID로 선택된 쿠폰을 조회합니다.
     *
     * @param couponId 쿠폰 ID
     * @return 선택된 쿠폰 정보
     */
    @Operation(summary = "선택된 쿠폰 조회", description = "특정 쿠폰 ID로 선택된 쿠폰을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/users/order/coupon")
    @AuthorizeRole({"COUPON_ADMIN", "MEMBER", "HEAD_ADMIN"})
    public ResponseEntity<UserAndCouponOrderResponseDTO> getSelectedCoupon(
        @Parameter(description = "쿠폰아이디", required = true)  @RequestParam(value = "couponId") Long couponId) {
        if (couponId == null) {
            ErrorStatus errorStatus = ErrorStatus.from("사용자 쿠폰 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }

        UserAndCouponOrderResponseDTO coupon = userAndCouponService.findUserAndCouponsById(couponId);
        return ResponseEntity.status(HttpStatus.OK).body(coupon);
    }

    /**
     * 비회원인지 회원인지 검사합니다.
     *
     * @param currentUserDetails 현재 인증된 사용자 정보
     * @return 사용자의 회원 여부
     */
    @Operation(summary = "회원인지 아닌지 검사", description = "비회원인지 회원인지 검사합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검사결과를 true or false로 반환"),
    })
    @GetMapping("/users/auth")
    public ResponseEntity<Boolean> isRealUserCheck(
        @Parameter(description = "유저 아이디 가져오는 용도", required = true) @CurrentUser CurrentUserDetails currentUserDetails) {
        if(currentUserDetails==null){
            return ResponseEntity.status(HttpStatus.OK).body(false);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

    }




}