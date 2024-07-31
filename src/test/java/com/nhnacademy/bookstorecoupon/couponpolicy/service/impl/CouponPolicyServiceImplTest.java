package com.nhnacademy.bookstorecoupon.couponpolicy.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;


	class CouponPolicyServiceImplTest {

		@InjectMocks
		private CouponPolicyServiceImpl couponPolicyService;

		@Mock
		private CouponPolicyRepository couponPolicyRepository;

		@Mock
		private BookCouponRepository bookCouponRepository;

		@Mock
		private CategoryCouponRepository categoryCouponRepository;

		@Mock
		private UserAndCouponRepository userAndCouponRepository;

		@BeforeEach
		void setUp() {
			MockitoAnnotations.openMocks(this);
		}

		@Test
		void testIssueWelcomeCoupon() {
			CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
				BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null, null, "welcome", null, null, null, null
			);

			couponPolicyService.issueWelcomeCoupon(requestDTO);

			verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
		}

		@Test
		void testIssueBirthdayCoupon() {
			CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
				BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null,
				null, "birthday", null, null, null, null
			);

			couponPolicyService.issueBirthdayCoupon(requestDTO);

			verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
		}

		@Test
		void testIssueSpecificBookCoupon() {
			CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
				BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null,
				null, "book", 1L, "Book Title", null, null
			);

			couponPolicyService.issueSpecificBookCoupon(requestDTO);

			verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
			verify(bookCouponRepository, times(1)).save(any(BookCoupon.class));
		}

		@Test
		void testIssueSpecificCategoryCoupon() {
			CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
				BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null,
				null, "category", null, null, 1L, "Category Name"
			);

			couponPolicyService.issueSpecificCategoryCoupon(requestDTO);

			verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
			verify(categoryCouponRepository, times(1)).save(any(CategoryCoupon.class));
		}

		@Test
		void testIssueDiscountCoupon() {
			CouponPolicyRequestDTO requestDTO = new CouponPolicyRequestDTO(
				BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null,
				null, "sale", null, null, null, null
			);

			couponPolicyService.issueDiscountCoupon(requestDTO);

			verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
		}

		@Test
		void testGetAllCouponPolicies() {
			Pageable pageable = PageRequest.of(0, 10);
			Map<Long, BookCoupon.BookInfo> bookIdMap = Map.of();
			Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = Map.of();

			CouponPolicyResponseDTO couponPolicyResponseDTO = CouponPolicyResponseDTO.builder()
				.categoryName(null).categoryId(null).bookTitle("title")
				.id(1L).type("book").bookId(1L).minOrderPrice(BigDecimal.valueOf(100))
				.maxSalePrice(null).salePrice(BigDecimal.valueOf(200)).saleRate(null)
				.isUsed(true).build();
			Page<CouponPolicyResponseDTO> couponPolicyPage = new PageImpl<>(List.of(couponPolicyResponseDTO));

			when(couponPolicyRepository.findAllWithBooksAndCategories(pageable, bookIdMap, categoryIdMap))
				.thenReturn(couponPolicyPage);

			couponPolicyService.getAllCouponPolicies(pageable);

			verify(couponPolicyRepository, times(1)).findAllWithBooksAndCategories(any(Pageable.class), anyMap(), anyMap());
		}
		@Test
		void testUpdateCouponPolicyWhenPolicyExistsAndIsNotUsed() {
			Long policyId = 1L;
			CouponPolicyUpdateRequestDTO requestDTO = new CouponPolicyUpdateRequestDTO(
				BigDecimal.valueOf(2000), BigDecimal.valueOf(400), null,
				null, false // isUsed = false
			);

			CouponPolicy existingPolicy = CouponPolicy.builder()
				.minOrderPrice(BigDecimal.valueOf(100))
				.salePrice(BigDecimal.valueOf(100))
				.type("sale")
				.maxSalePrice(null)
				.saleRate(null)
				.build();

			UserAndCoupon userAndCoupon = mock(UserAndCoupon.class);
			List<UserAndCoupon> userAndCoupons = List.of(userAndCoupon);

			// Create a spy for existingPolicy
			CouponPolicy spiedPolicy = spy(existingPolicy);

			// Mock repository methods
			when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(spiedPolicy));
			when(userAndCouponRepository.findByCouponPolicy(spiedPolicy)).thenReturn(userAndCoupons);

			// Call the method under test
			couponPolicyService.updateCouponPolicy(policyId, requestDTO);

			// Verify interactions
			verify(couponPolicyRepository, times(1)).findById(policyId);
			verify(userAndCouponRepository, times(1)).findByCouponPolicy(spiedPolicy);
			verify(spiedPolicy, times(1)).update(
				BigDecimal.valueOf(2000),
				BigDecimal.valueOf(400),
				null,
				null,
				false
			); // Verify the update method was called with the correct arguments
			verify(userAndCoupon, times(1)).update(any(), eq(true)); // Verify if update is called with true for isUsed
		}

		@Test
		void testUpdateCouponPolicyWhenPolicyExistsAndIsUsed() {
			Long policyId = 1L;
			CouponPolicyUpdateRequestDTO requestDTO = new CouponPolicyUpdateRequestDTO(
				BigDecimal.valueOf(2000), BigDecimal.valueOf(400), null,
				null, true // isUsed = true
			);

			CouponPolicy existingPolicy = CouponPolicy.builder()
				.minOrderPrice(BigDecimal.valueOf(100))
				.salePrice(BigDecimal.valueOf(100))
				.type("sale")
				.maxSalePrice(null)
				.saleRate(null)
				.build();

			// Create a spy for existingPolicy
			CouponPolicy spiedPolicy = spy(existingPolicy);



			// Mock repository methods
			when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(spiedPolicy));

			// Call the method under test
			couponPolicyService.updateCouponPolicy(policyId, requestDTO);

			// Verify interactions
			verify(couponPolicyRepository, times(1)).findById(policyId);
			verify(userAndCouponRepository, never()).findByCouponPolicy(any()); // Ensure findByCouponPolicy is not called
			verify(spiedPolicy, times(1)).update(
				BigDecimal.valueOf(2000),
				BigDecimal.valueOf(400),
				null,
				null,
				true
			); // Verify the update method was called with the correct arguments
		}

		@Test
		void testUpdateCouponPolicyNotFound() {
			Long policyId = 1L;
			CouponPolicyUpdateRequestDTO requestDTO = new CouponPolicyUpdateRequestDTO(
				BigDecimal.valueOf(2000), BigDecimal.valueOf(400), null,
				null, false
			);

			when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.empty());

			CouponPolicyNotFoundException exception = assertThrows(CouponPolicyNotFoundException.class, () ->
				couponPolicyService.updateCouponPolicy(policyId, requestDTO)
			);

			assertEquals("해당 쿠폰정책번호 '1'는 존재하지 않습니다.", exception.getErrorStatus().getMessage());
			assertEquals(HttpStatus.NOT_FOUND, exception.getErrorStatus().getStatus());
		}
	}