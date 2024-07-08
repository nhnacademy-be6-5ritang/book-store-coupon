package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

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
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponNotFoundException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

@Service
@Transactional
public class UserAndCouponServiceImpl implements UserAndCouponService {

	private final UserAndCouponRepository userAndCouponRepository;
	private final CouponTemplateRepository couponTemplateRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;

	public UserAndCouponServiceImpl(UserAndCouponRepository userAndCouponRepository,
		CouponTemplateRepository couponTemplateRepository, BookCouponRepository bookCouponRepository, CategoryCouponRepository categoryCouponRepository) {
		this.userAndCouponRepository = userAndCouponRepository;
		this.couponTemplateRepository = couponTemplateRepository;
		this.bookCouponRepository = bookCouponRepository;
		this.categoryCouponRepository = categoryCouponRepository;
	}

	@Override
	public void createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO) {
		String errorMessage = String.format("해당 쿠폰은 '%d'는 존재하지 않습니다.", couponId);
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());



		CouponTemplate couponTemplate = couponTemplateRepository.findById(couponId)
			.orElseThrow(() -> new CouponNotFoundException(errorStatus));

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponTemplate.getCouponPolicy())
			.userId(requestDTO.userId())
			.isUsed(requestDTO.isUsed())
			.expiredDate(couponTemplate.getExpiredDate())
			.issueDate(couponTemplate.getIssueDate())
			.build();

		userAndCouponRepository.save(userAndCoupon);


	}

// 임시방편용....  0 0 0 1/1 * ? *  매일 00 시 시작 설정
// 	@Override
// 	@Scheduled(cron = "*/10 * * * * * ")
// 	public void findExpiredCoupons() {
// 		LocalDateTime now = LocalDateTime.now();
//
// 		// 오늘 자정 이후에 만료된 쿠폰들을 조회하여 처리
// 		List<UserAndCoupon> expiredCoupons = userAndCouponRepository.findByExpiredDateBeforeAndIsUsedIsFalse(now);
//
// 		for (UserAndCoupon coupon : expiredCoupons) {
// 				coupon.update(now, true);
// 		}
//
// 	}




	@Override
	public Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByUserPaging(Long userId, Pageable pageable) {
		int page= Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize=pageable.getPageSize();
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();
		return userAndCouponRepository.findAllByUserPaging(PageRequest.of(page, pageSize), userId,bookIdMap, categoryIdMap);
	}

	@Override
	public Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByManagerPaging(Pageable pageable, String type, Long userId) {
		int page= Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize=pageable.getPageSize();
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();
		return userAndCouponRepository.findAllByManagerPaging(PageRequest.of(page, pageSize), type , userId,bookIdMap,  categoryIdMap);
	}





}


