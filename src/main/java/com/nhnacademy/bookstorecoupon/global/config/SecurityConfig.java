package com.nhnacademy.bookstorecoupon.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nhnacademy.bookstorecoupon.auth.jwt.client.TokenReissueClient;
import com.nhnacademy.bookstorecoupon.auth.jwt.filter.JwtFilter;
import com.nhnacademy.bookstorecoupon.auth.jwt.utils.JwtUtils;
import com.nhnacademy.bookstorecoupon.global.filter.IpAddressFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtUtils jwtUtils;
	private final IpAddressFilter ipAddressFilter;
	private final TokenReissueClient tokenReissueClient;

	@Value("${spring.jwt.access-token.expires-in}")
	private Long accessTokenExpiresIn;
	@Value("${spring.jwt.refresh-token.expires-in}")
	private Long refreshTokenExpiresIn;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((requests) -> requests
				// .requestMatchers("/api/sign-up").permitAll()
				// .requestMatchers("/api/auth/info").authenticated()
				// .requestMatchers("/api/internal/users/info").permitAll()
				// .requestMatchers("/api/admin").hasRole("ADMIN")
				.anyRequest().permitAll()
			)
			.addFilterBefore(ipAddressFilter, UsernamePasswordAuthenticationFilter.class)
			// .addFilterAfter(new JwtFilter(jwtUtils, tokenReissueClient), IpAddressFilter.class)
			.addFilterBefore(new JwtFilter(jwtUtils, tokenReissueClient, accessTokenExpiresIn, refreshTokenExpiresIn),
				UsernamePasswordAuthenticationFilter.class)
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
