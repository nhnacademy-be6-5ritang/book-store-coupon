package com.nhnacademy.bookstorecoupon.auth.jwt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;

import lombok.RequiredArgsConstructor;

// authenticated test를 위한 서비스
@Service
@RequiredArgsConstructor
public class TokenService {
	public Map<String, Object> getUserInfo(CurrentUserDetails currentUser) {
		Long id = currentUser.getUserId();
		List<String> roles = currentUser.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());

		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("id", id);
		userInfo.put("roles", roles);

		return userInfo;
	}
}
