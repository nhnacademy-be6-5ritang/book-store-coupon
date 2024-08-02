package com.nhnacademy.bookstorecoupon.auth.jwt.utils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 김태환
 * JWT 유틸리티 클래스입니다.
 */
@Slf4j
@Component
public class JwtUtils {
	private final SecretKey secretKey;

	/**
	 * 주어진 비밀 키를 사용하여 {@link SecretKey} 객체를 초기화합니다.
	 *
	 * @param secret 비밀 키 문자열.
	 */
	public JwtUtils(@Value("${spring.jwt.secret}") String secret) {
		secretKey = new SecretKeySpec(
			secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()
		);
	}

	/**
	 * 주어진 토큰에서 claims 를 추출합니다.
	 *
	 * @param token JWT 토큰.
	 * @return {@link Claims} 객체.
	 */
	private Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token.replace("Bearer ", ""))
			.getPayload();
	}

	/**
	 * JWT 토큰에서 사용자 ID를 추출합니다.
	 *
	 * @param token JWT 토큰.
	 * @return 사용자 ID.
	 */
	public Long getUserIdFromToken(String token) {
		return getClaims(token).get("userId", Long.class);
	}

	/**
	 * JWT 토큰에서 사용자 역할을 추출합니다.
	 *
	 * @param token JWT 토큰.
	 * @return 사용자 역할 목록.
	 */
	public List<String> getUserRolesFromToken(String token) {
		Claims claims = getClaims(token);
		return ((List<?>)claims.get("roles")).stream()
			.map(Object::toString)
			.toList();
	}

	/**
	 * JWT 토큰의 유효성을 검사합니다.
	 *
	 * @param token JWT 토큰.
	 * @return 유효성 검사 결과에 대한 오류 메시지 또는 null (토큰이 유효한 경우).
	 */
	public String validateToken(String token) {
		String errorMessage = null;
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(
					token.replace("Bearer ", ""));
		} catch (SecurityException | MalformedJwtException e) {
			errorMessage = "유효하지 않은 토큰입니다.";
			log.info(errorMessage, e);
		} catch (ExpiredJwtException e) {
			errorMessage = "만료된 토큰입니다.";
			log.info(errorMessage, e);
		} catch (UnsupportedJwtException e) {
			errorMessage = "지원하지 않는 토큰입니다.";
			log.info(errorMessage, e);
		} catch (IllegalArgumentException e) {
			errorMessage = "토큰 값이 비어있습니다.";
			log.info(errorMessage, e);
		}
		return errorMessage;
	}
}
