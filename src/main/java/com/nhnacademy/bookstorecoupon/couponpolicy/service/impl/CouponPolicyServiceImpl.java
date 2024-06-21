package com.nhnacademy.bookstorecoupon.couponpolicy.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.CouponPolicyService;

import jakarta.persistence.EntityNotFoundException;
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
				policy.getMinOrderPrice(),
				policy.getSalePrice(),
				policy.getSaleRate(),
				policy.getMaxSalePrice(),
				policy.getType());
		} else {
			throw new EntityNotFoundException("Coupon Policy not found with ID: " + id);
		}
	}

	@Override
	public CouponPolicyResponseDTO updateCouponPolicy(Long id, CouponPolicyRequestDTO requestDTO) {
		Optional<CouponPolicy> optionalPolicy = couponPolicyRepository.findById(id);
		if (optionalPolicy.isPresent()) {
			CouponPolicy policy = optionalPolicy.get();
			policy.update(
				requestDTO.minOrderPrice(),
				requestDTO.salePrice(),
				requestDTO.saleRate(),
				requestDTO.maxSalePrice(),
				requestDTO.type()
			);
			couponPolicyRepository.save(policy);
			return new CouponPolicyResponseDTO(
				policy.getMinOrderPrice(),
				policy.getSalePrice(),
				policy.getSaleRate(),
				policy.getMaxSalePrice(),
				policy.getType());
		} else {
			throw new EntityNotFoundException("Coupon Policy not found with ID: " + id);
		}
	}

	@Override
	public void deleteCouponPolicy(Long id) {
		if (couponPolicyRepository.existsById(id)) {
			couponPolicyRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Coupon Policy not found with ID: " + id);
		}
	}
}