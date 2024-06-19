package com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCouponDTO {
    private Long couponId;
    private String policyType;
    private BigDecimal minOrderPrice;
    private BigDecimal salePrice;
    private BigDecimal saleRate;
    private BigDecimal maxSalePrice;
    private Date issueDate;
    private Date expiredDate;
    private Boolean isUsed;
    private Date usedDate;
}
