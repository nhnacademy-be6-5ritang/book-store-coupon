package com.nhnacademy.bookstorecoupon.coupon.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.coupon.domain.dto.request.CouponRequestDTO;
import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;
import com.nhnacademy.bookstorecoupon.coupon.domain.entity.Coupon;
import com.nhnacademy.bookstorecoupon.coupon.repository.CouponRepository;
import com.nhnacademy.bookstorecoupon.coupon.service.CouponService;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;
	private final CouponPolicyRepository couponPolicyRepository;

	public CouponServiceImpl(CouponRepository couponRepository, CouponPolicyRepository couponPolicyRepository) {
		this.couponRepository = couponRepository;
		this.couponPolicyRepository = couponPolicyRepository;
	}

	@Override
	public CouponResponseDTO createCoupon(CouponRequestDTO requestDTO) {
		CouponPolicy couponPolicy = couponPolicyRepository.findById(requestDTO.couponPolicyId())
			.orElseThrow(
				() -> new EntityNotFoundException("Coupon Policy not found with ID: " + requestDTO.couponPolicyId()));

		Coupon coupon = Coupon.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(requestDTO.expiredDate())
			.issueDate(requestDTO.issueDate())
			.build();

		couponRepository.save(coupon);


		Coupon savedCoupon = couponRepository.save(coupon);

		return new CouponResponseDTO(
			savedCoupon.getId(),
			new CouponPolicyResponseDTO(
				savedCoupon.getCouponPolicy().getId(),
				savedCoupon.getCouponPolicy().getMinOrderPrice(),
				savedCoupon.getCouponPolicy().getSalePrice(),
				savedCoupon.getCouponPolicy().getSaleRate(),
				savedCoupon.getCouponPolicy().getMaxSalePrice(),
				savedCoupon.getCouponPolicy().getType()
			),
			savedCoupon.getExpiredDate(),
			savedCoupon.getIssueDate()
		);
	}



	@Override
	@Transactional(readOnly = true)
	public List<CouponResponseDTO> getAllCoupons() {
		List<Coupon> coupons = couponRepository.findAll();
		return coupons.stream()
			.map(coupon -> new CouponResponseDTO(coupon.getId(),
				new CouponPolicyResponseDTO(
					coupon.getCouponPolicy().getId(),
					coupon.getCouponPolicy().getMinOrderPrice(),
					coupon.getCouponPolicy().getSalePrice(),
					coupon.getCouponPolicy().getSaleRate(),
					coupon.getCouponPolicy().getMaxSalePrice(),
					coupon.getCouponPolicy().getType()),
				coupon.getExpiredDate(),
				coupon.getIssueDate()))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CouponResponseDTO> getAllCouponPaging(Pageable pageable) {
		int page=pageable.getPageNumber()-1;
		int pageSize=4;



		Page<Coupon> coupons = couponRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
		Page<CouponResponseDTO> couponResponseDTOs = coupons.map(coupon -> new CouponResponseDTO(coupon.getId(), new CouponPolicyResponseDTO(coupon.getCouponPolicy().getId(),
			coupon.getCouponPolicy().getMinOrderPrice(),
			coupon.getCouponPolicy().getSalePrice(),
			coupon.getCouponPolicy().getSaleRate(),
			coupon.getCouponPolicy().getMaxSalePrice(),
			coupon.getCouponPolicy().getType()),
			coupon.getExpiredDate(),
			coupon.getIssueDate()));

		return couponResponseDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public CouponResponseDTO getCouponById(Long id) {
		Coupon coupon = couponRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + id));

		return new CouponResponseDTO(coupon.getId(),
			new CouponPolicyResponseDTO(
				coupon.getCouponPolicy().getId(),
				coupon.getCouponPolicy().getMinOrderPrice(),
				coupon.getCouponPolicy().getSalePrice(),
				coupon.getCouponPolicy().getSaleRate(),
				coupon.getCouponPolicy().getMaxSalePrice(),
				coupon.getCouponPolicy().getType()),
			coupon.getExpiredDate(),
			coupon.getIssueDate());
	}


}