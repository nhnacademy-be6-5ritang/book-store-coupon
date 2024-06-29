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
@Service
@Transactional
public class CouponPolicyServiceImpl implements CouponPolicyService {
	private final CouponPolicyRepository couponPolicyRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;

	public CouponPolicyServiceImpl(CouponPolicyRepository couponPolicyRepository,
		BookCouponRepository bookCouponRepository, CategoryCouponRepository categoryCouponRepository) {
		this.couponPolicyRepository = couponPolicyRepository;
		this.bookCouponRepository = bookCouponRepository;
		this.categoryCouponRepository = categoryCouponRepository;
	}
	@Override
	public CouponPolicyResponseDTO issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.minOrderPrice())
			.salePrice(requestDTO.salePrice())
			.saleRate(requestDTO.saleRate())
			.maxSalePrice(requestDTO.maxSalePrice())
			.type(requestDTO.type())
			.build();

		couponPolicyRepository.save(couponPolicy);
		return new CouponPolicyResponseDTO(
			couponPolicy.getId(),
			couponPolicy.getMinOrderPrice(),
			couponPolicy.getSalePrice(),
			couponPolicy.getSaleRate(),
			couponPolicy.getMaxSalePrice(),
			couponPolicy.getType());
	}

	@Override
	public CouponPolicyResponseDTO issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.minOrderPrice())
			.salePrice(requestDTO.salePrice())
			.saleRate(requestDTO.saleRate())
			.maxSalePrice(requestDTO.maxSalePrice())
			.type(requestDTO.type())
			.build();

		couponPolicyRepository.save(couponPolicy);
		return new CouponPolicyResponseDTO(
			couponPolicy.getId(),
			couponPolicy.getMinOrderPrice(),
			couponPolicy.getSalePrice(),
			couponPolicy.getSaleRate(),
			couponPolicy.getMaxSalePrice(),
			couponPolicy.getType());
	}

	@Override
	public CouponPolicyResponseDTO issueSpecificBookCoupon(Long bookId, CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.minOrderPrice())
			.salePrice(requestDTO.salePrice())
			.saleRate(requestDTO.saleRate())
			.maxSalePrice(requestDTO.maxSalePrice())
			.type(requestDTO.type())
			.build();

		couponPolicyRepository.save(couponPolicy);
		bookCouponRepository.save(BookCoupon.builder().bookId(bookId).couponPolicy(couponPolicy).build());
		return new CouponPolicyResponseDTO(
			couponPolicy.getId(),
			couponPolicy.getMinOrderPrice(),
			couponPolicy.getSalePrice(),
			couponPolicy.getSaleRate(),
			couponPolicy.getMaxSalePrice(),
			couponPolicy.getType());
	}

	@Override
	public CouponPolicyResponseDTO issueSpecificCategoryCoupon(Long categoryId, CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.minOrderPrice())
			.salePrice(requestDTO.salePrice())
			.saleRate(requestDTO.saleRate())
			.maxSalePrice(requestDTO.maxSalePrice())
			.type(requestDTO.type())
			.build();

		couponPolicyRepository.save(couponPolicy);
		categoryCouponRepository.save(new CategoryCoupon(couponPolicy, categoryId));
		return new CouponPolicyResponseDTO(
			couponPolicy.getId(),
			couponPolicy.getMinOrderPrice(),
			couponPolicy.getSalePrice(),
			couponPolicy.getSaleRate(),
			couponPolicy.getMaxSalePrice(),
			couponPolicy.getType());
	}

	@Override
	public CouponPolicyResponseDTO issueDiscountCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.minOrderPrice())
			.salePrice(requestDTO.salePrice())
			.saleRate(requestDTO.saleRate())
			.maxSalePrice(requestDTO.maxSalePrice())
			.type(requestDTO.type())
			.build();

		couponPolicyRepository.save(couponPolicy);
		return new CouponPolicyResponseDTO(
			couponPolicy.getId(),
			couponPolicy.getMinOrderPrice(),
			couponPolicy.getSalePrice(),
			couponPolicy.getSaleRate(),
			couponPolicy.getMaxSalePrice(),
			couponPolicy.getType());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CouponPolicyResponseDTO> getAllCouponPolicies() {
		List<CouponPolicy> policies = couponPolicyRepository.findAll();
		return policies.stream()
			.map(policy -> new CouponPolicyResponseDTO(
				policy.getId(),
				policy.getMinOrderPrice(),
				policy.getSalePrice(),
				policy.getSaleRate(),
				policy.getMaxSalePrice(),
				policy.getType()))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CouponPolicyResponseDTO getCouponPolicyById(Long id) {
		Optional<CouponPolicy> optionalPolicy = couponPolicyRepository.findById(id);
		if (optionalPolicy.isPresent()) {
			CouponPolicy policy = optionalPolicy.get();
			return new CouponPolicyResponseDTO(
				policy.getId(),
				policy.getMinOrderPrice(),
				policy.getSalePrice(),
				policy.getSaleRate(),
				policy.getMaxSalePrice(),
				policy.getType());
		} else {
			String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new CouponPolicyNotFoundException(errorStatus);
		}
	}

	@Override
	public CouponPolicyResponseDTO updateCouponPolicy(Long id, CouponPolicyUpdateRequestDTO requestDTO) {
		Optional<CouponPolicy> optionalPolicy = couponPolicyRepository.findById(id);
		if (optionalPolicy.isPresent()) {
			CouponPolicy policy = optionalPolicy.get();
			policy.update(
				requestDTO.minOrderPrice(),
				requestDTO.salePrice(),
				requestDTO.saleRate(),
				requestDTO.maxSalePrice()
			);
			couponPolicyRepository.save(policy); // TODO : 굳이이걸해야하는지 ?????
			return new CouponPolicyResponseDTO(
				policy.getId(),
				policy.getMinOrderPrice(),
				policy.getSalePrice(),
				policy.getSaleRate(),
				policy.getMaxSalePrice(),
				policy.getType());
		} else {
			String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new CouponPolicyNotFoundException(errorStatus);
		}
	}

	@Override
	public void deleteCouponPolicy(Long id) {
		if (couponPolicyRepository.existsById(id)) {
			couponPolicyRepository.deleteById(id);
		} else {

			String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new CouponPolicyNotFoundException(errorStatus);
		}
	}
}