package com.nhnacademy.bookstorecoupon.coupontemplate.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.service.CouponTemplateService;

@WebMvcTest(CouponTemplateController.class)
class CouponTemplateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CouponTemplateService couponTemplateService;

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testCreateCouponTemplate_Valid() throws Exception {
		CouponTemplateRequestDTO validRequest = new CouponTemplateRequestDTO(
			1L,
			LocalDateTime.now().plusDays(365),
			LocalDateTime.now(),
			100L
		);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validRequest)).with(csrf()))
			.andExpect(status().isCreated());

		verify(couponTemplateService, times(1)).createCouponTemplate(any(CouponTemplateRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testCreateCouponTemplate_InvalidCouponPolicyId() throws Exception {
		CouponTemplateRequestDTO invalidRequest = new CouponTemplateRequestDTO(
			null,
			LocalDateTime.now().plusDays(1),
			LocalDateTime.now(),
			100L
		);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)).with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("couponPolicyId 에러: must not be null, 입력된 값: null; "))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"));
		verify(couponTemplateService, times(0)).createCouponTemplate(any(CouponTemplateRequestDTO.class));

	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testCreateCouponTemplate_InvalidExpiredDate() throws Exception {
		CouponTemplateRequestDTO invalidRequest = new CouponTemplateRequestDTO(
			1L,
			null,
			LocalDateTime.now(),
			100L
		);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)).with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("expiredDate 에러: must not be null, 입력된 값: null; "))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"));

		verify(couponTemplateService, times(0)).createCouponTemplate(any(CouponTemplateRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testCreateCouponTemplate_InvalidIssueDate() throws Exception {
		CouponTemplateRequestDTO invalidRequest = new CouponTemplateRequestDTO(
			1L,
			LocalDateTime.now().plusDays(1),
			null,
			100L
		);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)).with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("issueDate 에러: must not be null, 입력된 값: null; "))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"));

		verify(couponTemplateService, times(0)).createCouponTemplate(any(CouponTemplateRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testCreateCouponTemplate_InvalidQuantity() throws Exception {
		CouponTemplateRequestDTO invalidRequest = new CouponTemplateRequestDTO(
			1L,
			LocalDateTime.now().plusDays(1),
			LocalDateTime.now(),
			null
		);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)).with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("quantity 에러: must not be null, 입력된 값: null; "))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"));

		verify(couponTemplateService, times(0)).createCouponTemplate(any(CouponTemplateRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testCreateCouponTemplate_NegativeQuantity() throws Exception {
		CouponTemplateRequestDTO invalidRequest = new CouponTemplateRequestDTO(
			1L,
			LocalDateTime.now().plusDays(1),
			LocalDateTime.now(),
			-100L
		);

		mockMvc.perform(post("/coupons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)).with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("quantity 에러: must be greater than 0, 입력된 값: -100; "))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"));

		verify(couponTemplateService, times(0)).createCouponTemplate(any(CouponTemplateRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"MEMBER"})
	void testGetAllCouponTemplatesByUserPaging() throws Exception {
		// CouponTemplateResponseDTO 객체 생성
		CouponTemplateResponseDTO coupon1 = new CouponTemplateResponseDTO(
			1L, 1L, new BigDecimal("100.00"), new BigDecimal("10.00"),
			null, null, "sale",
			true, null, null, null, null,
			LocalDateTime.now().plusDays(30), LocalDateTime.now(), 500L
		);

		CouponTemplateResponseDTO coupon2 = new CouponTemplateResponseDTO(
			2L, 2L, new BigDecimal("200.00"), null,
			new BigDecimal("0.15"), null, "book",
			true, 2L, "Another Great Book", null, null,
			LocalDateTime.now().plusDays(60), LocalDateTime.now().minusDays(5), 300L
		);

		CouponTemplateResponseDTO coupon3 = new CouponTemplateResponseDTO(
			3L, 3L, new BigDecimal("300.00"), new BigDecimal("500.00"),
			null, null, "category",
			true, null, null, 3L, "Science",
			LocalDateTime.now().plusDays(90), LocalDateTime.now().minusDays(10), 200L
		);

		Page<CouponTemplateResponseDTO> page = new PageImpl<>(List.of(coupon1, coupon2, coupon3));

		Mockito.when(couponTemplateService.getAllCouponTemplatesByUserPaging(Mockito.any(Pageable.class)))
			.thenReturn(page);

		mockMvc.perform(get("/coupons/issue")
				.param("page", "0")  // 페이지는 0부터 시작하므로 0으로 설정
				.param("size", "3")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content[0].id").value(1))
			.andExpect(jsonPath("$.content[0].couponPolicyId").value(1))
			.andExpect(jsonPath("$.content[0].minOrderPrice").value(100.00))
			.andExpect(jsonPath("$.content[1].id").value(2))
			.andExpect(jsonPath("$.content[1].couponPolicyId").value(2))
			.andExpect(jsonPath("$.content[1].minOrderPrice").value(200.00))
			.andExpect(jsonPath("$.content[2].id").value(3))
			.andExpect(jsonPath("$.content[2].couponPolicyId").value(3))
			.andExpect(jsonPath("$.content[2].minOrderPrice").value(300.00));

		verify(couponTemplateService, times(1)).getAllCouponTemplatesByUserPaging(Mockito.any(Pageable.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})  // 올바른 역할로 설정
	void testGetAllCouponTemplatesByManagerPaging() throws Exception {
		// CouponTemplateResponseDTO 객체 생성
		CouponTemplateResponseDTO coupon1 = new CouponTemplateResponseDTO(
			1L, 1L, new BigDecimal("100.00"), new BigDecimal("10.00"),
			null, null, "sale",
			true, null, null, null, null,
			LocalDateTime.now().plusDays(30), LocalDateTime.now(), 500L
		);

		CouponTemplateResponseDTO coupon2 = new CouponTemplateResponseDTO(
			2L, 2L, new BigDecimal("200.00"), null,
			new BigDecimal("0.15"), null, "book",
			true, 2L, "Another Great Book", null, null,
			LocalDateTime.now().plusDays(60), LocalDateTime.now().minusDays(5), 300L
		);

		CouponTemplateResponseDTO coupon3 = new CouponTemplateResponseDTO(
			3L, 3L, new BigDecimal("300.00"), new BigDecimal("500.00"),
			null, null, "category",
			true, null, null, 3L, "Science",
			LocalDateTime.now().plusDays(90), LocalDateTime.now().minusDays(10), 200L
		);

		Page<CouponTemplateResponseDTO> page = new PageImpl<>(List.of(coupon1, coupon2, coupon3));

		Mockito.when(couponTemplateService.getAllCouponTemplatesByManagerPaging(Mockito.any(Pageable.class)))
			.thenReturn(page);

		mockMvc.perform(get("/coupons")
				.param("page", "0")  // 페이지는 0부터 시작하므로 0으로 설정
				.param("size", "2")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))  // CSRF 토큰을 포함
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content[0].id").value(1))
			.andExpect(jsonPath("$.content[0].couponPolicyId").value(1))
			.andExpect(jsonPath("$.content[0].minOrderPrice").value(100.00))
			.andExpect(jsonPath("$.content[1].id").value(2))
			.andExpect(jsonPath("$.content[1].couponPolicyId").value(2))
			.andExpect(jsonPath("$.content[1].minOrderPrice").value(200.00))
			.andExpect(jsonPath("$.content[2].id").value(3))
			.andExpect(jsonPath("$.content[2].couponPolicyId").value(3))
			.andExpect(jsonPath("$.content[2].minOrderPrice").value(300.00));

		verify(couponTemplateService, times(1)).getAllCouponTemplatesByManagerPaging(Mockito.any(Pageable.class));
	}
}