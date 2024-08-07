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
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponOrderResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.NotFoundUserAndCouponException;
import com.nhnacademy.bookstorecoupon.userandcoupon.exception.NotFoundUserBirthdayException;
import com.nhnacademy.bookstorecoupon.userandcoupon.feignclient.UserBirthdayFeignClient;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAndCouponServiceImpl implements UserAndCouponService {
	private static final int MAX_RETRY_ATTEMPTS = 3;
	private static final int RETRY_DELAY_MS = 5000; // 5초 대기

	private final UserAndCouponRepository userAndCouponRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;
	private final UserBirthdayFeignClient userBirthdayFeignClient;
	private final CouponPolicyRepository couponPolicyRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createUserWelcomeCouponIssue(Long userId) {
		CouponPolicy couponPolicy = couponPolicyRepository.findLatestCouponPolicyByType("welcome")
			.orElseThrow(() -> new CouponPolicyNotFoundException(ErrorStatus.from("최신 웰컴쿠폰 정책을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now())));

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
	@Scheduled(cron = "0 0 2 * * *")
	public void findExpiredCouponsScheduler() {
		log.warn("기한만료 쿠폰 스케쥴러 시작");

		try {
			// 실제 로직 수행
			findExpiredCoupons();
		} catch (Exception e) {
			log.error("기한만료 쿠폰 체크로직 실행 중 오류 발생", e);
			// 재시도 로직 호출
			retryLogic(1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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

	private void retryLogic(int attempt) {
		if (attempt > MAX_RETRY_ATTEMPTS) {
			log.error("최대 재시도 횟수 초과");
			// 재시도 실패 알림 전송 또는 다른 처리
			return;
		}

		try {
			Thread.sleep(RETRY_DELAY_MS); // 대기 시간
			log.warn("재시도 - 시도 " + attempt);

			new Thread(() -> {
				try {
					findExpiredCoupons();
				} catch (Exception e) {
					log.error("재시도 중 오류 발생", e);
					retryLogic(attempt + 1); // 다음 시도
				}
			}).start();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("재시도 중 오류 발생", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Scheduled(cron = "0 0 1 * * *")
	public void issueBirthdayCoupon() {
		log.warn("생일쿠폰발급 스케줄링 시작");

		LocalDate today = LocalDate.now();
		List<BirthdayCouponTargetResponse> birthdayList = Optional.ofNullable(userBirthdayFeignClient.getUsersWithBirthday(today).getBody())
			.orElseThrow(() -> new NotFoundUserBirthdayException( ErrorStatus.from("유저의 생일리스트가 존재하지 않습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now())));

		// 최신 생일 쿠폰 템플릿을 가져옴
		CouponPolicy couponPolicy = couponPolicyRepository.findLatestCouponPolicyByType("birthday")
			.orElseThrow(() -> new CouponPolicyNotFoundException(ErrorStatus.from("최신 생일쿠폰 정책을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now())));

		// 생일 목록에 있는 각 사용자에게 쿠폰을 발행
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserAndCouponResponseDTO> findCouponByOrder(
		Long userId, List<Long> bookIds, List<Long> categoryIds,  BigDecimal bookPrice) {
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();


		if (bookIds == null) {
			bookIds = new ArrayList<>();
		}
		if (categoryIds == null) {
			categoryIds = new ArrayList<>();
		}

		return userAndCouponRepository.findCouponByOrder(userId, bookIdMap, categoryIdMap, bookIds, categoryIds, bookPrice);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserAndCouponResponseDTO> findCouponByCartOrder(
		Long userId, List<GetBookByOrderCouponResponse> bookDetails) {
		Map<Long, BookCoupon.BookInfo> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();

		return userAndCouponRepository.findCouponByCartOrder(userId, bookIdMap, categoryIdMap, bookDetails);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCouponAfterPayment(Long userAndCouponId) {
		Optional<UserAndCoupon> optionalUserAndCoupon = userAndCouponRepository.findById(userAndCouponId);

		if (optionalUserAndCoupon.isPresent()) {
			UserAndCoupon userAndCoupon = optionalUserAndCoupon.get();
			userAndCoupon.update(LocalDateTime.now(), true);
		} else {

			 throw new NotFoundUserAndCouponException(ErrorStatus.from("유저의 쿠폰이 존재하지 않습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserAndCouponOrderResponseDTO findUserAndCouponsById(Long couponId) {
		UserAndCoupon userAndCoupon = userAndCouponRepository.findById(couponId).orElse(null);
		if (userAndCoupon == null) {
			throw new NotFoundUserAndCouponException(ErrorStatus.from("유저의 쿠폰이 존재하지 않습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now()));
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


