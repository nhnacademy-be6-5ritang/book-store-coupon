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
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.feignclient.UserAndCouponFeignClient;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

@Service
@Transactional
public class UserAndCouponServiceImpl implements UserAndCouponService {

	private final UserAndCouponRepository userAndCouponRepository;
	private final CouponTemplateRepository couponTemplateRepository;
	private final BookCouponRepository bookCouponRepository;
	private final CategoryCouponRepository categoryCouponRepository;
	private final UserAndCouponFeignClient userAndCouponFeignClient;



	public UserAndCouponServiceImpl(UserAndCouponRepository userAndCouponRepository,
		CouponTemplateRepository couponTemplateRepository, BookCouponRepository bookCouponRepository, CategoryCouponRepository categoryCouponRepository,
	UserAndCouponFeignClient userAndCouponFeignClient) {
		this.userAndCouponRepository = userAndCouponRepository;
		this.couponTemplateRepository = couponTemplateRepository;
		this.bookCouponRepository = bookCouponRepository;
		this.categoryCouponRepository = categoryCouponRepository;
		this.userAndCouponFeignClient=userAndCouponFeignClient;
	}

	@Override
	public void createUserAndCoupon(Long couponId, Long userId) {
		String errorMessage = String.format("해당 쿠폰은 '%d'는 존재하지 않습니다.", couponId);
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());



		CouponTemplate couponTemplate = couponTemplateRepository.findById(couponId)
			.orElseThrow(() -> new CouponNotFoundException(errorStatus));

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponTemplate.getCouponPolicy())
			.userId(userId)
			.isUsed(false)
			.expiredDate(couponTemplate.getExpiredDate())
			.issueDate(couponTemplate.getIssueDate())
			.build();

		userAndCouponRepository.save(userAndCoupon);


	}

	@Override
	public void createUserWelcomeCouponIssue(Long userId) {

		String errorMessage = "최신 웰컴쿠폰 템플릿을 찾을 수 없습니다.";
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());


		CouponTemplate couponTemplate = couponTemplateRepository.findLatestWelcomeCouponTemplate()
			.orElseThrow(() -> new CouponNotFoundException(errorStatus));

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.couponPolicy(couponTemplate.getCouponPolicy())
			.userId(userId)
			.isUsed(false)
			.expiredDate(couponTemplate.getExpiredDate())
			.issueDate(couponTemplate.getIssueDate())
			.build();

		userAndCouponRepository.save(userAndCoupon);


	}


//임시방편용....  0 0 0 1/1 * ? *  매일 00 시 시작 설정
// 	@Override
// 	@Scheduled(cron = "*/10 * * * * * ")
// 	public void findExpiredCoupons() {
// 		LocalDateTime now = LocalDateTime.now();
//
// 		// 오늘 자정 이후에 만료된 쿠폰들을 조회하여 처리
// 		List<UserAndCoupon> expiredCoupons = userAndCouponRepository.findByExpiredDateBeforeAndIsUsedIsFalse(now);
//
// 		for (UserAndCoupon coupon : expiredCoupons) {
// 				coupon.update(coupon.getExpiredDate(), true);
// 		}
//
// 	}

//	@Override
// 	@Scheduled(cron = "*/10 * * * * * ")
// 	public void issueBirthdayCoupon() {
// 		LocalDate today = LocalDate.now();
// 		List<BirthdayCouponTargetResponse> birthdayList = userAndCouponFeignClient.getUsersWithBirthday(today).getBody();
//
// 		// 최신 생일 쿠폰 템플릿을 가져옴
// 		CouponTemplate couponTemplate = couponTemplateRepository.findLatestBirthdayCouponTemplate()
// 			.orElseThrow(() -> new IllegalStateException("생일 쿠폰 템플릿이 없습니다."));
//
// 		// 생일 목록에 있는 각 사용자에게 쿠폰을 발행
// 		assert birthdayList != null;
// 		birthdayList.forEach(user -> {
//
// 			UserAndCoupon userAndCoupon = UserAndCoupon.builder()
// 				.couponPolicy(couponTemplate.getCouponPolicy())
// 				.userId(user.userId())
// 				.isUsed(false)
// 				.expiredDate(couponTemplate.getExpiredDate())
// 				.issueDate(couponTemplate.getIssueDate())
// 				.build();
//
// 			userAndCouponRepository.save(userAndCoupon);
// 		});
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


