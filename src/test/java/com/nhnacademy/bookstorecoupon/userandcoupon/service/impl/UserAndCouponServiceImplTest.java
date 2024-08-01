package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.NotFoundUserAndCouponException;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.NotFoundUserBirthdayException;
import com.nhnacademy.bookstorecoupon.userandcoupon.feignclient.UserBirthdayFeignClient;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;

class UserAndCouponServiceImplTest {

	@InjectMocks
	private UserAndCouponServiceImpl userAndCouponService;

	@Mock
	private UserAndCouponRepository userAndCouponRepository;

	@Mock
	private BookCouponRepository bookCouponRepository;

	@Mock
	private CategoryCouponRepository categoryCouponRepository;

	@Mock
	private UserBirthdayFeignClient userBirthdayFeignClient;

	@Mock
	private CouponPolicyRepository couponPolicyRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createUserWelcomeCouponIssue_ShouldCreateCoupon_WhenPolicyExists() {
		Long userId = 1L;
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("welcome")
			.build();

		// 현재 시간 고정
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expectedExpiredDate = now.plusDays(365);
		LocalDateTime expectedIssueDate = now;

		when(couponPolicyRepository.findLatestCouponPolicyByType("welcome"))
			.thenReturn(Optional.of(couponPolicy));

		// 테스트 실행
		userAndCouponService.createUserWelcomeCouponIssue(userId);

		// 검증할 객체의 필드 값
		UserAndCoupon expectedUserAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(userId)
			.isUsed(false)
			.expiredDate(expectedExpiredDate)
			.issueDate(expectedIssueDate)
			.build();

		// Mockito Argument Matcher 사용
		verify(userAndCouponRepository, times(1)).save(argThat(userAndCoupon ->
			userAndCoupon.getCouponPolicy().equals(expectedUserAndCoupon.getCouponPolicy()) &&
				userAndCoupon.getUserId().equals(expectedUserAndCoupon.getUserId()) &&
				!userAndCoupon.getIsUsed() &&
				userAndCoupon.getExpiredDate().isAfter(expectedExpiredDate.minusSeconds(1)) &&
				userAndCoupon.getExpiredDate().isBefore(expectedExpiredDate.plusSeconds(1)) &&
				userAndCoupon.getIssueDate().isAfter(expectedIssueDate.minusSeconds(1)) &&
				userAndCoupon.getIssueDate().isBefore(expectedIssueDate.plusSeconds(1))
		));
	}

	@Test
	void createUserWelcomeCouponIssue_ShouldThrowException_WhenPolicyNotFound() {
		Long userId = 1L;

		when(couponPolicyRepository.findLatestCouponPolicyByType("welcome"))
			.thenReturn(Optional.empty());

		CouponPolicyNotFoundException exception = assertThrows(
			CouponPolicyNotFoundException.class,
			() -> userAndCouponService.createUserWelcomeCouponIssue(userId)
		);

		assertEquals("최신 웰컴쿠폰 정책을 찾을 수 없습니다.", exception.getErrorStatus().getMessage());
	}



	@Test
	void issueBirthdayCoupon_ShouldIssueCoupons_WhenUsersExist() {
		LocalDate today = LocalDate.now();
		BirthdayCouponTargetResponse user = new BirthdayCouponTargetResponse(1L, today);
		List<BirthdayCouponTargetResponse> birthdayList = Collections.singletonList(user);

		// 현재 시간 고정
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expectedExpiredDate = now.plusDays(365);
		LocalDateTime expectedIssueDate = now;


		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("birthday")
			.build();

		when(userBirthdayFeignClient.getUsersWithBirthday(today))
			.thenReturn(ResponseEntity.ok(birthdayList));
		when(couponPolicyRepository.findLatestCouponPolicyByType("birthday"))
			.thenReturn(Optional.of(couponPolicy));

		userAndCouponService.issueBirthdayCoupon();

		UserAndCoupon expectedUserAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(user.userId())
			.isUsed(false)
			.expiredDate(expectedExpiredDate)
			.issueDate(expectedIssueDate)
			.build();



		verify(userAndCouponRepository, times(1)).save(argThat(userAndCoupon ->
			userAndCoupon.getCouponPolicy().equals(expectedUserAndCoupon.getCouponPolicy()) &&
				userAndCoupon.getUserId().equals(expectedUserAndCoupon.getUserId()) &&
				!userAndCoupon.getIsUsed() &&
				userAndCoupon.getExpiredDate().isAfter(expectedExpiredDate.minusSeconds(1)) &&
				userAndCoupon.getExpiredDate().isBefore(expectedExpiredDate.plusSeconds(1)) &&
				userAndCoupon.getIssueDate().isAfter(expectedIssueDate.minusSeconds(1)) &&
				userAndCoupon.getIssueDate().isBefore(expectedIssueDate.plusSeconds(1))
		));
	}

	@Test
	public void testIssueBirthdayCoupon_whenNoCouponPolicy() {
		// Given
		LocalDate today = LocalDate.now();
		BirthdayCouponTargetResponse user1 = new BirthdayCouponTargetResponse(1L, today);
		List<BirthdayCouponTargetResponse> birthdayList = List.of(user1);

		when(userBirthdayFeignClient.getUsersWithBirthday(today)).thenReturn(new ResponseEntity<>(birthdayList, HttpStatus.OK));
		when(couponPolicyRepository.findLatestCouponPolicyByType("birthday")).thenReturn(Optional.empty());

		// When
		CouponPolicyNotFoundException exception = assertThrows(CouponPolicyNotFoundException.class, () -> {
			userAndCouponService.issueBirthdayCoupon();
		});

		// Then
		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals("최신 생일쿠폰 정책을 찾을 수 없습니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
		assertNotNull(errorStatus.getTimestamp());
	}

	@Test
	public void testIssueBirthdayCoupon_whenNoUsersWithBirthday() {
		// Given
		LocalDate today = LocalDate.now();
		when(userBirthdayFeignClient.getUsersWithBirthday(today)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

		// When & Then
		NotFoundUserBirthdayException exception = assertThrows(NotFoundUserBirthdayException.class, () -> {
			userAndCouponService.issueBirthdayCoupon();
		});
		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals("유저의 생일리스트가 존재하지 않습니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
		assertNotNull(errorStatus.getTimestamp());
	}

	@Test
	void getAllUsersAndCouponsByUserPaging_ShouldReturnPagedResult() {
		Long userId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		UserAndCouponResponseDTO responseDTO = new UserAndCouponResponseDTO(
			1L, userId, null, false, LocalDateTime.now(), LocalDateTime.now(),
			BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, "book",
			true, 1L, "Book Title", null, null);

		Page<UserAndCouponResponseDTO> page = new PageImpl<>(Collections.singletonList(responseDTO));

		when(userAndCouponRepository.findAllByUserPaging(pageable, userId, new HashMap<>(), new HashMap<>()))
			.thenReturn(page);

		Page<UserAndCouponResponseDTO> result = userAndCouponService.getAllUsersAndCouponsByUserPaging(userId, pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals(responseDTO, result.getContent().getFirst());
	}

	@Test
	void getAllUsersAndCouponsByManagerPaging_ShouldReturnPagedResult() {
		Pageable pageable = PageRequest.of(0, 10);
		String type = "welcome";
		Long userId = 1L;
		UserAndCouponResponseDTO responseDTO = new UserAndCouponResponseDTO(
			1L, userId, null, false, LocalDateTime.now(), LocalDateTime.now(),
			BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, "book",
			true, 1L, "Book Title", null, null);

		Page<UserAndCouponResponseDTO> page = new PageImpl<>(Collections.singletonList(responseDTO));

		when(userAndCouponRepository.findAllByManagerPaging(pageable, type, userId, new HashMap<>(), new HashMap<>()))
			.thenReturn(page);

		Page<UserAndCouponResponseDTO> result = userAndCouponService.getAllUsersAndCouponsByManagerPaging(pageable, type, userId);

		assertEquals(1, result.getTotalElements());
		assertEquals(responseDTO, result.getContent().getFirst());
	}

	@Test
	void findCouponByOrder_ShouldReturnCoupons() {
		Long userId = 1L;
		List<Long> bookIds = Collections.singletonList(1L);
		List<Long> categoryIds = Collections.singletonList(1L);
		BigDecimal bookPrice = BigDecimal.valueOf(50);
		UserAndCouponResponseDTO responseDTO = new UserAndCouponResponseDTO(
			1L, userId, null, false, LocalDateTime.now(), LocalDateTime.now(),
			BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, "book",
			true, 1L, "Book Title", null, null);

		when(userAndCouponRepository.findCouponByOrder(userId, new HashMap<>(), new HashMap<>(), bookIds, categoryIds, bookPrice))
			.thenReturn(Collections.singletonList(responseDTO));

		List<UserAndCouponResponseDTO> result = userAndCouponService.findCouponByOrder(userId, bookIds, categoryIds, bookPrice);

		assertEquals(1, result.size());
		assertEquals(responseDTO, result.getFirst());
	}







	@Test
	void findCouponByCartOrder_ShouldReturnCoupons() {
		Long userId = 1L;
		List<GetBookByOrderCouponResponse> bookDetails = Collections.singletonList(
			new GetBookByOrderCouponResponse(1L, BigDecimal.valueOf(50), List.of(1L))
		);
		UserAndCouponResponseDTO responseDTO = new UserAndCouponResponseDTO(
			1L, userId, null, false, LocalDateTime.now(), LocalDateTime.now(),
			BigDecimal.valueOf(100), BigDecimal.valueOf(10), null, null, "book",
			true, 1L, "Book Title", null, null);

		when(userAndCouponRepository.findCouponByCartOrder(userId, new HashMap<>(), new HashMap<>(), bookDetails))
			.thenReturn(Collections.singletonList(responseDTO));

		List<UserAndCouponResponseDTO> result = userAndCouponService.findCouponByCartOrder(userId, bookDetails);

		assertEquals(1, result.size());
		assertEquals(responseDTO, result.getFirst());
	}

	// @Test
	// void updateCouponAfterPayment_ShouldUpdateCoupon() {
	// 	Long userAndCouponId = 1L;
	// 	UserAndCoupon userAndCoupon = UserAndCoupon.builder()
	// 		.couponPolicy(CouponPolicy.builder().minOrderPrice(BigDecimal.valueOf(100)).salePrice(BigDecimal.valueOf(100)).maxSalePrice(null).saleRate(null).type("welcome").build())
	// 		.userId(1L)
	// 		.isUsed(false)
	// 		.expiredDate(LocalDateTime.now().plusDays(30))
	// 		.issueDate(LocalDateTime.now().minusDays(10))
	// 		.build();
	//
	// 	when(userAndCouponRepository.findById(userAndCouponId)).thenReturn(Optional.of(userAndCoupon));
	//
	// 	userAndCouponService.updateCouponAfterPayment(userAndCouponId);
	//
	// 	verify(userAndCouponRepository, times(1)).save(argThat(coupon ->
	// 		coupon.getId().equals(userAndCouponId) &&
	// 			coupon.getIsUsed() &&
	// 			coupon.getUsedDate().isAfter(LocalDateTime.now().minusMinutes(1))
	// 	));
	// }

	@Test
	void updateCouponAfterPayment_ShouldThrowException_WhenCouponNotFound() {
		Long userAndCouponId = 1L;

		when(userAndCouponRepository.findById(userAndCouponId)).thenReturn(Optional.empty());

		NotFoundUserAndCouponException exception = assertThrows(
			NotFoundUserAndCouponException.class,
			() -> userAndCouponService.updateCouponAfterPayment(userAndCouponId)
		);

		assertEquals("유저의 쿠폰이 존재하지 않습니다.", exception.getErrorStatus().getMessage());
	}

	// @Test
	// void findUserAndCouponsById_ShouldReturnCouponDetails() {
	// 	Long couponId = 1L;
	// 	CouponPolicy couponPolicy = CouponPolicy.builder()
	// 		.minOrderPrice(BigDecimal.valueOf(100))
	// 		.salePrice(BigDecimal.valueOf(10))
	// 		.saleRate(null)
	// 		.maxSalePrice(null)
	// 		.type("welcome")
	// 		.build();
	//
	// 	UserAndCoupon userAndCoupon = UserAndCoupon.builder()
	// 		.couponPolicy(couponPolicy)
	// 		.userId(1L)
	// 		.isUsed(false)
	// 		.expiredDate(LocalDateTime.now().plusDays(30))
	// 		.issueDate(LocalDateTime.now().minusDays(10))
	// 		.build();
	//
	// 	when(userAndCouponRepository.findById(couponId)).thenReturn(Optional.of(userAndCoupon));
	//
	// 	UserAndCouponOrderResponseDTO responseDTO = userAndCouponService.findUserAndCouponsById(couponId);
	//
	// 	assertEquals(couponId, responseDTO.id());
	// 	assertEquals(couponPolicy.getMinOrderPrice(), responseDTO.minOrderPrice());
	// 	assertEquals(couponPolicy.getSalePrice(), responseDTO.salePrice());
	// 	assertEquals(couponPolicy.getSaleRate(), responseDTO.saleRate());
	// 	assertEquals(couponPolicy.getMaxSalePrice(), responseDTO.maxSalePrice());
	// 	assertEquals(couponPolicy.getType(), responseDTO.type());
	// }

	@Test
	void findUserAndCouponsById_ShouldThrowException_WhenCouponNotFound() {
		Long couponId = 1L;

		when(userAndCouponRepository.findById(couponId)).thenReturn(Optional.empty());

		NotFoundUserAndCouponException exception = assertThrows(
			NotFoundUserAndCouponException.class,
			() -> userAndCouponService.findUserAndCouponsById(couponId)
		);

		assertEquals("유저의 쿠폰이 존재하지 않습니다.", exception.getErrorStatus().getMessage());
	}
}