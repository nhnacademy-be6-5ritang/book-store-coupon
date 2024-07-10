package com.nhnacademy.bookstorecoupon.user.domain.dto.request;

import lombok.Builder;

@Builder
public record CreateUserStatusRequest(
	String userStatusName
) {
}
