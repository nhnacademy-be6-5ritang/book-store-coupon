package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponNotFoundException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.global.config.RabbitMQConfig;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;

@Service
public class RabbitMQUserAndCouponService {

    private final RabbitTemplate rabbitTemplate;
    private final CouponTemplateRepository couponTemplateRepository;

    public RabbitMQUserAndCouponService(RabbitTemplate rabbitTemplate, CouponTemplateRepository couponTemplateRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.couponTemplateRepository = couponTemplateRepository;
    }

    public void createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO) {
        String errorMessage = String.format("해당 쿠폰은 '%d'는 존재하지 않습니다.", couponId);
        ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());

        couponTemplateRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(errorStatus));

        CouponIssuanceMessage message = new CouponIssuanceMessage(couponId, requestDTO.userId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.COUPON_ISSUE_QUEUE, message);
    }
}
