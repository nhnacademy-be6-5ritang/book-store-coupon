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
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyBanUpdateException;
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

		if (Boolean.TRUE.equals(requestDTO.isUsed())) {
			ErrorStatus errorStatus = ErrorStatus.from("이미 폐기된 쿠폰정책은 변경할 수 없습니다", HttpStatus.BAD_REQUEST, LocalDateTime.now());
			throw new CouponPolicyBanUpdateException(errorStatus);
		}

		if (optionalPolicy.isPresent()) {
			CouponPolicy policy = optionalPolicy.get();
			policy.update(
				requestDTO.minOrderPrice(),
				requestDTO.salePrice(),
				requestDTO.saleRate(),
				requestDTO.maxSalePrice(),
				requestDTO.isUsed()
			);

			if (Boolean.FALSE.equals(requestDTO.isUsed())) {
				List<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByCouponPolicy(policy);

				for (UserAndCoupon userAndCoupon : userAndCoupons) {
					userAndCoupon.update(userAndCoupon.getExpiredDate(), true);
				}
			}

		} else {
			ErrorStatus errorStatus = ErrorStatus.from(String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", id), HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new CouponPolicyNotFoundException(errorStatus);
		}

	}
}



