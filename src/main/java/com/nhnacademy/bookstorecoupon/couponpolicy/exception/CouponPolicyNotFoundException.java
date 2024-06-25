package com.nhnacademy.bookstorecoupon.couponpolicy.exception;


import com.nhnacademy.bookstorecoupon.global.exception.GlobalException;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class CouponPolicyNotFoundException extends GlobalException {
	public CouponPolicyNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
