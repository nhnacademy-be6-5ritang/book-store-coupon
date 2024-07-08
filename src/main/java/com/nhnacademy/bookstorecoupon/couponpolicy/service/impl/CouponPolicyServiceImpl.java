package com.nhnacademy.bookstorecoupon.couponpolicy.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		UserAndCouponRepository userAndCouponRepository
	) {
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
	public void issueSpecificBookCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		couponPolicyRepository.save(couponPolicy);
		bookCouponRepository.save(BookCoupon.builder()
			.bookId(requestDTO.bookId())
			.bookTitle(requestDTO.bookTitle())
			.couponPolicy(couponPolicy)
			.build());

	}

	@Override
	public void issueSpecificCategoryCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		couponPolicyRepository.save(couponPolicy);
		categoryCouponRepository.save(CategoryCoupon.builder().categoryId(requestDTO.categoryId()).categoryName(
			requestDTO.categoryName()).couponPolicy(couponPolicy).build());

	}

	@Override
	public void issueDiscountCoupon(CouponPolicyRequestDTO requestDTO) {
		CouponPolicy couponPolicy = CouponPolicy.createFromRequestDTO(requestDTO);

		couponPolicyRepository.save(couponPolicy);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<CouponPolicyResponseDTO> getAllCouponPolicies(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();

		return couponPolicyRepository.findAllWithBooksAndCategories(PageRequest.of(page, pageSize), bookIdMap,
			categoryIdMap);
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

			// TODO 쿠폰정책이 사용폐기될경우 쿠폰사용됨처리, 다시 정책을 살리면 사용안됨처리 하는게 맞나?
			//  정책폐기시 front에서 경고창 띄우고 다시 정책을 사용하게 할 수 없게끔 로직짜는게 맞아보임.
			//  따라서 정책 isUsed = false 일경우  쿠폰 isUsed = true 로 하되 다시 반대로 할 수는 없게끔 조치
			if (requestDTO.isUsed() == Boolean.FALSE) {
				List<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByCouponPolicy(policy);

				for (UserAndCoupon userAndCoupon : userAndCoupons) {
					userAndCoupon.update(LocalDateTime.now(), true);
				}

			} else {
				List<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByCouponPolicy(policy);

				for (UserAndCoupon userAndCoupon : userAndCoupons) {
					userAndCoupon.update(null, false);
				}

			}

		} else {
			String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new CouponPolicyNotFoundException(errorStatus);
		}

	}
}


