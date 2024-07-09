package com.nhnacademy.bookstorecoupon.user.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

	private Long id;




	private UserStatus status;

	private List<UserRole> userRoles = new ArrayList<>();


	private String password;


	private String email;



	public List<String> getAllRoles() {
		return userRoles.stream()
			.map(UserRole::getRoleName)
			.collect(Collectors.toList());
	}


}
