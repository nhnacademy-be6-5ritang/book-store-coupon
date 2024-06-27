package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
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
			.couponPolicyId(couponTemplate.getCouponPolicy())
			.userId(requestDTO.userId())
			.isUsed(requestDTO.isUsed())
			.expiredDate(couponTemplate.getExpiredDate())
			.issueDate(couponTemplate.getIssueDate())
			.build();

		UserAndCoupon savedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

		return new UserAndCouponResponseDTO(
			savedUserAndCoupon.getId(),
			new CouponPolicyResponseDTO(
					savedUserAndCoupon.getCouponPolicyId().getId(),
					savedUserAndCoupon.getCouponPolicyId().getMinOrderPrice(),
					savedUserAndCoupon.getCouponPolicyId().getSalePrice(),
					savedUserAndCoupon.getCouponPolicyId().getSaleRate(),
					savedUserAndCoupon.getCouponPolicyId().getMaxSalePrice(),
					savedUserAndCoupon.getCouponPolicyId().getType()
				),
			savedUserAndCoupon.getUserId(),
			savedUserAndCoupon.getUsedDate(),
			savedUserAndCoupon.getIsUsed(),
			savedUserAndCoupon.getExpiredDate(),
			savedUserAndCoupon.getIssueDate()
		);

	}

	// TODO : 이게 dto가 필요할까? ! ?  그냥 여기서 로직으로 바꾸면 되는거 아닐까 ?
	@Override
	public UserAndCouponResponseDTO updateUserAndCoupon(Long userId) {
		UserAndCoupon userAndCoupon = userAndCouponRepository.getByUserId(userId);

		String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		userAndCoupon.update(LocalDateTime.parse(formattedDateTime), true);

		UserAndCoupon updatedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

		return new UserAndCouponResponseDTO(
			updatedUserAndCoupon.getId(),
			new CouponPolicyResponseDTO(
				updatedUserAndCoupon.getCouponPolicyId().getId(),
				updatedUserAndCoupon.getCouponPolicyId().getMinOrderPrice(),
				updatedUserAndCoupon.getCouponPolicyId().getSalePrice(),
				updatedUserAndCoupon.getCouponPolicyId().getSaleRate(),
				updatedUserAndCoupon.getCouponPolicyId().getMaxSalePrice(),
				updatedUserAndCoupon.getCouponPolicyId().getType()
			),
			updatedUserAndCoupon.getUserId(),
			updatedUserAndCoupon.getUsedDate(),
			updatedUserAndCoupon.getIsUsed(),
			updatedUserAndCoupon.getExpiredDate(),
			updatedUserAndCoupon.getIssueDate()
		);

	}

	// @Override
	// @Transactional(readOnly = true)
	// public Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(Pageable pageable) {
	//
	// 	int page = pageable.getPageNumber() - 1;
	// 	int pageSize = 4;
	//
	// 	Page<UserAndCoupon> userAndCoupons = userAndCouponRepository.findAll(
	// 		PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
	//
	// 	return userAndCoupons.map(userAndCoupon -> new UserAndCouponResponseDTO(userAndCoupon.getId(),
	// 		new CouponResponseDTO(userAndCoupon.getCoupon().getId(),
	// 			new CouponPolicyResponseDTO(
	// 				userAndCoupon.getCoupon().getCouponPolicy().getId(),
	// 				userAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
	// 				userAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
	// 				userAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
	// 				userAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
	// 				userAndCoupon.getCoupon().getCouponPolicy().getType()),
	// 			userAndCoupon.getCoupon().getExpiredDate(),
	// 			userAndCoupon.getCoupon().getIssueDate()),
	// 		userAndCoupon.getUserEmail(),
	// 		userAndCoupon.getUsedDate(),
	// 		userAndCoupon.getIsUsed()));
	// }


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
				criteriaBuilder.equal(root.get("couponPolicyId").get("type"), type));
		}


		Page<UserAndCoupon> userAndCoupons = userAndCouponRepository.findAll(spec,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));


		return userAndCoupons.map(userAndCoupon -> new UserAndCouponResponseDTO(userAndCoupon.getId(),
			new CouponPolicyResponseDTO(userAndCoupon.getCouponPolicyId().getId(),
				userAndCoupon.getCouponPolicyId().getMinOrderPrice(),
				userAndCoupon.getCouponPolicyId().getSalePrice(),
				userAndCoupon.getCouponPolicyId().getSaleRate(),
				userAndCoupon.getCouponPolicyId().getMaxSalePrice(),
				userAndCoupon.getCouponPolicyId().getType()),
			userAndCoupon.getUserId(),
			userAndCoupon.getUsedDate(),
			userAndCoupon.getIsUsed(),
			userAndCoupon.getExpiredDate(),
			userAndCoupon.getIssueDate()));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserAndCouponResponseDTO> getUserAndCouponByIdPaging(Long userId, Pageable pageable) {

		int page = pageable.getPageNumber() - 1;
		int pageSize = 4;
		Page<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByUserId(userId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));

		return userAndCoupons.map(userAndCoupon -> new UserAndCouponResponseDTO(userAndCoupon.getId(),
			new CouponPolicyResponseDTO(userAndCoupon.getCouponPolicyId().getId(),
					userAndCoupon.getCouponPolicyId().getMinOrderPrice(),
					userAndCoupon.getCouponPolicyId().getSalePrice(),
					userAndCoupon.getCouponPolicyId().getSaleRate(),
					userAndCoupon.getCouponPolicyId().getMaxSalePrice(),
					userAndCoupon.getCouponPolicyId().getType()),
				userAndCoupon.getUserId(),
				userAndCoupon.getUsedDate(),
				userAndCoupon.getIsUsed(),
				userAndCoupon.getExpiredDate(),
				userAndCoupon.getIssueDate()));
	}

}


