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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.auth.annotation.CurrentUser;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.UserCouponValidationException;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.impl.RabbitMQUserAndCouponService;

@RestController
@RequestMapping("/coupons")
public class UserAndCouponController {

    private final UserAndCouponService userAndCouponService;
    private final RabbitMQUserAndCouponService rabbitMQUserAndCouponService;

    public UserAndCouponController(UserAndCouponService userAndCouponService, RabbitMQUserAndCouponService rabbitMQUserAndCouponService) {
        this.userAndCouponService = userAndCouponService;
        this.rabbitMQUserAndCouponService = rabbitMQUserAndCouponService;
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<Void> createUserAndCoupon(@PathVariable("couponId") Long couponId, @CurrentUser CurrentUserDetails currentUser) {
        Long userId= currentUser.getUserId();
       rabbitMQUserAndCouponService.createUserAndCoupon(couponId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @PostMapping("/coupon/welcome")
    public ResponseEntity<Void> createUserWelcomeCouponIssue(Long userId) {
        if (userId == null) {
            ErrorStatus errorStatus = ErrorStatus.from("유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }
        userAndCouponService.createUserWelcomeCouponIssue(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }





    @GetMapping("/users/user")
    public ResponseEntity<Page<UserAndCouponResponseDTO>> getAllUserAndCouponsByUserPaging(@CurrentUser CurrentUserDetails currentUser,@PageableDefault(page = 1, size = 3) Pageable pageable) {
        Long userId= currentUser.getUserId();
        if (userId == null) {
            ErrorStatus errorStatus = ErrorStatus.from( "유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }
        Page<UserAndCouponResponseDTO> coupons = userAndCouponService.getAllUsersAndCouponsByUserPaging(
           userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }


    @GetMapping("/users")
    public ResponseEntity<Page<UserAndCouponResponseDTO>> getAllUsersAndCouponsByManagerPaging(
        @PageableDefault(page = 1, size = 3) Pageable pageable,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Long userId
    ) {
        if (userId == null) {
            ErrorStatus errorStatus = ErrorStatus.from("유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }
        Page<UserAndCouponResponseDTO> coupons = userAndCouponService.getAllUsersAndCouponsByManagerPaging(pageable, type, userId);
        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }



    // 비회원일경우 처리는 여기서... current user 처리해주는 메소드 만들기
    @GetMapping("/users/order")
    public ResponseEntity<List<UserAndCouponResponseDTO>> findCouponByOrder(
        @CurrentUser CurrentUserDetails currentUserDetails,
        @RequestParam(required = false) List<Long> bookIds,
        @RequestParam(required = false) List<Long> categoryIds,
        @RequestParam BigDecimal bookPrice) {

        if (currentUserDetails.getUserId() == null) {
            ErrorStatus errorStatus = ErrorStatus.from("유저 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }

        List<UserAndCouponResponseDTO> coupons = userAndCouponService.findCouponByOrder(currentUserDetails.getUserId(), bookIds, categoryIds, bookPrice);

        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }


    @PatchMapping("/users/payment/{userAndCouponId}")
    public ResponseEntity<Void> updateCouponAfterPayment(
        @PathVariable("userAndCouponId") Long userAndCouponId) {

        if (userAndCouponId == null) {
            ErrorStatus errorStatus = ErrorStatus.from("사용자 쿠폰 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }


         userAndCouponService.updateCouponAfterPayment(userAndCouponId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }



    @PatchMapping("/users/refund/{userAndCouponId}")
    public ResponseEntity<Void> updateCouponAfterRefund(
        @PathVariable("userAndCouponId") Long userAndCouponId) {

        if (userAndCouponId == null) {
            ErrorStatus errorStatus = ErrorStatus.from("사용자 쿠폰 아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }


        userAndCouponService.updateCouponAfterRefund(userAndCouponId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/users/order/coupon")
    public ResponseEntity<UserAndCouponOrderResponseDTO> getSelectedCoupon(
        @RequestParam(value = "couponId", required = false) Long couponId) {
        UserAndCouponOrderResponseDTO coupon = userAndCouponService.findUserAndCouponsById(couponId);
        return ResponseEntity.status(HttpStatus.OK).body(coupon);
    }



    @GetMapping("/users/auth")
    public ResponseEntity<Boolean> isRealUserCheck(
        @CurrentUser CurrentUserDetails currentUserDetails) {
        if(currentUserDetails==null){
            return ResponseEntity.status(HttpStatus.OK).body(false);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

    }




}