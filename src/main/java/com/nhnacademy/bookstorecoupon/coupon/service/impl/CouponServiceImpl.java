package com.nhnacademy.bookstorecoupon.coupon.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.coupon.domain.entity.Coupon;
import com.nhnacademy.bookstorecoupon.coupon.dto.CouponDTO;
import com.nhnacademy.bookstorecoupon.coupon.repository.CouponRepository;

@Service
public class CouponServiceImpl {

	private final UsersAndCouponsRepository usersAndCouponsRepository;
	private final CouponRepository couponRepository;

	public CouponServiceImpl(UsersAndCouponsRepository usersAndCouponsRepository, CouponRepository couponRepository) {
		this.usersAndCouponsRepository = usersAndCouponsRepository;
		this.couponRepository = couponRepository;
	}

	@Transactional(readOnly = true)
	public List<CouponDTO> getAvailableCouponsForUser(Long userId) {
		return usersAndCouponsRepository.findByUserIdAndIsUsed(userId, false).stream()
			.map(this::convertToCouponDTO)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<CouponUsageDTO> getUsedCouponsForUser(Long userId) {
		return usersAndCouponsRepository.findByUserIdAndIsUsed(userId, true).stream()
			.map(this::convertToCouponUsageDTO)
			.collect(Collectors.toList());
	}

	@Transactional
	public void applyCoupon(Long userId, Long couponId) {
		UserAndCoupon userCoupon = usersAndCouponsRepository.findByUserIdAndCouponIdAndIsUsed(userId, couponId, false)
			.orElseThrow(() -> new RuntimeException("Coupon not found or already used"));

		userCoupon.setIsUsed(true);
		userCoupon.setUsedDate(new Date());
		usersAndCouponsRepository.save(userCoupon);
	}

	@Transactional
	public void expireOldCoupons() {
		Date today = new Date();
		List<Coupon> expiredCoupons = couponRepository.findByExpiredDateBefore(today);
		for (Coupon coupon : expiredCoupons) {
			// Implement expiration logic (e.g., removing or marking expired)
		}
	}

	private CouponDTO convertToCouponDTO(UserAndCoupon userCoupon) {
		Coupon coupon = userCoupon.getCoupon();
		CouponPolicy policy = coupon.getCouponPolicy();

		return CouponDTO.builder()
			.id(coupon.getId())
			.policyId(policy.getId())
			.issueDate(coupon.getIssueDate())
			.expiredDate(coupon.getExpiredDate())
			.policyType(policy.getType())
			.description("Min Order Price: " + policy.getMinOrderPrice() +
				", Sale Price: " + policy.getSalePrice() +
				", Sale Rate: " + policy.getSaleRate() +
				", Max Sale Price: " + policy.getMaxSalePrice())
			.build();
	}

	private CouponUsageDTO convertToCouponUsageDTO(UserAndCoupon userCoupon) {
		return CouponUsageDTO.builder()
			.id(userCoupon.getId())
			.userId(userCoupon.getUserId())
			.couponId(userCoupon.getCoupon().getId())
			.usedDate(userCoupon.getUsedDate())
			.isUsed(userCoupon.getIsUsed())
			.build();
	}

	@Override
	public List<UserCouponDTO> getAvailableCoupons(Long userId) {
		return usersAndCouponsRepository.findByUserIdAndIsUsedFalse(userId)
			.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}

	@Override
	public List<UserCouponDTO> getUsedCoupons(Long userId) {
		return usersAndCouponsRepository.findByUserIdAndIsUsedTrue(userId)
			.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}

	private UserCouponDTO toDTO(UsersAndCoupons usersAndCoupons) {
		return UserCouponDTO.builder()
			.couponId(usersAndCoupons.getCoupon().getId())
			.policyType(usersAndCoupons.getCoupon().getCouponPolicy().getType())
			.minOrderPrice(usersAndCoupons.getCoupon().getCouponPolicy().getMinOrderPrice())
			.salePrice(usersAndCoupons.getCoupon().getCouponPolicy().getSalePrice())
			.saleRate(usersAndCoupons.getCoupon().getCouponPolicy().getSaleRate())
			.maxSalePrice(usersAndCoupons.getCoupon().getCouponPolicy().getMaxSalePrice())
			.issueDate(usersAndCoupons.getCoupon().getIssueDate())
			.expiredDate(usersAndCoupons.getCoupon().getExpiredDate())
			.isUsed(usersAndCoupons.getIsUsed())
			.usedDate(usersAndCoupons.getUsedDate())
			.build();
	}

}

