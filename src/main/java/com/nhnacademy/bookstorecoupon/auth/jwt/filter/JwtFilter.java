package com.nhnacademy.bookstorecoupon.auth.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nhnacademy.bookstorecoupon.auth.jwt.client.TokenReissueClient;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.request.ReissueTokenRequest;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.response.ReissueTokensResponse;
import com.nhnacademy.bookstorecoupon.auth.jwt.utils.JwtUtils;
import com.nhnacademy.bookstorecoupon.user.domain.dto.response.UserTokenInfo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;
	private final TokenReissueClient tokenReissueClient;
	private final Long accessTokenExpiresIn;
	private final Long refreshTokenExpiresIn;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = request.getHeader("Authorization");
		String refreshToken = request.getHeader("Refresh-Token");

		if (Objects.isNull(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}

		accessToken = URLDecoder.decode(accessToken, StandardCharsets.UTF_8);
		refreshToken = URLDecoder.decode(refreshToken, StandardCharsets.UTF_8);

		String accessTokenErrorMessage = jwtUtils.validateToken(accessToken);
		if ("만료된 토큰입니다.".equals(accessTokenErrorMessage)) {
			ResponseEntity<ReissueTokensResponse> reissueTokensResponse
				= tokenReissueClient.reissueTokensWithRefreshToken(
				ReissueTokenRequest.builder()
					.refreshToken(refreshToken)
					.build()
			);

			if (!reissueTokensResponse.getStatusCode().is2xxSuccessful()) {
				PrintWriter writer = response.getWriter();
				writer.print("토큰 재발급에 실패했습니다. 다시 로그인해주세요.");
				writer.flush();
				writer.close();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			accessToken = Objects.requireNonNull(reissueTokensResponse.getBody()).accessToken();
			refreshToken = reissueTokensResponse.getBody().refreshToken();

			String requestUri = request.getRequestURI();
			if (!"/api/users/withdraw".equals(requestUri)) {
				response.setHeader("New-Authorization", URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
				response.setHeader("New-Refresh-Token", URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
			}
		} else if (Objects.nonNull(accessTokenErrorMessage)) {
			PrintWriter writer = response.getWriter();
			writer.print(accessTokenErrorMessage);
			writer.flush();
			writer.close();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		Long userId = jwtUtils.getUserIdFromToken(accessToken);
		List<String> userRoles = jwtUtils.getUserRolesFromToken(accessToken);

		UserTokenInfo user = UserTokenInfo.builder()
			.id(userId)
			.roles(userRoles)
			.build();
		CurrentUserDetails userDetails = new CurrentUserDetails(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	@Override
	@NonNull
	protected String getAlreadyFilteredAttributeName() {
		return this.getClass().getName() + ".FILTERED";
	}
}
