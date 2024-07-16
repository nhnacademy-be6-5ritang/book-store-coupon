package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.UserCouponValidationException;

@Service
public class RabbitMQUserAndCouponService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQUserAndCouponService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createUserAndCoupon(Long couponId, Long userId) {


        if (couponId == null || userId == null) {
            String errorMessage = "쿠폰아이디와 사용자아이디가 필요합니다.";
            ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }

        CouponIssuanceMessage message = new CouponIssuanceMessage(couponId,userId);
        rabbitTemplate.convertAndSend("5ritang.coupon.exchange","5ritang.coupon.key", message);
    }
}

