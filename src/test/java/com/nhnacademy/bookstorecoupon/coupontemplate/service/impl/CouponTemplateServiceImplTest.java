package com.nhnacademy.bookstorecoupon.coupontemplate.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponTemplateAddErrorException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;

class CouponTemplateServiceImplTest {

	@Mock
	private CouponTemplateRepository couponTemplateRepository;

	@Mock
	private CouponPolicyRepository couponPolicyRepository;

	@Mock
	private BookCouponRepository bookCouponRepository;

	@Mock
	private CategoryCouponRepository categoryCouponRepository;

	@InjectMocks
	private CouponTemplateServiceImpl couponTemplateService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void testCreateCouponTemplateSuccess() {
		Long couponPolicyId = 1L;
		Long templateId = 1L; // 이 ID가 일관되게 사용되도록 합니다.

		// CouponPolicy 객체 설정
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("sale")
			.build();



		// 요청 DTO 설정
		CouponTemplateRequestDTO requestDTO = new CouponTemplateRequestDTO(
			couponPolicyId,
			LocalDateTime.now().plusDays(30),
			LocalDateTime.now(),
			100L
		);

		// couponPolicyRepository의 모의 동작 설정
		when(couponPolicyRepository.findById(couponPolicyId)).thenReturn(Optional.of(couponPolicy));

		// 테스트할 메소드 호출
		couponTemplateService.createCouponTemplate(requestDTO);

		// 예상되는 CouponTemplate 객체 생성
		CouponTemplate expectedTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(requestDTO.expiredDate())
			.issueDate(requestDTO.issueDate())
			.quantity(requestDTO.quantity())
			.build();

		// CouponTemplate을 저장한 후 모의 동작 설정
		when(couponTemplateRepository.findById(templateId)).thenReturn(Optional.of(expectedTemplate));

		// save 메소드가 올바른 CouponTemplate으로 호출되었는지 검증
		CouponTemplate actualTemplate = couponTemplateRepository.findById(templateId).orElseThrow();

		// 실제 객체와 예상 객체 비교
		assertEquals(expectedTemplate, actualTemplate);
	}

	@Test
	void testCreateCouponTemplateWhenPolicyNotFound() {
		Long couponPolicyId = 1L;
		CouponTemplateRequestDTO requestDTO = new CouponTemplateRequestDTO(
			couponPolicyId,
			LocalDateTime.now().plusDays(30),
			LocalDateTime.now(),
			100L
		);

		when(couponPolicyRepository.findById(couponPolicyId)).thenReturn(Optional.empty());

		CouponPolicyNotFoundException thrown = assertThrows(CouponPolicyNotFoundException.class, () ->
			couponTemplateService.createCouponTemplate(requestDTO)
		);

		assertEquals("해당 쿠폰정책번호 '1'는 존재하지 않습니다.", thrown.getErrorStatus().getMessage());
		assertEquals(HttpStatus.NOT_FOUND, thrown.getErrorStatus().getStatus());
	}

	@Test
	void testCreateCouponTemplateWhenPolicyTypeNotAllowed() {
		Long couponPolicyId = 1L;
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("birthday") // Ensure type is set to "birthday" for this test
			.build();

		CouponTemplateRequestDTO requestDTO = new CouponTemplateRequestDTO(
			couponPolicyId,
			LocalDateTime.now().plusDays(30),
			LocalDateTime.now(),
			100L
		);

		when(couponPolicyRepository.findById(couponPolicyId)).thenReturn(Optional.of(couponPolicy));

		CouponTemplateAddErrorException thrown = assertThrows(CouponTemplateAddErrorException.class, () ->
			couponTemplateService.createCouponTemplate(requestDTO)
		);

		assertEquals("해당 쿠폰타입 'birthday'은 템플릿 발급을 할 수 없습니다.", thrown.getErrorStatus().getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getErrorStatus().getStatus());
	}

	@Test
	void testCreateCouponTemplateWhenPolicyIsUsed() throws NoSuchFieldException, IllegalAccessException {
		Long couponPolicyId = 1L;
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("sale")
			.build();


		// Use reflection to set `isUsed` to true
		Field isUsedField = CouponPolicy.class.getDeclaredField("isUsed");
		isUsedField.setAccessible(true);
		isUsedField.set(couponPolicy, false);

		CouponTemplateRequestDTO requestDTO = new CouponTemplateRequestDTO(
			couponPolicyId,
			LocalDateTime.now().plusDays(30),
			LocalDateTime.now(),
			100L
		);

		when(couponPolicyRepository.findById(couponPolicyId)).thenReturn(Optional.of(couponPolicy));

		CouponTemplateAddErrorException thrown = assertThrows(CouponTemplateAddErrorException.class, () ->
			couponTemplateService.createCouponTemplate(requestDTO)
		);

		assertEquals("해당 쿠폰템플릿은 정책폐기로 인해 발급을 할 수 없습니다.", thrown.getErrorStatus().getMessage());
		assertEquals(HttpStatus.FORBIDDEN, thrown.getErrorStatus().getStatus());
	}

	@Test
	void testGetAllCouponTemplatesByManagerPaging() {
		Pageable pageable = PageRequest.of(0, 10);
		CouponTemplateResponseDTO responseDTO = CouponTemplateResponseDTO.builder()
			.id(1L)
			.couponPolicyId(1L)
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("book")
			.isUsed(true)
			.bookId(1L)
			.bookTitle("Sample Book")
			.categoryId(null)
			.categoryName(null)
			.expiredDate(LocalDateTime.now().plusDays(30))
			.issueDate(LocalDateTime.now())
			.quantity(100L)
			.build();

		Page<CouponTemplateResponseDTO> page = new PageImpl<>(List.of(responseDTO), pageable, 1);

		// Create a Map with specific types
		Map<Long, BookCoupon.BookInfo> bookInfoMap = Map.of(1L, new BookCoupon.BookInfo(1L, "Sample Book"));
		Map<Long, CategoryCoupon.CategoryInfo> categoryInfoMap = Map.of(1L, new CategoryCoupon.CategoryInfo(1L, "Sample Category"));

		// Ensure that mock methods are called
		when(bookCouponRepository.fetchBookIdMap()).thenReturn(bookInfoMap);
		when(categoryCouponRepository.fetchCategoryIdMap()).thenReturn(categoryInfoMap);

		// Match the exact arguments in the when clause
		when(couponTemplateRepository.findAllTemplatesByManagerPaging(eq(pageable), eq(bookInfoMap), eq(categoryInfoMap))).thenReturn(page);

		// Execute the method under test
		Page<CouponTemplateResponseDTO> result = couponTemplateService.getAllCouponTemplatesByManagerPaging(pageable);

		// Verify the result
		assertNotNull(result, "Result should not be null");
		assertEquals(1, result.getTotalElements(), "Total elements should match");
		assertEquals(responseDTO, result.getContent().getFirst(), "Response DTO should match");

	}
	@Test
	void testGetAllCouponTemplatesByUserPaging() {
		Pageable pageable = PageRequest.of(0, 10);
		CouponTemplateResponseDTO responseDTO = CouponTemplateResponseDTO.builder()
			.id(1L)
			.couponPolicyId(1L)
			.minOrderPrice(BigDecimal.valueOf(100))
			.salePrice(BigDecimal.valueOf(10))
			.saleRate(null)
			.maxSalePrice(null)
			.type("book")
			.isUsed(true)
			.bookId(1L)
			.bookTitle("Sample Book")
			.categoryId(null)
			.categoryName(null)
			.expiredDate(LocalDateTime.now().plusDays(30))
			.issueDate(LocalDateTime.now())
			.quantity(100L)
			.build();

		Page<CouponTemplateResponseDTO> page = new PageImpl<>(List.of(responseDTO), pageable, 1);

		// Create a Map with specific types
		Map<Long, BookCoupon.BookInfo> bookInfoMap = Map.of(1L, new BookCoupon.BookInfo(1L, "Sample Book"));
		Map<Long, CategoryCoupon.CategoryInfo> categoryInfoMap = Map.of(1L, new CategoryCoupon.CategoryInfo(1L, "Sample Category"));

		// Ensure that mock methods are called
		when(bookCouponRepository.fetchBookIdMap()).thenReturn(bookInfoMap);
		when(categoryCouponRepository.fetchCategoryIdMap()).thenReturn(categoryInfoMap);

		// Match the exact arguments in the when clause
		when(couponTemplateRepository.findAllTemplatesByUserPaging(eq(pageable), eq(bookInfoMap), eq(categoryInfoMap))).thenReturn(page);

		// Execute the method under test
		Page<CouponTemplateResponseDTO> result = couponTemplateService.getAllCouponTemplatesByUserPaging(pageable);

		// Verify the result
		assertNotNull(result, "Result should not be null");
		assertEquals(1, result.getTotalElements(), "Total elements should match");
		assertEquals(responseDTO, result.getContent().getFirst(), "Response DTO should match");

	}
}
