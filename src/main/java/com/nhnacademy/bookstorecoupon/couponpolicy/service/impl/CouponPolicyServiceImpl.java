package com.nhnacademy.bookstorecoupon.couponpolicy.service.impl;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.request.CouponPolicyRequestDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.service.CouponPolicyService;

import jakarta.transaction.Transactional;

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
	public void issueWelcomeCoupon(CouponPolicyRequestDTO requestDTO) {
		// 비즈니스 로직 처리
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.getMinOrderPrice())
			.salePrice(requestDTO.getSalePrice())
			.saleRate(requestDTO.getSaleRate())
			.maxSalePrice(requestDTO.getMaxSalePrice())
			.type(requestDTO.getType())
			.build();

		// TODO 1:같은정책있는지 체크!
		couponPolicyRepository.save(couponPolicy);

	}

	@Override
	public void issueBirthdayCoupon(CouponPolicyRequestDTO requestDTO) {
		// 비즈니스 로직 처리
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.getMinOrderPrice())
			.salePrice(requestDTO.getSalePrice())
			.saleRate(requestDTO.getSaleRate())
			.maxSalePrice(requestDTO.getMaxSalePrice())
			.type(requestDTO.getType())
			.build();

		// TODO 1:같은정책있는지 체크!
		couponPolicyRepository.save(couponPolicy);

	}

	@Override
	public void issueSpecificBookCoupon(Long bookId, CouponPolicyRequestDTO requestDTO) {
		// 비즈니스 로직 처리
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.getMinOrderPrice())
			.salePrice(requestDTO.getSalePrice())
			.saleRate(requestDTO.getSaleRate())
			.maxSalePrice(requestDTO.getMaxSalePrice())
			.type(requestDTO.getType())
			.build();

		// TODO 1:같은정책있는지 체크!
		couponPolicyRepository.save(couponPolicy);
		// BookCoupon 객체 생성 및 저장
		bookCouponRepository.save(BookCoupon.builder().bookId(bookId).couponPolicy(couponPolicy).build());
		// Coupon 객체 생성 및 반환

	}

	@Override
	public void issueSpecificCategoryCoupon(Long categoryId, CouponPolicyRequestDTO requestDTO) {
		// 비즈니스 로직 처리
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.getMinOrderPrice())
			.salePrice(requestDTO.getSalePrice())
			.saleRate(requestDTO.getSaleRate())
			.maxSalePrice(requestDTO.getMaxSalePrice())
			.type(requestDTO.getType())
			.build();

		// TODO 1:같은정책있는지 체크!
		couponPolicyRepository.save(couponPolicy);
		// CategoryCoupon 객체 생성 및 저장
		// TODO 2: @builder 왜써야하는지 공부
		categoryCouponRepository.save(new CategoryCoupon(couponPolicy, categoryId));
		// Coupon 객체 생성 및 반환

	}

	@Override
	public void issueDiscountCoupon(CouponPolicyRequestDTO requestDTO) {
		// 비즈니스 로직 처리
		CouponPolicy couponPolicy = CouponPolicy.builder()
			.minOrderPrice(requestDTO.getMinOrderPrice())
			.salePrice(requestDTO.getSalePrice())
			.saleRate(requestDTO.getSaleRate())
			.maxSalePrice(requestDTO.getMaxSalePrice())
			.type(requestDTO.getType())
			.build();

		// TODO 1:같은정책있는지 체크!
		couponPolicyRepository.save(couponPolicy);
		// Coupon 객체 생성 및 반환
	}
}
