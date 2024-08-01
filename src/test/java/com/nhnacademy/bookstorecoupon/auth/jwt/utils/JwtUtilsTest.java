package com.nhnacademy.bookstorecoupon.auth.jwt.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class JwtUtilsTest {

	private JwtUtils jwtUtils;
	private SecretKey secretKey;

	private static final String SECRET = "supersecretkeythatshouldbeatleast32characterslong";

	@BeforeEach
	void setup() {
		secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
		jwtUtils = new JwtUtils(SECRET);
	}

	@Test
	void testGetUserIdFromToken() {
		Long expectedUserId = 123L;

		String token = Jwts.builder()
			.claim("userId", expectedUserId)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();

		Long userId = jwtUtils.getUserIdFromToken("Bearer " + token);
		assertEquals(expectedUserId, userId);
	}

	@Test
	void testGetUserRolesFromToken() {
		List<String> expectedRoles = Collections.singletonList("ROLE_USER");

		String token = Jwts.builder()
			.claim("roles", expectedRoles)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();

		List<String> roles = jwtUtils.getUserRolesFromToken("Bearer " + token);
		assertEquals(expectedRoles, roles);
	}

	@Test
	void testValidateToken_ValidToken() {
		String token = Jwts.builder()
			.claim("userId", 123L)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();

		String errorMessage = jwtUtils.validateToken("Bearer " + token);
		assertNull(errorMessage);
	}

	@Test
	void testValidateToken_ExpiredToken() {
		String token = Jwts.builder()
			.claim("userId", 123L)
			.setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();

		String errorMessage = jwtUtils.validateToken("Bearer " + token);
		assertEquals("만료된 토큰입니다.", errorMessage);
	}

	@Test
	void testValidateToken_InvalidToken() {
		String token = "Bearer invalid_token";

		String errorMessage = jwtUtils.validateToken(token);
		assertEquals("유효하지 않은 토큰입니다.", errorMessage);
	}

	@Test
	void testValidateToken_NullToken() {
		String errorMessage = jwtUtils.validateToken("Bearer ");
		assertEquals("토큰 값이 비어있습니다.", errorMessage);
	}
}