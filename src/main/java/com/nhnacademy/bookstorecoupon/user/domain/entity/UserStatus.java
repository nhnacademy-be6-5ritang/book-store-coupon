package com.nhnacademy.bookstorecoupon.user.domain.entity;

import com.nhnacademy.bookstorecoupon.user.domain.dto.request.CreateUserStatusRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserStatus {
	private Long id;
	private String userStatusName;

	@Builder
	public UserStatus(String userStatusName) {
		this.userStatusName = userStatusName;
	}

	public static UserStatus toEntity(CreateUserStatusRequest createUserStatusRequest) {
		return UserStatus.builder()
			.userStatusName(createUserStatusRequest.userStatusName())
			.build();
	}
}
