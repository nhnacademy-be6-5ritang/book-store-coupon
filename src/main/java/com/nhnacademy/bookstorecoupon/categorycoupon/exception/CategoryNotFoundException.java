package com.nhnacademy.bookstorecoupon.categorycoupon.exception;


import com.nhnacademy.bookstorecoupon.global.exception.GlobalException;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends GlobalException {
	public CategoryNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
