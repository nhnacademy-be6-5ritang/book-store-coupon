package com.nhnacademy.bookstorecoupon.bookcoupon.exception;


import com.nhnacademy.bookstorecoupon.global.exception.GlobalException;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class BookNotFoundException extends GlobalException {
	public BookNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
