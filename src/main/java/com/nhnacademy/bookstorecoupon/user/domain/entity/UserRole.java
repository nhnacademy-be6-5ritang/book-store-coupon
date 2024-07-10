package com.nhnacademy.bookstorecoupon.user.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRole {

	private Long id;


	private User user;

	private Role role;

	@Builder
	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
	}

	public String getRoleName() {
		return role.getRoleName();
	}
}
