package com.nhnacademy.bookstorecoupon.coupon.exception;


import com.nhnacademy.bookstorecoupon.global.exception.GlobalException;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class CouponNotFoundException extends GlobalException {
	public CouponNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
