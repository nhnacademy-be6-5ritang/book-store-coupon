package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstorecoupon.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.impl.RabbitMQUserAndCouponService;

@WebMvcTest(UserAndCouponController.class)
class UserAndCouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserAndCouponService userAndCouponService;

	@MockBean
	private RabbitMQUserAndCouponService rabbitMQUserAndCouponService;

	private CurrentUserDetails currentMember;
	private CurrentUserDetails currentAdmin;

	@Autowired
	private ObjectMapper objectMapper;



	private UserAndCouponResponseDTO dto1;
	private UserAndCouponResponseDTO dto2;
	private UserAndCouponResponseDTO dto3;
	private UserAndCouponResponseDTO dto4;

	private GetBookByOrderCouponResponse bookDetail1;
	private GetBookByOrderCouponResponse bookDetail2;

	private UserAndCouponOrderResponseDTO dto5;

	@BeforeEach
	void setUp() {
		// UserTokenInfo 및 CurrentUserDetails 초기화
		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("ROLE_MEMBER"))
			.status("ACTIVE")
			.build();

		currentMember = new CurrentUserDetails(userTokenInfo);



		UserTokenInfo userTokenInfo2 = UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("ROLE_COUPON_ADMIN"))
			.status("ACTIVE")
			.build();

		currentAdmin = new CurrentUserDetails(userTokenInfo2);


		// 공통적으로 사용할 DTO 객체 초기화
		dto1 = new UserAndCouponResponseDTO(
			1L, 1L, null, true, LocalDateTime.now().plusDays(10),
			LocalDateTime.now().minusDays(20), BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), null,
			null, "sale", true, null, null,
			null, null
		);

		dto2 = new UserAndCouponResponseDTO(
			2L, 1L, null, false, LocalDateTime.now().plusDays(10),
			LocalDateTime.now().minusDays(20), BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), null,
			null, "sale", true, null, null,
			null, null
		);
		dto3 = new UserAndCouponResponseDTO(
			3L, 1L, null, true, LocalDateTime.now().plusDays(10),
			LocalDateTime.now().minusDays(20), BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), null,
			null, "book", true, 1L, "츠츠츠츠",
			null, null
		);

		dto4 = new UserAndCouponResponseDTO(
			4L, 1L, null, false, LocalDateTime.now().plusDays(10),
			LocalDateTime.now().minusDays(20), BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), null,
			null, "category", true, null, null,
			1L, "유아"
		);

		dto5 = new UserAndCouponOrderResponseDTO(
			1L, BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), null, null, "sale"
		);

		bookDetail1 = new GetBookByOrderCouponResponse(
			3L, BigDecimal.valueOf(20000), List.of(1L)
		);

		bookDetail2 = new GetBookByOrderCouponResponse(
			4L, BigDecimal.valueOf(30000), List.of(1L)
		);


	}
	@Test
	@WithMockUser(roles = "MEMBER")
	void testCreateUserAndCoupon() throws Exception {
		Long couponId = 1L;

		// Arrange - Set up the mock behavior
		// You can use `Mockito` to mock behaviors here if needed
		doNothing().when(rabbitMQUserAndCouponService).createUserAndCoupon(couponId, 1L);

		// Act - Perform the request
		mockMvc.perform(post("/coupons/{couponId}", couponId)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer dummyToken")
				.with(csrf())
				.with(user(currentMember)))
			.andExpect(status().isCreated());

		// Assert - Verify interactions with mocks
		verify(rabbitMQUserAndCouponService, times(1)).createUserAndCoupon(couponId, 1L);
	}

	@Test
	@WithMockUser(roles = "COUPON_ADMIN")
	void testCreateUserWelcomeCouponIssue() throws Exception {
		Long userId = 1L;

		mockMvc.perform(post("/coupons/coupon/welcome")
				.param("userId", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userId))
			.with(csrf()))
			.andExpect(status().isCreated());

		verify(userAndCouponService, times(1)).createUserWelcomeCouponIssue(userId);
	}


	@Test
	@WithMockUser(roles = "COUPON_ADMIN")
	void testFailCreateUserWelcomeCouponIssue() throws Exception {
		// Arrange - Setting up the test without userId
		mockMvc.perform(post("/coupons/coupon/welcome")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
			.andExpect(status().isBadRequest()) // Expecting 400 Bad Request
			.andExpect(jsonPath("$.message").value("유저 아이디가 필요합니다.")) // Check the error message
			.andExpect(jsonPath("$.status").value("BAD_REQUEST")); // Check the error status


		verify(userAndCouponService, times(0)).createUserWelcomeCouponIssue(null);
	}


	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetAllUserAndCouponsByUserPaging() throws Exception {



		Page<UserAndCouponResponseDTO> dtoPage = new PageImpl<>(List.of(dto1, dto2));


		// Arrange
		Long userId = 1L;
		Pageable pageable = PageRequest.of(1, 2);
		when(userAndCouponService.getAllUsersAndCouponsByUserPaging(userId, pageable)).thenReturn(dtoPage);


		// Act & Assert
		mockMvc.perform(get("/coupons/users/user")
				.param("page", "1")
				.param("size", "2")
				.header("Authorization", "Bearer dummyToken")
				.with(csrf())
			.with(user(currentMember)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(1))
			.andExpect(jsonPath("$.content[0].userId").value(1))
			.andExpect(jsonPath("$.content[1].id").value(2))
			.andExpect(jsonPath("$.content[1].userId").value(1));

		verify(userAndCouponService, times(1)).getAllUsersAndCouponsByUserPaging(userId, pageable);
	}

	@Test
	@WithMockUser(roles = "COUPON_ADMIN")
	void testGetAllUsersAndCouponsByManagerPaging() throws Exception {

		Page<UserAndCouponResponseDTO> dtoPage = new PageImpl<>(List.of(dto1, dto2));




		// Arrange
		Pageable pageable = PageRequest.of(1, 2);
		when(userAndCouponService.getAllUsersAndCouponsByManagerPaging(pageable, null, null)).thenReturn(dtoPage);

		// Act & Assert
		mockMvc.perform(get("/coupons/users")
				.param("page", "1")
				.param("size", "2")
				.header("Authorization", "Bearer dummyToken")
				.with(csrf())
				.with(user(currentAdmin)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(1))
			.andExpect(jsonPath("$.content[0].userId").value(1))
			.andExpect(jsonPath("$.content[1].id").value(2))
			.andExpect(jsonPath("$.content[1].userId").value(1));

		verify(userAndCouponService, times(1)).getAllUsersAndCouponsByManagerPaging(pageable, null, null);
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testFindCouponByOrder() throws Exception {
		// 준비: 공통 DTO 객체를 사용
		List<UserAndCouponResponseDTO> userAndCouponResponseList = List.of(dto3, dto4);

		// Mocking 서비스 호출
		BigDecimal bookPrice = new BigDecimal("30000");
		List<Long> bookIds = List.of(1L); // 예제 책 ID
		List<Long> categoryIds = List.of(1L); // 예제 카테고리 ID

		when(userAndCouponService.findCouponByOrder(anyLong(), eq(bookIds), eq(categoryIds), eq(bookPrice)))
			.thenReturn(userAndCouponResponseList);

		// Act & Assert
		mockMvc.perform(get("/coupons/users/order")
				.param("bookPrice", bookPrice.toString())
				.param("bookIds", "1") // Query 파라미터로 전달
				.param("categoryIds", "1") // Query 파라미터로 전달
				.header("Authorization", "Bearer dummyToken")
				.with(csrf())
				.with(user(currentMember)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(3))
			.andExpect(jsonPath("$[0].bookTitle").value("츠츠츠츠"))
			.andExpect(jsonPath("$[1].id").value(4))
			.andExpect(jsonPath("$[1].categoryName").value("유아"));

		verify(userAndCouponService, times(1)).findCouponByOrder(anyLong(), eq(bookIds), eq(categoryIds), eq(bookPrice));


	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void testFindCouponByCartOrder() throws Exception {
		// 여러 개의 GetBookByOrderCouponResponse 객체를 포함하는 리스트 생성
		List<GetBookByOrderCouponResponse> bookDetails = Arrays.asList(bookDetail1, bookDetail2);

		// Mocking 서비스 호출
		when(userAndCouponService.findCouponByCartOrder(anyLong(), any()))
			.thenReturn(List.of(dto3, dto4));

		mockMvc.perform(post("/coupons/users/order/carts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookDetails))
				.header("Authorization", "Bearer dummyToken")
			.with(csrf())
			.with(user(currentMember)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(dto3.id()))
			.andExpect(jsonPath("$[0].bookTitle").value(dto3.bookTitle()))
			.andExpect(jsonPath("$[1].id").value(dto4.id()))
			.andExpect(jsonPath("$[1].categoryName").value(dto4.categoryName()));

		verify(userAndCouponService, times(1)).findCouponByCartOrder(anyLong(), any());
	}



	@Test
	@WithMockUser(roles = "MEMBER")
	void testUpdateCouponAfterPayment() throws Exception {
		// Arrange
		Long userAndCouponId = 1L;

		// Mocking 서비스 호출
		Mockito.doNothing().when(userAndCouponService).updateCouponAfterPayment(userAndCouponId);

		// Act & Assert
		mockMvc.perform(patch("/coupons/users/payment/{userAndCouponId}", userAndCouponId)
				.header("Authorization", "Bearer dummyToken")
				.with(csrf())
			.with(user(currentMember)))
			.andExpect(status().isOk());

		verify(userAndCouponService, times(1)).updateCouponAfterPayment(userAndCouponId);
	}



	@Test
	@WithMockUser(roles = "MEMBER")
	void testGetSelectedCouponWithValidCouponId() throws Exception {
		Long validCouponId = 1L;

		Mockito.when(userAndCouponService.findUserAndCouponsById(validCouponId))
			.thenReturn(dto5);

		mockMvc.perform(get("/coupons/users/order/coupon")
				.param("couponId", validCouponId.toString())
				.header("Authorization", "Bearer dummyToken")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.type").value("sale"));

		verify(userAndCouponService, times(1)).findUserAndCouponsById(validCouponId);
	}





	@Test
	void testIsRealUserCheckWhenCurrentUserDetailsIsNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/coupons/users/auth")
				.header("Authorization", "Bearer dummyToken")
				.with(csrf())
				.with(user("1").roles("MEMBER")) // This is just to have a user, but you need to simulate the null user
				.with(request -> {
					// Simulate a null currentUserDetails
					request.setAttribute("currentUserDetails", null);
					return request;
				}))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(false));
	}


	@Test
	@WithMockUser(roles = "MEMBER")
	void testIsRealUserCheckWhenCurrentUserDetailsIsNotNull() throws Exception {
		mockMvc.perform(get("/coupons/users/auth")
				.header("Authorization", "Bearer dummyToken")
				.with(csrf())
				.with(user(currentMember)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(true));

	}
}