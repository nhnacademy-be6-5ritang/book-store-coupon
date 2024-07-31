package com.nhnacademy.bookstorecoupon.global.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;
@WebMvcTest(SwaggerController.class)
@AutoConfigureMockMvc(addFilters = false)
class SwaggerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OpenApiResource openApiResource;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetSwaggerJson() throws Exception {
		// Mock Swagger JSON response
		String mockSwaggerJson = "{ \"openapi\": \"3.0.0\" }";
		byte[] mockSwaggerJsonBytes = mockSwaggerJson.getBytes(StandardCharsets.UTF_8);

		// Mock OpenApiResource to return byte[] array
		when(openApiResource.openapiJson(any(HttpServletRequest.class), eq("/coupon-docs/coupon-api"), any(Locale.class)))
			.thenReturn(mockSwaggerJsonBytes);

		// Perform request and validate response
		mockMvc.perform(get("/coupons/api")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(mockSwaggerJson));
	}

	@Test
	void testGetSwaggerJson_whenEmpty() throws Exception {
		// Mock empty Swagger JSON response
		String emptySwaggerJson = "{}";
		byte[] emptySwaggerJsonBytes = emptySwaggerJson.getBytes(StandardCharsets.UTF_8);

		// Mock HttpServletRequest
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getLocale()).thenReturn(Locale.getDefault());

		// Configure mock behavior with HttpServletRequest and Locale
		when(openApiResource.openapiJson(eq(mockRequest), eq("/coupon-docs/coupon-api"), any(Locale.class)))
			.thenReturn(emptySwaggerJsonBytes);

		// Perform request and validate response
		mockMvc.perform(get("/coupons/api")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(emptySwaggerJson));
	}
}