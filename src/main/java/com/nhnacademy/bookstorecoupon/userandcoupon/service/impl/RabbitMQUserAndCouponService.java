package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.UserCouponValidationException;

import lombok.RequiredArgsConstructor;

/**
 * @author 이기훈
 * 사용자와 쿠폰 관련 작업을 처리하기 위한 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class RabbitMQUserAndCouponService {
    private final RabbitTemplate rabbitTemplate;

    /**
     * 사용자가 쿠폰을 발급받도록 RabbitMQ에 메시지를 전송합니다.
     *
     * @param couponId 쿠폰의 ID. {@code null}일 수 없습니다.
     * @param userId 사용자 ID. {@code null}일 수 없습니다.
     *
     * @throws UserCouponValidationException 쿠폰 ID나 사용자 ID가 {@code null}일 경우 발생하는 예외.
     */
    public void createUserAndCoupon(Long couponId, Long userId) {
        if (couponId == null || userId == null) {
            ErrorStatus errorStatus = ErrorStatus.from( "쿠폰아이디와 사용자아이디가 필요합니다.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            throw new UserCouponValidationException(errorStatus);
        }

        CouponIssuanceMessage message = new CouponIssuanceMessage(couponId,userId);
        rabbitTemplate.convertAndSend("5ritang.coupon.exchange","5ritang.coupon.key", message);
    }
}

