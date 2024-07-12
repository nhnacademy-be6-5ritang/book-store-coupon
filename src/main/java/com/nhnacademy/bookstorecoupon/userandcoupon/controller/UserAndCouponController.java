package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

import java.math.BigDecimal;
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
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

@RestController
@RequestMapping("/coupons")
public class UserAndCouponController {

    private final UserAndCouponService userAndCouponService;

    public UserAndCouponController(UserAndCouponService userAndCouponService) {
        this.userAndCouponService = userAndCouponService;
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<Void> createUserAndCoupon(@PathVariable("couponId") Long couponId, @CurrentUser CurrentUserDetails currentUser) {
        Long userId= currentUser.getUserId();
       userAndCouponService.createUserAndCoupon(couponId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @PostMapping("/coupon/welcome")
    public ResponseEntity<Void> createUserWelcomeCouponIssue(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        userAndCouponService.createUserWelcomeCouponIssue(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }





    @GetMapping("/users/user")
    public ResponseEntity<Page<UserAndCouponResponseDTO>> getAllUserAndCouponsByUserPaging(@CurrentUser CurrentUserDetails currentUser,@PageableDefault(page = 1, size = 3) Pageable pageable) {
        Long userId= currentUser.getUserId();
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
        Page<UserAndCouponResponseDTO> coupons = userAndCouponService.getAllUsersAndCouponsByManagerPaging(pageable, type, userId);
        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }




    @GetMapping("/users/order")
    public ResponseEntity<List<UserAndCouponResponseDTO>> findCouponByOrder(
        @CurrentUser CurrentUserDetails currentUserDetails,
        @RequestParam(required = false) List<Long> bookIds,
        @RequestParam(required = false) List<Long> categoryIds,
        @RequestParam BigDecimal bookPrice) {


        List<UserAndCouponResponseDTO> coupons = userAndCouponService.findCouponByOrder(currentUserDetails.getUserId(), bookIds, categoryIds, bookPrice);

        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }


    @PatchMapping("/users/payment/{userAndCouponId}")
    public ResponseEntity<Void> updateCouponAfterPayment(
        @PathVariable("userAndCouponId") Long userAndCouponId) {


         userAndCouponService.updateCouponAfterPayment(userAndCouponId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/users/order/coupon")
    public ResponseEntity<UserAndCouponOrderResponseDTO> getSelectedCoupon(
        @RequestParam(value = "couponId", required = false) Long couponId) {
        UserAndCouponOrderResponseDTO coupon = userAndCouponService.findUserAndCouponsById(couponId);
        return ResponseEntity.status(HttpStatus.OK).body(coupon);
    }



}