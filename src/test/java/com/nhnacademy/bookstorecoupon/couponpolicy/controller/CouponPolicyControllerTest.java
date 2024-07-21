package com.nhnacademy.bookstorecoupon.couponpolicy.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.Test;
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
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyValidationException;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.CouponPolicyService;

/**
 * @author 이기훈
 * couponPolicyController 단위테스트
 */
@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CouponPolicyService couponPolicyService;



	@Autowired
	private ObjectMapper objectMapper;



	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueWelcomeCoupon_Success() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"WELCOME",                // type
			null,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/welcome")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))

			.andExpect(status().isCreated());

		verify(couponPolicyService, times(1)).issueWelcomeCoupon(any(CouponPolicyRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueWelcomeCoupon_InvalidRequest() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			null,                     // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"WELCOME",                // type
			null,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/welcome")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueBirthdayCoupon_Success() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(2000), // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"BIRTHDAY",               // type
			null,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/birthday")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isCreated());

		verify(couponPolicyService, times(1)).issueBirthdayCoupon(any(CouponPolicyRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueSpecificBookCoupon_Success() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"BOOK",                   // type
			1L,                       // bookId
			"Book Title",                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isCreated());

		verify(couponPolicyService, times(1)).issueSpecificBookCoupon(any(CouponPolicyRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueSpecificBookCoupon_BookIdOrTitleMissing() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"BOOK",                   // type
			1L,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueSpecificCategoryCoupon_Success() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"CATEGORY",               // type
			null,                     // bookId
			null,                     // bookTitle
			1L,                       // categoryId
			"Category Name"                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isCreated());

		verify(couponPolicyService, times(1)).issueSpecificCategoryCoupon(any(CouponPolicyRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueSpecificCategoryCoupon_CategoryIdOrNameMissing() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"CATEGORY",               // type
			null,                     // bookId
			null,                     // bookTitle
			1L,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueDiscountCoupon_Success() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(5000), // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"sale",               // type
			null,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/sale")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isCreated());

		verify(couponPolicyService, times(1)).issueDiscountCoupon(any(CouponPolicyRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testGetAllCouponPolicies_Success() throws Exception {
		CouponPolicyResponseDTO responseDTO = new CouponPolicyResponseDTO(
			1L,
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(5000), // salePrice
			null,                     // saleRate
			null,                     // maxSalePrice
			"sale",// type
			true,
			null,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null
		);
		Page<CouponPolicyResponseDTO> page = new PageImpl<>(Collections.singletonList(responseDTO));

		when(couponPolicyService.getAllCouponPolicies(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/coupons/policies")
				.contentType(MediaType.APPLICATION_JSON).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0]").isNotEmpty());

		verify(couponPolicyService, times(1)).getAllCouponPolicies(any(Pageable.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testUpdateCouponPolicy_Success() throws Exception {
		CouponPolicyUpdateRequestDTO requestDTO = new CouponPolicyUpdateRequestDTO(
			BigDecimal.valueOf(3000),
			BigDecimal.valueOf(2000),
			null,
			null,
			true
		);

		mockMvc.perform(patch("/coupons/policies/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isOk());

		verify(couponPolicyService, times(1)).updateCouponPolicy(anyLong(), any(CouponPolicyUpdateRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testUpdateCouponPolicy_InvalidRequest() throws Exception {
		CouponPolicyUpdateRequestDTO requestDTO = new CouponPolicyUpdateRequestDTO(
			null,
			BigDecimal.valueOf(2000),
			null,
			null,
			true
		);


		mockMvc.perform(patch("/coupons/policies/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)).with(csrf()))
			.andExpect(status().isBadRequest());

		verify(couponPolicyService, times(0)).updateCouponPolicy(anyLong(), any(CouponPolicyUpdateRequestDTO.class));
	}

	@Test
	@WithMockUser(roles = {"COUPON_ADMIN"})
	void testIssueCoupon_SalePriceAndSaleRateInvalid() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			BigDecimal.valueOf(0.22), // saleRate
			null,                     // maxSalePrice
			"welcome",                // type
			null,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/welcome")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> {
				Throwable exception = result.getResolvedException();
				assertTrue("Expected CouponPolicyValidationException but got: " + exception,
					exception instanceof CouponPolicyValidationException);

				CouponPolicyValidationException ex = null;
				if (exception instanceof CouponPolicyValidationException) {
					ex = (CouponPolicyValidationException) exception;
				}

				String actualMessage = null;
				if (ex != null) {
					actualMessage = ex.getErrorStatus().getMessage();
				}
				String expectedMessage = "해당 할인률, 최대가격과 할인가격은 동시에 작성할 수 없습니다";

				System.out.println("Actual message: " + actualMessage); // 로그로 확인

				assertTrue("메시지 체크", actualMessage.equals(expectedMessage));
			});
	}

	@Test
	void testIssueCoupon_SalePriceAndMaxSalePriceInvalid() throws Exception {
		CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			BigDecimal.valueOf(1000), // maxSalePrice
			"welcome",                // type
			null,                     // bookId
			null,                     // bookTitle
			null,                     // categoryId
			null                      // categoryName
		);

		mockMvc.perform(post("/coupons/policies/welcome")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> {
				Throwable exception = result.getResolvedException();
				assertTrue("Expected CouponPolicyValidationException but got: " + exception,
					exception instanceof CouponPolicyValidationException);

				CouponPolicyValidationException ex = null;
				if (exception instanceof CouponPolicyValidationException) {
					ex = (CouponPolicyValidationException) exception;
				}

				String actualMessage = null;
				if (ex != null) {
					actualMessage = ex.getErrorStatus().getMessage();
				}
				String expectedMessage = "해당 할인률, 최대가격과 할인가격은 동시에 작성할 수 없습니다";

				System.out.println("Actual message: " + actualMessage); // 로그로 확인

				if (actualMessage != null) {
					assertTrue("메시지 체크", actualMessage.equals(expectedMessage));
				}
			});
	}


	@Test
	void testUpdateCoupon_SalePriceAndMaxSalePriceSet() throws Exception {
		CouponPolicyUpdateRequestDTO requestDTO = new CouponPolicyUpdateRequestDTO(
			BigDecimal.valueOf(1000), // minOrderPrice
			BigDecimal.valueOf(500),  // salePrice
			null,                     // saleRate
			BigDecimal.valueOf(1000),
			true
		);

		mockMvc.perform(patch("/coupons/policies/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> {
				Throwable exception = result.getResolvedException();
				assertTrue("Expected CouponPolicyValidationException but got: " + exception,
					exception instanceof CouponPolicyValidationException);

				CouponPolicyValidationException ex = null;
				if (exception instanceof CouponPolicyValidationException) {
					ex = (CouponPolicyValidationException) exception;
				}

				String actualMessage = null;
				if (ex != null) {
					actualMessage = ex.getErrorStatus().getMessage();
				}
				String expectedMessage = "해당 할인률, 최대가격과 할인가격은 동시에 작성할 수 없습니다";

				System.out.println("Actual message: " + actualMessage); // 로그로 확인

				if (actualMessage != null) {
					assertTrue("메시지 체크", actualMessage.equals(expectedMessage));
				}
			});
	}




}
