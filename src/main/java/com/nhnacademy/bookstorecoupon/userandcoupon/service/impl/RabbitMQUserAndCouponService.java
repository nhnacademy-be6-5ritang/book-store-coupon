package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;

@Service
public class RabbitMQUserAndCouponService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQUserAndCouponService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createUserAndCoupon(Long couponId, Long userId) {




        CouponIssuanceMessage message = new CouponIssuanceMessage(couponId,userId);
        rabbitTemplate.convertAndSend("5ritang.coupon.exchange","5ritang.coupon.key", message);
    }
}
