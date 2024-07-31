package com.nhnacademy.bookstorecoupon.auth.jwt.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.bookstorecoupon.auth.jwt.client.TokenReissueClient;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.request.ReissueTokenRequest;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.response.ReissueTokensResponse;
import com.nhnacademy.bookstorecoupon.auth.jwt.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtFilterTest {


	@InjectMocks
	private JwtFilter jwtFilter;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private TokenReissueClient tokenReissueClient;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private PrintWriter writer;  // Add a mock for PrintWriter

	private static final String VALID_ACCESS_TOKEN = "valid-access-token";
	private static final String EXPIRED_ACCESS_TOKEN = "expired-access-token";
	private static final String VALID_REFRESH_TOKEN = "valid-refresh-token";
	private static final String NEW_ACCESS_TOKEN = "new-access-token";
	private static final String NEW_REFRESH_TOKEN = "new-refresh-token";

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		try {
			when(response.getWriter()).thenReturn(writer);  // Mock PrintWriter
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testDoFilterInternal_ValidToken() throws Exception {
		when(request.getHeader("Authorization")).thenReturn(VALID_ACCESS_TOKEN);
		when(request.getHeader("Refresh-Token")).thenReturn(VALID_REFRESH_TOKEN);
		when(jwtUtils.validateToken(VALID_ACCESS_TOKEN)).thenReturn(null);
		when(jwtUtils.getUserIdFromToken(VALID_ACCESS_TOKEN)).thenReturn(123L);
		when(jwtUtils.getUserRolesFromToken(VALID_ACCESS_TOKEN)).thenReturn(Collections.singletonList("ROLE_USER"));

		jwtFilter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verifyNoMoreInteractions(response);
	}

	@Test
	void testDoFilterInternal_ExpiredToken() throws Exception {
		when(request.getHeader("Authorization")).thenReturn(EXPIRED_ACCESS_TOKEN);
		when(request.getHeader("Refresh-Token")).thenReturn(VALID_REFRESH_TOKEN);
		when(jwtUtils.validateToken(EXPIRED_ACCESS_TOKEN)).thenReturn("만료된 토큰입니다.");
		when(tokenReissueClient.reissueTokensWithRefreshToken(any(ReissueTokenRequest.class)))
			.thenReturn(ResponseEntity.ok(new ReissueTokensResponse(NEW_ACCESS_TOKEN, NEW_REFRESH_TOKEN)));

		jwtFilter.doFilterInternal(request, response, filterChain);

		ArgumentCaptor<String> authHeaderCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> refreshHeaderCaptor = ArgumentCaptor.forClass(String.class);
		verify(response).setHeader(eq("New-Authorization"), authHeaderCaptor.capture());
		verify(response).setHeader(eq("New-Refresh-Token"), refreshHeaderCaptor.capture());

		assertEquals(NEW_ACCESS_TOKEN, authHeaderCaptor.getValue());
		assertEquals(NEW_REFRESH_TOKEN, refreshHeaderCaptor.getValue());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	void testDoFilterInternal_TokenReissueFailure() throws Exception {
		when(request.getHeader("Authorization")).thenReturn(EXPIRED_ACCESS_TOKEN);
		when(request.getHeader("Refresh-Token")).thenReturn(VALID_REFRESH_TOKEN);
		when(jwtUtils.validateToken(EXPIRED_ACCESS_TOKEN)).thenReturn("만료된 토큰입니다.");
		when(tokenReissueClient.reissueTokensWithRefreshToken(any(ReissueTokenRequest.class)))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));

		jwtFilter.doFilterInternal(request, response, filterChain);

		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(writer).print("토큰 재발급에 실패했습니다. 다시 로그인해주세요.");  // Mock PrintWriter behavior
		verifyNoMoreInteractions(filterChain);
	}

	@Test
	void testDoFilterInternal_InvalidToken() throws Exception {
		when(request.getHeader("Authorization")).thenReturn(VALID_ACCESS_TOKEN);
		when(request.getHeader("Refresh-Token")).thenReturn(VALID_REFRESH_TOKEN);
		when(jwtUtils.validateToken(VALID_ACCESS_TOKEN)).thenReturn("유효하지 않은 토큰입니다.");

		jwtFilter.doFilterInternal(request, response, filterChain);

		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(writer).print("유효하지 않은 토큰입니다.");  // Mock PrintWriter behavior
		verifyNoMoreInteractions(filterChain);
	}

	@Test
	void testDoFilterInternal_NoTokenProvided() throws Exception {
		when(request.getHeader("Authorization")).thenReturn(null);

		jwtFilter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verifyNoMoreInteractions(response);
	}

}