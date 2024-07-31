package com.nhnacademy.bookstorecoupon.user.domain.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

	private User user;
	private UserRole userRoleAdmin;
	private UserRole userRoleUser;

	@BeforeEach
	void setUp() {
		user = new User();
		userRoleAdmin = mock(UserRole.class);
		userRoleUser = mock(UserRole.class);
	}

	@Test
	void testGetAllRoles() {
		// Arrange
		String adminRoleName = "ADMIN";
		String userRoleName = "USER";
		when(userRoleAdmin.getRoleName()).thenReturn(adminRoleName);
		when(userRoleUser.getRoleName()).thenReturn(userRoleName);

		user.getUserRoles().addAll(Arrays.asList(userRoleAdmin, userRoleUser));

		// Act
		List<String> roles = user.getAllRoles();

		// Assert
		assertEquals(Arrays.asList(adminRoleName, userRoleName), roles);
	}

}