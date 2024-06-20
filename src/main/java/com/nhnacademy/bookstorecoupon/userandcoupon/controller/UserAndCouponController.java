package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

@RestController
@RequestMapping("/user-and-coupons")
public class UserAndCouponController {

    private final UserAndCouponService userAndCouponService;

    public UserAndCouponController(UserAndCouponService userAndCouponService) {
        this.userAndCouponService = userAndCouponService;
    }

    @PostMapping
    public ResponseEntity<UserAndCouponResponseDTO> createUserAndCoupon(@RequestBody UserAndCouponRequestDTO requestDTO) {
       userAndCouponService.createUserAndCoupon(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAndCouponResponseDTO> updateUserAndCoupon(@PathVariable Long id, @RequestBody UserAndCouponRequestDTO requestDTO) {
        userAndCouponService.updateUserAndCoupon(id, requestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<UserAndCouponResponseDTO>> getAllUserAndCoupons() {
        List<UserAndCouponResponseDTO> userAndCoupons = userAndCouponService.getAllUserAndCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(userAndCoupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAndCouponResponseDTO> getUserAndCouponById(@PathVariable Long id) {
        UserAndCouponResponseDTO userAndCoupon = userAndCouponService.getUserAndCouponById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userAndCoupon);
    }

}