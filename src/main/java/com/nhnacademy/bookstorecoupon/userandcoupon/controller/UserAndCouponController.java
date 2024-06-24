package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<UserAndCouponResponseDTO> createUserAndCoupon(@PathVariable("couponId") Long couponId, @RequestBody UserAndCouponCreateRequestDTO requestDTO) {
        UserAndCouponResponseDTO responseDTO = userAndCouponService.createUserAndCoupon(couponId ,requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserAndCouponResponseDTO> updateUserAndCoupon(@PathVariable("userId") String userEmail) {
        UserAndCouponResponseDTO responseDTO = userAndCouponService.updateUserAndCoupon(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserAndCouponResponseDTO>> getAllUserAndCoupons() {
        List<UserAndCouponResponseDTO> userAndCoupons = userAndCouponService.getAllUserAndCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(userAndCoupons);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<UserAndCouponResponseDTO>> getUserAndCouponById( @PathVariable("userId") String userEmail) {
        List<UserAndCouponResponseDTO> userAndCoupon = userAndCouponService.getUserAndCouponById(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(userAndCoupon);
    }

}