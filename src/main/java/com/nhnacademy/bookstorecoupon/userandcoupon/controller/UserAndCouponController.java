package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

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

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
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
    public ResponseEntity<Void> createUserAndCoupon(@PathVariable("couponId") Long couponId, @RequestBody UserAndCouponCreateRequestDTO requestDTO) {
       userAndCouponService.createUserAndCoupon(couponId ,requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<Void> updateUserAndCoupon(@PathVariable("userId") Long userId) {
        userAndCouponService.updateUserAndCoupon(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<UserAndCouponResponseDTO>> getAllUserAndCouponsByUserPaging(@PathVariable("userId") Long userId,@PageableDefault(page = 1, size = 3) Pageable pageable) {
        Page<UserAndCouponResponseDTO> coupons = userAndCouponService.getAllUsersAndCouponsByUserPaging(userId, pageable);
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


}