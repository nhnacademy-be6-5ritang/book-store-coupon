package com.nhnacademy.bookstorecoupon.couponpolicy.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyUpdateRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO2;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;

@Service
@Transactional
public class CouponPolicyServiceImpl implements CouponPolicyService {
	private final CouponPolicyRepository couponPolicyRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;
	private final UserAndCouponRepository userAndCouponRepository;

	public CouponPolicyServiceImpl(CouponPolicyRepository couponPolicyRepository,
		BookCouponRepository bookCouponRepository, CategoryCouponRepository categoryCouponRepository,
		UserAndCouponRepository userAndCouponRepository) {
		this.couponPolicyRepository = couponPolicyRepository;
		this.bookCouponRepository = bookCouponRepository;
		this.categoryCouponRepository = categoryCouponRepository;
		this.userAndCouponRepository = userAndCouponRepository;
	}

	@Override
	public void issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		couponPolicyRepository.save(couponPolicy);

	}

	@Override
	public void issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);
		couponPolicyRepository.save(couponPolicy);

	}

	@Override
	public void issueSpecificBookCoupon(Long bookId, CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		couponPolicyRepository.save(couponPolicy);
		bookCouponRepository.save(BookCoupon.builder().bookId(bookId).couponPolicy(couponPolicy).build());

	}

	@Override
	public void issueSpecificCategoryCoupon(Long categoryId, CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		couponPolicyRepository.save(couponPolicy);
		categoryCouponRepository.save(new CategoryCoupon(couponPolicy, categoryId));

	}

	@Override
	public void issueDiscountCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		couponPolicyRepository.save(couponPolicy);

	}

	@Override
	@Transactional(readOnly = true)
	public List<CouponPolicyResponseDTO2> getAllCouponPolicies() {
		List<CouponPolicy> policies = couponPolicyRepository.findAll();
		List<CouponPolicyResponseDTO2> dtos = new ArrayList<>();

		for (CouponPolicy policy : policies) {
			Long bookId = null;
			Long categoryId = null;

			// 쿠폰 정책의 종류(type)에 따라서 bookId 또는 categoryId 설정
			if ("book".equals(policy.getType())) {
				BookCoupon bookCoupon = bookCouponRepository.findByCouponPolicy(policy);
				if (bookCoupon != null) {
					bookId = bookCoupon.getBookId();
				}
			}
			if ("category".equals(policy.getType())) {
				CategoryCoupon categoryCoupon = categoryCouponRepository.findByCouponPolicy(policy);
				if (categoryCoupon != null) {
					categoryId = categoryCoupon.getCategoryId();
				}
			}

			// CouponPolicyResponseDTO 객체 생성 및 리스트에 추가
			CouponPolicyResponseDTO2 dto = CouponPolicyResponseDTO2.fromCouponPolicy(policy, bookId, categoryId);
			dtos.add(dto);
		}

		return dtos;
	}


	@Override
	public void updateCouponPolicy(Long id, CouponPolicyUpdateRequestDTO requestDTO) {
		Optional<CouponPolicy> optionalPolicy = couponPolicyRepository.findById(id);
		if (optionalPolicy.isPresent()) {
			CouponPolicy policy = optionalPolicy.get();
			policy.update(
				requestDTO.minOrderPrice(),
				requestDTO.salePrice(),
				requestDTO.saleRate(),
				requestDTO.maxSalePrice(),
				requestDTO.isUsed()
			);
			if (requestDTO.isUsed() == Boolean.FALSE) {
				List<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByCouponPolicy(policy);


				for (UserAndCoupon userAndCoupon : userAndCoupons) {
					userAndCoupon.update(LocalDateTime.now(), true);
				}

			}


		} else {
			String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new CouponPolicyNotFoundException(errorStatus);
		}



		}
	}


