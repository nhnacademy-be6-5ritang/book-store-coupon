package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

@RestController
@RequestMapping("/coupons")
public class UserAndCouponController {

    private final UserAndCouponService userAndCouponService;

    public UserAndCouponController(UserAndCouponService userAndCouponService) {
        this.userAndCouponService = userAndCouponService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserAndCouponResponseDTO> createUserAndCoupon(@RequestBody UserAndCouponRequestDTO requestDTO) {
        UserAndCouponResponseDTO responseDTO = userAndCouponService.createUserAndCoupon(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserAndCouponResponseDTO> updateUserAndCoupon(@PathVariable("userId") Long id, @RequestBody UserAndCouponRequestDTO requestDTO) {
        UserAndCouponResponseDTO responseDTO = userAndCouponService.updateUserAndCoupon(id, requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    // @GetMapping("/users/{userId}")
    // public ResponseEntity<List<UserAndCouponResponseDTO>> getAllUserAndCoupons(@PathVariable("userId") Long userId) {
    //     List<UserAndCouponResponseDTO> userAndCoupons = userAndCouponService.getAllUserAndCoupons();
    //     return ResponseEntity.status(HttpStatus.OK).body(userAndCoupons);
    // }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserAndCouponResponseDTO> getUserAndCouponById( @PathVariable("userId") Long userId) {
        UserAndCouponResponseDTO userAndCoupon = userAndCouponService.getUserAndCouponById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userAndCoupon);
    }

}