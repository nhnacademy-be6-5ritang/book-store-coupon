package com.nhnacademy.bookstorecoupon.auth.jwt.dto.request;

import lombok.Builder;

@Builder
public record ReissueTokenRequest(
	String refreshToken
) {
}
