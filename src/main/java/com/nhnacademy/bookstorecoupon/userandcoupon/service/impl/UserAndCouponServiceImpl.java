package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public UserAndCouponServiceImpl(UserAndCouponRepository userAndCouponRepository,
		CouponTemplateRepository couponTemplateRepository) {
		this.userAndCouponRepository = userAndCouponRepository;
		this.couponTemplateRepository = couponTemplateRepository;
	}

	@Override
	public UserAndCouponResponseDTO createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO) {
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

		UserAndCoupon savedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

		return UserAndCouponResponseDTO.fromUserAndCoupon(savedUserAndCoupon);

	}

	@Override
	public void updateUserAndCoupon(Long userId) {
		UserAndCoupon userAndCoupon = userAndCouponRepository.getByUserId(userId);



		userAndCoupon.update(LocalDateTime.now(), true);
			//TODO :이걸 꼭 붙혀야하나?
		// userAndCouponRepository.save(userAndCoupon);


	}



	@Override
	@Transactional(readOnly = true)
	public Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(Long userId, String type, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 4;

		// 기본 조건: 모든 UserAndCoupon 가져오기
		Specification<UserAndCoupon> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

		// userId 조건 추가
		if (userId != null) {
			spec = spec.and((root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("userId"), userId));
		}

		// type 조건 추가
		if (type != null && !type.isEmpty()) {
			spec = spec.and((root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("couponPolicy").get("type"), type));
		}


		Page<UserAndCoupon> userAndCoupons = userAndCouponRepository.findAll(spec,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));


		return userAndCoupons.map(UserAndCouponResponseDTO::fromUserAndCoupon);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserAndCouponResponseDTO> getUserAndCouponByIdPaging(Long userId, Pageable pageable) {

		int page = pageable.getPageNumber() - 1;
		int pageSize = 4;


		// TODO 만약 없을경우 없는 것도  문제없이 페이지 띄우기

		Page<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByUserIdAndIsUsedFalse(userId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));


		return userAndCoupons.map(UserAndCouponResponseDTO::fromUserAndCoupon);
	}

}


