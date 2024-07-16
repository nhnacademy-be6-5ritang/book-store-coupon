package com.nhnacademy.bookstorecoupon.coupontemplate.exception;


import com.nhnacademy.bookstorecoupon.global.exception.GlobalException;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class CouponTemplateNotFoundException extends GlobalException {
	public CouponTemplateNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
