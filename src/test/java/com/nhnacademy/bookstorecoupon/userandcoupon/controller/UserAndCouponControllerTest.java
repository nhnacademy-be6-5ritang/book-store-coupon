package com.nhnacademy.bookstorecoupon.userandcoupon.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
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

	@MockBean
	private CurrentUserDetails currentUserDetails;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(roles = {"MEMBER"})
	void createUserAndCoupon_ShouldReturnCreated_WhenValidRequest() throws Exception {
		Long couponId = 1L;
		Long userId = 1L;

		// Mock the currentUserDetails to return the userId
		CurrentUserDetails currentUserDetails = mock(CurrentUserDetails.class);
		when(currentUserDetails.getUserId()).thenReturn(userId);

		// Mock the Authentication and SecurityContext
		UserDetails userDetails = User.withUsername("user")
			.password("password")
			.authorities(new SimpleGrantedAuthority("ROLE_MEMBER"))
			.build();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);

		// Perform the request
		mockMvc.perform(MockMvcRequestBuilders.post("/coupons/{couponId}", couponId)
				.with(csrf()))  // csrf 토큰이 필요한 경우
			.andExpect(MockMvcResultMatchers.status().isCreated());

		// Verify that the RabbitMQUserAndCouponService was called exactly once
		verify(rabbitMQUserAndCouponService, times(1)).createUserAndCoupon(couponId, userId);
	}
}


	// @Test
	// void createUserWelcomeCouponIssue_ShouldReturnCreated_WhenValidRequest() {
	// 	Long userId = 1L;
	//
	// 	ResponseEntity<Void> response = userAndCouponController.createUserWelcomeCouponIssue(userId);
	//
	// 	verify(userAndCouponService).createUserWelcomeCouponIssue(userId);
	// 	assertEquals(HttpStatus.CREATED, response.getStatusCode());
	// }
	//
	// @Test
	// void createUserWelcomeCouponIssue_ShouldThrowException_WhenUserIdIsNull() {
	// 	Long userId = null;
	//
	// 	UserCouponValidationException thrown = assertThrows(UserCouponValidationException.class, () ->
	// 		userAndCouponController.createUserWelcomeCouponIssue(userId));
	//
	// 	assertEquals("유저 아이디가 필요합니다.", thrown.getMessage());
	// }
	//
	// @Test
	// void getAllUserAndCouponsByUserPaging_ShouldReturnPageOfCoupons() {
	// 	Long userId = 1L;
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	Page<UserAndCouponResponseDTO> page = new PageImpl<>(List.of(new UserAndCouponResponseDTO()));
	//
	// 	when(currentUserDetails.getUserId()).thenReturn(userId);
	// 	when(userAndCouponService.getAllUsersAndCouponsByUserPaging(userId, pageable)).thenReturn(page);
	//
	// 	ResponseEntity<Page<UserAndCouponResponseDTO>> response = userAndCouponController.getAllUserAndCouponsByUserPaging(currentUserDetails, pageable);
	//
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// 	assertEquals(1, response.getBody().getTotalElements());
	// }
	//
	// @Test
	// void getAllUserAndCouponsByUserPaging_ShouldThrowException_WhenUserIdIsNull() {
	// 	when(currentUserDetails.getUserId()).thenReturn(null);
	//
	// 	UserCouponValidationException thrown = assertThrows(UserCouponValidationException.class, () ->
	// 		userAndCouponController.getAllUserAndCouponsByUserPaging(currentUserDetails, PageRequest.of(0, 10)));
	//
	// 	assertEquals("유저 아이디가 필요합니다.", thrown.getMessage());
	// }
	//
	// @Test
	// void getAllUsersAndCouponsByManagerPaging_ShouldReturnPageOfCoupons() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	Page<UserAndCouponResponseDTO> page = new PageImpl<>(List.of(new UserAndCouponResponseDTO()));
	//
	// 	when(userAndCouponService.getAllUsersAndCouponsByManagerPaging(pageable, null, null)).thenReturn(page);
	//
	// 	ResponseEntity<Page<UserAndCouponResponseDTO>> response = userAndCouponController.getAllUsersAndCouponsByManagerPaging(pageable, null, null);
	//
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// 	assertEquals(1, response.getBody().getTotalElements());
	// }
	//
	// @Test
	// void findCouponByOrder_ShouldReturnListOfCoupons() {
	// 	Long userId = 1L;
	// 	BigDecimal bookPrice = BigDecimal.valueOf(100);
	// 	List<UserAndCouponResponseDTO> coupons = List.of(new UserAndCouponResponseDTO());
	//
	// 	when(currentUserDetails.getUserId()).thenReturn(userId);
	// 	when(userAndCouponService.findCouponByOrder(userId, null, null, bookPrice)).thenReturn(coupons);
	//
	// 	ResponseEntity<List<UserAndCouponResponseDTO>> response = userAndCouponController.findCouponByOrder(currentUserDetails, null, null, bookPrice);
	//
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// 	assertEquals(coupons, response.getBody());
	// }
	//
	// @Test
	// void findCouponByCartOrder_ShouldReturnListOfCoupons() {
	// 	Long userId = 1L;
	// 	List<GetBookByOrderCouponResponse> bookDetails = List.of(new GetBookByOrderCouponResponse());
	// 	List<UserAndCouponResponseDTO> coupons = List.of(new UserAndCouponResponseDTO());
	//
	// 	when(currentUserDetails.getUserId()).thenReturn(userId);
	// 	when(userAndCouponService.findCouponByCartOrder(userId, bookDetails)).thenReturn(coupons);
	//
	// 	ResponseEntity<List<UserAndCouponResponseDTO>> response = userAndCouponController.findCouponByCartOrder(currentUserDetails, bookDetails);
	//
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// 	assertEquals(coupons, response.getBody());
	// }
	//
	// @Test
	// void updateCouponAfterPayment_ShouldReturnOk_WhenValidRequest() {
	// 	Long userAndCouponId = 1L;
	//
	// 	ResponseEntity<Void> response = userAndCouponController.updateCouponAfterPayment(userAndCouponId);
	//
	// 	verify(userAndCouponService).updateCouponAfterPayment(userAndCouponId);
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// }
	//
	// @Test
	// void updateCouponAfterPayment_ShouldThrowException_WhenUserAndCouponIdIsNull() {
	// 	Long userAndCouponId = null;
	//
	// 	UserCouponValidationException thrown = assertThrows(UserCouponValidationException.class, () ->
	// 		userAndCouponController.updateCouponAfterPayment(userAndCouponId));
	//
	// 	assertEquals("사용자 쿠폰 아이디가 필요합니다.", thrown.getMessage());
	// }
	//
	// @Test
	// void getSelectedCoupon_ShouldReturnCoupon_WhenValidRequest() {
	// 	Long couponId = 1L;
	// 	UserAndCouponOrderResponseDTO coupon = new UserAndCouponOrderResponseDTO();
	//
	// 	when(userAndCouponService.findUserAndCouponsById(couponId)).thenReturn(coupon);
	//
	// 	ResponseEntity<UserAndCouponOrderResponseDTO> response = userAndCouponController.getSelectedCoupon(couponId);
	//
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// 	assertEquals(coupon, response.getBody());
	// }
	//
	// @Test
	// void getSelectedCoupon_ShouldThrowException_WhenCouponIdIsNull() {
	// 	Long couponId = null;
	//
	// 	UserCouponValidationException thrown = assertThrows(UserCouponValidationException.class, () ->
	// 		userAndCouponController.getSelectedCoupon(couponId));
	//
	// 	assertEquals("사용자 쿠폰 아이디가 필요합니다.", thrown.getMessage());
	// }

	// @Test
	// void isRealUserCheck_ShouldReturnTrue_WhenUserIsReal() {
	// 	when(currentUserDetails).thenReturn(new CurrentUserDetails()); // Non-null CurrentUserDetails
	//
	// 	ResponseEntity<Boolean> response = userAndCouponController.isRealUserCheck(currentUserDetails);
	//
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// 	assertTrue(response.getBody());
	// }
	//
	// @Test
	// void isRealUserCheck_ShouldReturnFalse_WhenUserIsNull() {
	// 	when(currentUserDetails).thenReturn(null);
	//
	// 	ResponseEntity<Boolean> response = userAndCouponController.isRealUserCheck(null);
	//
	// 	assertEquals(HttpStatus.OK, response.getStatusCode());
	// 	assertFalse(response.getBody());
	// }
