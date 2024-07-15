package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponIssuanceMessage implements Serializable {
    private Long couponId;
    private Long userId;


    public CouponIssuanceMessage(Long couponId, Long userId){
        this.couponId=couponId;
        this.userId=userId;
    }

}
