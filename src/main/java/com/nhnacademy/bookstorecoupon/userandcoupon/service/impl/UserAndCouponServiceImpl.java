package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.bookcoupon.repository.BookCouponRepository;
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

	@Override
	public void updateUserAndCoupon(Long userId) {
		UserAndCoupon userAndCoupon = userAndCouponRepository.getByUserId(userId);



		userAndCoupon.update(LocalDateTime.now(), true);
			//TODO :이걸 꼭 붙혀야하나?
		// userAndCouponRepository.save(userAndCoupon);


	}

	@Override
	public Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByUserPaging(Long userId, Pageable pageable) {
		int page=pageable.getPageNumber()-1;
		int pageSize=pageable.getPageSize();
		Map<Long, Long> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, Long> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();
		return userAndCouponRepository.findAllByUserPaging(PageRequest.of(page, pageSize), userId,bookIdMap, categoryIdMap);
	}

	@Override
	public Page<UserAndCouponResponseDTO> getAllUsersAndCouponsByManagerPaging(Pageable pageable, String type, Long userId) {
		int page=pageable.getPageNumber()-1;
		int pageSize=pageable.getPageSize();
		Map<Long, Long> bookIdMap = bookCouponRepository.fetchBookIdMap();
		Map<Long, Long> categoryIdMap = categoryCouponRepository.fetchCategoryIdMap();
		return userAndCouponRepository.findAllByManagerPaging(PageRequest.of(page, pageSize), type , userId,bookIdMap,  categoryIdMap);
	}





}


