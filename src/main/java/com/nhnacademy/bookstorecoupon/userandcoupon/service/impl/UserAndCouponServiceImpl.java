package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;
import com.nhnacademy.bookstorecoupon.coupon.domain.entity.Coupon;
import com.nhnacademy.bookstorecoupon.coupon.repository.CouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponCreateRequestDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.repository.UserAndCouponRepository;
import com.nhnacademy.bookstorecoupon.userandcoupon.service.UserAndCouponService;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class UserAndCouponServiceImpl implements UserAndCouponService {

	private final UserAndCouponRepository userAndCouponRepository;
	private final CouponRepository couponRepository;

	public UserAndCouponServiceImpl(UserAndCouponRepository userAndCouponRepository,
		CouponRepository couponRepository) {
		this.userAndCouponRepository = userAndCouponRepository;
		this.couponRepository = couponRepository;
	}

	@Override
	public UserAndCouponResponseDTO createUserAndCoupon(Long couponId, UserAndCouponCreateRequestDTO requestDTO) {

		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + couponId));

		UserAndCoupon userAndCoupon = UserAndCoupon.builder()
			.coupon(coupon)
			.userEmail(requestDTO.userEmail())
			.isUsed(requestDTO.isUsed())
			.build();

		UserAndCoupon savedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

		return new UserAndCouponResponseDTO(
			savedUserAndCoupon.getId(),
			new CouponResponseDTO(
				savedUserAndCoupon.getCoupon().getId(),
				new CouponPolicyResponseDTO(
					savedUserAndCoupon.getCoupon().getCouponPolicy().getId(),
					savedUserAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
					savedUserAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
					savedUserAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
					savedUserAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
					savedUserAndCoupon.getCoupon().getCouponPolicy().getType()
				),
				savedUserAndCoupon.getCoupon().getExpiredDate(),
				savedUserAndCoupon.getCoupon().getIssueDate()
			),
			savedUserAndCoupon.getUserEmail(),
			savedUserAndCoupon.getUsedDate(),
			savedUserAndCoupon.getIsUsed()
		);

	}

	// TODO : 이게 dto가 필요할까? ! ?  그냥 여기서 로직으로 바꾸면 되는거 아닐까 ?
	@Override
	public UserAndCouponResponseDTO updateUserAndCoupon(String userId) {
		UserAndCoupon userAndCoupon = userAndCouponRepository.getByUserEmail(userId);

		String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		userAndCoupon.update(LocalDateTime.parse(formattedDateTime), true);

		UserAndCoupon updatedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

		return new UserAndCouponResponseDTO(
			updatedUserAndCoupon.getId(),
			new CouponResponseDTO(
				updatedUserAndCoupon.getCoupon().getId(),
				new CouponPolicyResponseDTO(
					updatedUserAndCoupon.getCoupon().getCouponPolicy().getId(),
					updatedUserAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
					updatedUserAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
					updatedUserAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
					updatedUserAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
					updatedUserAndCoupon.getCoupon().getCouponPolicy().getType()
				),
				updatedUserAndCoupon.getCoupon().getExpiredDate(),
				updatedUserAndCoupon.getCoupon().getIssueDate()
			),
			updatedUserAndCoupon.getUserEmail(),
			updatedUserAndCoupon.getUsedDate(),
			updatedUserAndCoupon.getIsUsed()
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
	public Page<UserAndCouponResponseDTO> getAllUserAndCouponPaging(String userEmail, String type, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 4;

		// 기본 조건: 모든 UserAndCoupon 가져오기
		Specification<UserAndCoupon> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

		// userEmail 조건 추가
		if (userEmail != null && !userEmail.isEmpty()) {
			spec = spec.and((root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("userEmail"), userEmail));
		}

		// type 조건 추가
		if (type != null && !type.isEmpty()) {
			spec = spec.and((root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("coupon").get("couponPolicy").get("type"), type));
		}

		Page<UserAndCoupon> userAndCoupons = userAndCouponRepository.findAll(spec,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));

		return userAndCoupons.map(userAndCoupon -> new UserAndCouponResponseDTO(userAndCoupon.getId(),
			new CouponResponseDTO(userAndCoupon.getCoupon().getId(),
				new CouponPolicyResponseDTO(
					userAndCoupon.getCoupon().getCouponPolicy().getId(),
					userAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
					userAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
					userAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
					userAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
					userAndCoupon.getCoupon().getCouponPolicy().getType()),
				userAndCoupon.getCoupon().getExpiredDate(),
				userAndCoupon.getCoupon().getIssueDate()),
			userAndCoupon.getUserEmail(),
			userAndCoupon.getUsedDate(),
			userAndCoupon.getIsUsed()));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserAndCouponResponseDTO> getUserAndCouponByIdPaging(String userEmail, Pageable pageable) {

		int page = pageable.getPageNumber() - 1;
		int pageSize = 4;
		Page<UserAndCoupon> userAndCoupons = userAndCouponRepository.findByUserEmail(userEmail,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));

		return userAndCoupons.map(userAndCoupon -> new UserAndCouponResponseDTO(userAndCoupon.getId(),
			new CouponResponseDTO(userAndCoupon.getCoupon().getId(),
				new CouponPolicyResponseDTO(
					userAndCoupon.getCoupon().getCouponPolicy().getId(),
					userAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
					userAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
					userAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
					userAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
					userAndCoupon.getCoupon().getCouponPolicy().getType()),
				userAndCoupon.getCoupon().getExpiredDate(),
				userAndCoupon.getCoupon().getIssueDate()),
			userAndCoupon.getUserEmail(),
			userAndCoupon.getUsedDate(),
			userAndCoupon.getIsUsed()));
	}

}


