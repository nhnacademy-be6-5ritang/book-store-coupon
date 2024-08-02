package com.nhnacademy.bookstorecoupon.global.listener;

import java.time.LocalDateTime;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponTemplateInsufficientQuantity;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



/**
 * @author 이기훈
 * 쿠폰 발급을 처리하는 리스너입니다.
 * 메시지를 수신하여 쿠폰 템플릿을 조회하고, 쿠폰 발급 및 수량 업데이트를 수행합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssuanceListener implements MessageListener {
    private final UserAndCouponRepository userAndCouponRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    /**
     * 메시지를 수신하여 쿠폰 발급을 처리합니다.
     * 메시지에서 쿠폰 발급 정보를 추출하고, 쿠폰 템플릿을 조회하여 수량을 확인합니다.
     * 수량이 충분한 경우, 쿠폰을 발급하고 쿠폰 템플릿의 수량을 업데이트합니다.
     *
     * @param message 수신한 메시지
     */
    @Override
    @Transactional
    public void onMessage(Message message) {
        try {
            Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
            CouponIssuanceMessage issuanceMessage = (CouponIssuanceMessage) jsonMessageConverter.fromMessage(message);

            String errorMessage = String.format("해당 쿠폰템플릿 아이디 '%d'는 존재하지 않습니다.", issuanceMessage.getCouponId());
            ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());

            log.debug("Received message: {}", issuanceMessage);

            CouponTemplate couponTemplate = couponTemplateRepository.findById(issuanceMessage.getCouponId())
                .orElseThrow(() -> new CouponTemplateNotFoundException(errorStatus));


            String errorMessage1 = String.format("해당 쿠폰템플릿 아이디 '%d'의 발급수량이 부족합니다.", issuanceMessage.getCouponId());
            ErrorStatus errorStatus1 = ErrorStatus.from(errorMessage1, HttpStatus.NOT_FOUND, LocalDateTime.now());

            if (couponTemplate.getQuantity() <= 0) {
                throw new CouponTemplateInsufficientQuantity(errorStatus1);
            }



            UserAndCoupon userAndCoupon = UserAndCoupon.builder()
                .couponPolicy(couponTemplate.getCouponPolicy())
                .userId(issuanceMessage.getUserId())
                .isUsed(false)
                .expiredDate(couponTemplate.getExpiredDate())
                .issueDate(couponTemplate.getIssueDate())
                .build();

            userAndCouponRepository.save(userAndCoupon);
            // 수량 업데이트
            couponTemplate.update(couponTemplate.getQuantity() - 1);
            couponTemplateRepository.save(couponTemplate);
        } catch (Exception e) {
            log.error("Failed to process message", e);
            throw new RuntimeException("Failed to process message", e);
        }
    }
}