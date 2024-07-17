package com.nhnacademy.bookstorecoupon.userandcoupon.exception;


import com.nhnacademy.bookstorecoupon.global.exception.GlobalException;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class NotFoundUserAndCouponException extends GlobalException {
	public NotFoundUserAndCouponException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
