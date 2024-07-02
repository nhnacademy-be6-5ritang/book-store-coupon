package com.nhnacademy.bookstorecoupon.couponpolicy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<CouponPolicyResponseDTO> getAllCouponPolicies() {
		List<CouponPolicy> policies = couponPolicyRepository.findAll();
		return policies.stream()
			.map(CouponPolicyResponseDTO::fromCouponPolicy)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CouponPolicyResponseDTO getCouponPolicyById(Long id) {
		Optional<CouponPolicy> optionalPolicy = couponPolicyRepository.findById(id);
		if (optionalPolicy.isPresent()) {
			CouponPolicy policy = optionalPolicy.get();
			return CouponPolicyResponseDTO.fromCouponPolicy(policy);
		} else {
			String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new CouponPolicyNotFoundException(errorStatus);
		}
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

		//	 couponPolicyRepository.save(policy);

			if (requestDTO.isUsed() == Boolean.FALSE) {
				List<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByCouponPolicy(policy);


				for (UserAndCoupon userAndCoupon : userAndCoupons) {
					userAndCoupon.update(LocalDateTime.now(), true);
				}

			} else {
				String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id);
				ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
				throw new CouponPolicyNotFoundException(errorStatus);
			}
		}
	}
}

