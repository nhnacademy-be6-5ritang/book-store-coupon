package com.nhnacademy.bookstorecoupon.user.domain.dto.response;

import java.util.List;

import com.nhnacademy.bookstorecoupon.user.domain.entity.User;

import lombok.Builder;

@Builder
public record UserTokenInfo(
	Long id,
	String password,
	List<String> roles,
	String status
) {
	public static UserTokenInfo fromEntity(User user) {
		return UserTokenInfo.builder()
			.id(user.getId())
			.password(user.getPassword())
			.roles(user.getAllRoles())
			.status(user.getStatus().getUserStatusName())
			.build();
	}
}

