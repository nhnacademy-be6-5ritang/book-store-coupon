package com.nhnacademy.bookstorecoupon.user.domain.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class UserRoleTest {

    @Test
    void testBuilder() {
        // Arrange
        User user = mock(User.class);
        Role role = mock(Role.class);

        // Act
        UserRole userRole = UserRole.builder()
            .user(user)
            .role(role)
            .build();

        // Assert
        assertEquals(user, userRole.getUser());
        assertEquals(role, userRole.getRole());
    }

    @Test
    void testGetRoleName() {
        // Arrange
        Role role = mock(Role.class);
        String roleName = "ADMIN";
        when(role.getRoleName()).thenReturn(roleName);
        User user = mock(User.class);

        UserRole userRole = UserRole.builder()
            .user(user)
            .role(role)
            .build();

        // Act
        String actualRoleName = userRole.getRoleName();

        // Assert
        assertEquals(roleName, actualRoleName);
    }
}
