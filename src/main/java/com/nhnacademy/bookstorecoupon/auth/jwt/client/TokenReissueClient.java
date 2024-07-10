package com.nhnacademy.bookstorecoupon.auth.jwt.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.bookstorecoupon.auth.jwt.dto.request.ReissueTokenRequest;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.response.ReissueTokensResponse;

@FeignClient(name = "token-feign-client", url = "http://localhost:8070")
public interface TokenReissueClient {
	@PostMapping("/auth/reissue-with-refresh-token")
	ResponseEntity<ReissueTokensResponse> reissueTokensWithRefreshToken(
		@RequestBody ReissueTokenRequest reissueTokenRequest);
}
