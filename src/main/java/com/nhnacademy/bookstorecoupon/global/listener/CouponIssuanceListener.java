package com.nhnacademy.bookstorecoupon.global.listener;

import java.time.LocalDateTime;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponNotFoundException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.CouponIssuanceMessage;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CouponIssuanceListener implements MessageListener {

    private final UserAndCouponRepository userAndCouponRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    public CouponIssuanceListener(UserAndCouponRepository userAndCouponRepository,
        CouponTemplateRepository couponTemplateRepository
    ) {
        this.userAndCouponRepository = userAndCouponRepository;
        this.couponTemplateRepository = couponTemplateRepository;
    }

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
                .orElseThrow(() -> new CouponNotFoundException(errorStatus));

            UserAndCoupon userAndCoupon = UserAndCoupon.builder()
                .couponPolicy(couponTemplate.getCouponPolicy())
                .userId(issuanceMessage.getUserId())
                .isUsed(false)
                .expiredDate(couponTemplate.getExpiredDate())
                .issueDate(couponTemplate.getIssueDate())
                .build();

            userAndCouponRepository.save(userAndCoupon);
        } catch (Exception e) {
            log.error("Failed to process message", e);
            throw new RuntimeException("Failed to process message", e);
        }
    }
}