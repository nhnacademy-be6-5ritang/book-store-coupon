package com.nhnacademy.bookstorecoupon.user.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstorecoupon.user.domain.dto.request.CreateUserStatusRequest;

class UserStatusTest {

    @Test
    void testBuilder() {
        // Arrange
        String userStatusName = "ACTIVE";

        // Act
        UserStatus userStatus = UserStatus.builder()
            .userStatusName(userStatusName)
            .build();

        // Assert
        assertEquals(userStatusName, userStatus.getUserStatusName());
    }

    @Test
    void testToEntity() {
        // Arrange
        String userStatusName = "INACTIVE";
        CreateUserStatusRequest request = new CreateUserStatusRequest(userStatusName);

        // Act
        UserStatus userStatus = UserStatus.toEntity(request);

        // Assert
        assertEquals(userStatusName, userStatus.getUserStatusName());
    }
}
