package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.repository.CategoryCouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponNotFoundException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.feignclient.UserBirthdayFeignClient;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserAndCouponServiceImpl implements UserAndCouponService {

	private final UserAndCouponRepository userAndCouponRepository;
	private final CouponTemplateRepository couponTemplateRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;
	private final UserBirthdayFeignClient userBirthdayFeignClient;
	private final CouponPolicyRepository couponPolicyRepository;

	public UserAndCouponServiceImpl(UserAndCouponRepository userAndCouponRepository,
		CouponTemplateRepository couponTemplateRepository, BookCouponRepository bookCouponRepository,
		CategoryCouponRepository categoryCouponRepository,
		UserBirthdayFeignClient userBirthdayFeignClient, CouponPolicyRepository couponPolicyRepository) {
		this.userAndCouponRepository = userAndCouponRepository;
		this.couponTemplateRepository = couponTemplateRepository;
		this.bookCouponRepository = bookCouponRepository;
		this.categoryCouponRepository = categoryCouponRepository;
		this.userBirthdayFeignClient = userBirthdayFeignClient;
		this.couponPolicyRepository = couponPolicyRepository;
	}

	// @Override
	// public void createUserAndCoupon(Long couponId, Long userId) {
	// 	String errorMessage = String.format("해당 쿠폰은 '%d'는 존재하지 않습니다.", couponId);
	// 	ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
	//
	// 	CouponTemplate couponTemplate = couponTemplateRepository.findById(couponId)
	// 		.orElseThrow(() -> new CouponNotFoundException(errorStatus));
	//
	// 	UserAndCoupon userAndCoupon = UserAndCoupon.builder()
	// 		.couponPolicy(couponTemplate.getCouponPolicy())
	// 		.userId(userId)
	// 		.isUsed(false)
	// 		.expiredDate(couponTemplate.getExpiredDate())
	// 		.issueDate(couponTemplate.getIssueDate())
	// 		.build();
	//
	// 	userAndCouponRepository.save(userAndCoupon);
	//
	// }

	@Override
	public void createUserWelcomeCouponIssue(Long userId) {

		String errorMessage = "최신 웰컴쿠폰 정책을 찾을 수 없습니다.";
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());

		CouponPolicy couponPolicy = couponPolicyRepository.findLatestCouponPolicyByType("welcome")
			.orElseThrow(() -> new CouponNotFoundException(errorStatus));

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponPolicy)
			.userId(userId)
			.isUsed(false)
			.expiredDate(LocalDateTime.now().plusDays(365))
			.issueDate(LocalDateTime.now())
			.build();

		userAndCouponRepository.save(userAndCoupon);

	}

	// 쿠폰 만료처리 스케줄링   매일 새벽 2시 시작 설정
	@Override
	@Scheduled(cron = "0 0 2 * * *")
	public void findExpiredCoupons() {
		log.warn("기한만료 쿠폰 체크로직 발동");
		LocalDateTime now = LocalDateTime.now();

		// 오늘 자정 이후에 만료된 쿠폰들을 조회하여 처리
		List<UserAndCoupon> expiredCoupons = userAndCouponRepository.findByExpiredDateBeforeAndIsUsedIsFalse(now);

		for (UserAndCoupon coupon : expiredCoupons) {
			coupon.update(coupon.getExpiredDate(), true);
		}

		log.warn("기한만료 쿠폰 체크로직 실행완료");
	}

	// TODO :생일쿠폰 발급 스케줄링, 생일 설정되면 할 예정
	@Override
	@Scheduled(cron = "0 0 1 * * *")
	public void issueBirthdayCoupon() {
		log.warn("생일쿠폰발급 스케줄링 시작");

		LocalDate today = LocalDate.now();
		List<BirthdayCouponTargetResponse> birthdayList = userBirthdayFeignClient.getUsersWithBirthday(today).getBody();

		String errorMessage = "최신 생일쿠폰 정책을 찾을 수 없습니다.";
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());

		// 최신 생일 쿠폰 템플릿을 가져옴
		CouponPolicy couponPolicy = couponPolicyRepository.findLatestCouponPolicyByType("birthday")
			.orElseThrow(() -> new CouponNotFoundException(errorStatus));

		// 생일 목록에 있는 각 사용자에게 쿠폰을 발행

		assert birthdayList != null;
		birthdayList.forEach(user -> {

			UserAndCoupon userAndCoupon = UserAndCoupon.builder()
				.couponPolicy(couponPolicy)
				.userId(user.userId())
				.isUsed(false)
				.expiredDate(LocalDateTime.now().plusDays(365)) // 만료 날짜를 생성일로부터 365일 후로 설정
				.issueDate(LocalDateTime.now())
				.build();

			userAndCouponRepository.save(userAndCoupon);
		});

		log.warn("생일쿠폰발급 스케줄링 끝");
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByUserPaging(Long userId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();
		return userAndCouponRepository.findAllByUserPaging(PageRequest.of(page, pageSize), userId, bookIdMap,
			categoryIdMap);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByManagerPaging(Pageable pageable, String type,
		Long userId) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();
		return userAndCouponRepository.findAllByManagerPaging(PageRequest.of(page, pageSize), type, userId, bookIdMap,
			categoryIdMap);
	}

	@Override
	public List<UserAndCouponResponseDTO> findCouponByOrder(
		Long userId, List<Long> bookIds, List<Long> categoryIds,  BigDecimal bookPrice) {
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();

		// Ensure bookIds and categoryIds are not null
		if (bookIds == null) {
			bookIds = new ArrayList<>();
		}
		if (categoryIds == null) {
			categoryIds = new ArrayList<>();
		}

		return userAndCouponRepository.findCouponByOrder(userId, bookIdMap, categoryIdMap, bookIds, categoryIds, bookPrice);
	}

	@Override
	public void updateCouponAfterPayment(Long userAndCouponId) {
		Optional<UserAndCoupon> optionalUserAndCoupon = userAndCouponRepository.findById(userAndCouponId);

		if (optionalUserAndCoupon.isPresent()) {
			UserAndCoupon userAndCoupon = optionalUserAndCoupon.get();
			userAndCoupon.update(LocalDateTime.now(), true);
		} else {
			throw new IllegalArgumentException("Coupon not found with id: " + userAndCouponId);
		}
	}

	@Override
	public UserAndCouponOrderResponseDTO findUserAndCouponsById(Long couponId) {
		UserAndCoupon userAndCoupon = userAndCouponRepository.findById(couponId).orElse(null);
		if (userAndCoupon == null) {
			return null;
		}
		return new UserAndCouponOrderResponseDTO(
			userAndCoupon.getId(),
			userAndCoupon.getCouponPolicy().getMinOrderPrice(),
			userAndCoupon.getCouponPolicy().getSalePrice(),
			userAndCoupon.getCouponPolicy().getSaleRate(),
			userAndCoupon.getCouponPolicy().getMaxSalePrice(),
			userAndCoupon.getCouponPolicy().getType()
		);
	}

}


