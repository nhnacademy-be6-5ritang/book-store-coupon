package com.nhnacademy.bookstorecoupon.coupontemplate.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.nhnacademy.bookstorecoupon.coupontemplate.service.CouponTemplateService;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService {
	private final CouponTemplateRepository couponTemplateRepository;
	private final CouponPolicyRepository couponPolicyRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void createCouponTemplate(CouponTemplateRequestDTO requestDTO) {
		CouponPolicy couponPolicy = couponPolicyRepository.findById(requestDTO.couponPolicyId())
			.orElseThrow(
				() -> new CouponPolicyNotFoundException(
					ErrorStatus.from(String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", requestDTO.couponPolicyId()),
						HttpStatus.NOT_FOUND, LocalDateTime.now())));

		if (couponPolicy.getType().equals("birthday") || couponPolicy.getType().equals("welcome")) {
			ErrorStatus errorStatus2 = ErrorStatus.from(
				String.format("해당 쿠폰타입 '%s'은 템플릿 발급을 할 수 없습니다.", couponPolicy.getType()), HttpStatus.BAD_REQUEST,
				LocalDateTime.now());
			throw new CouponTemplateAddErrorException(errorStatus2);
		}

		if (couponPolicy.getIsUsed() == Boolean.FALSE) {
			ErrorStatus errorStatus2 = ErrorStatus.from("해당 쿠폰템플릿은 정책폐기로 인해 발급을 할 수 없습니다.", HttpStatus.FORBIDDEN,
				LocalDateTime.now());
			throw new CouponTemplateAddErrorException(errorStatus2);
		}

		CouponTemplate couponTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(requestDTO.expiredDate())
			.issueDate(requestDTO.issueDate())
			.quantity(requestDTO.quantity())
			.build();

		couponTemplateRepository.save(couponTemplate);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CouponTemplateResponseDTO> getAllCouponTemplatesByManagerPaging(Pageable pageable) {
		// Fetch necessary maps
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();

		// Query templates
		return couponTemplateRepository.findAllTemplatesByManagerPaging(PageRequest.of(page, pageSize), bookIdMap,
			categoryIdMap);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CouponTemplateResponseDTO> getAllCouponTemplatesByUserPaging(Pageable pageable
	) {

		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		// Fetch necessary maps
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();

		// Query templates
		return couponTemplateRepository.findAllTemplatesByUserPaging(PageRequest.of(page, pageSize), bookIdMap,
			categoryIdMap);
	}

}