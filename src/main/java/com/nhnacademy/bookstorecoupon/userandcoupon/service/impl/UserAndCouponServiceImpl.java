package com.nhnacademy.bookstorecoupon.userandcoupon.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.coupon.domain.dto.response.CouponResponseDTO;
import com.nhnacademy.bookstorecoupon.coupon.domain.entity.Coupon;
import com.nhnacademy.bookstorecoupon.coupon.repository.CouponRepository;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.request.UserAndCouponRequestDTO;
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

    public UserAndCouponServiceImpl(UserAndCouponRepository userAndCouponRepository, CouponRepository couponRepository) {
        this.userAndCouponRepository = userAndCouponRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public UserAndCouponResponseDTO createUserAndCoupon(UserAndCouponRequestDTO requestDTO) {
        Coupon coupon = couponRepository.findById(requestDTO.couponId())
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + requestDTO.couponId()));

        UserAndCoupon userAndCoupon = UserAndCoupon.builder()
                .coupon(coupon)
                .userId(requestDTO.userId())
                .usedDate(requestDTO.usedDate())
                .isUsed(requestDTO.isUsed())
                .build();


        UserAndCoupon savedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

        return new UserAndCouponResponseDTO(
            savedUserAndCoupon.getId(),
            new CouponResponseDTO(
                savedUserAndCoupon.getCoupon().getId(),
                new CouponPolicyResponseDTO(
                    savedUserAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
                    savedUserAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
                    savedUserAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
                    savedUserAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
                    savedUserAndCoupon.getCoupon().getCouponPolicy().getType()
                ),
                savedUserAndCoupon.getCoupon().getExpiredDate(),
                savedUserAndCoupon.getCoupon().getIssueDate()
            ),
            savedUserAndCoupon.getUserId(),
            savedUserAndCoupon.getUsedDate(),
            savedUserAndCoupon.getIsUsed()
        );


    }

    @Override
    public UserAndCouponResponseDTO updateUserAndCoupon(Long id, UserAndCouponRequestDTO requestDTO) {
        UserAndCoupon userAndCoupon = userAndCouponRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserAndCoupon not found with ID: " + id));

        userAndCoupon.update(requestDTO.usedDate(), requestDTO.isUsed());

        UserAndCoupon updatedUserAndCoupon = userAndCouponRepository.save(userAndCoupon);

        return new UserAndCouponResponseDTO(
            updatedUserAndCoupon.getId(),
            new CouponResponseDTO(
                updatedUserAndCoupon.getCoupon().getId(),
                new CouponPolicyResponseDTO(
                    updatedUserAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
                    updatedUserAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
                    updatedUserAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
                    updatedUserAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
                    updatedUserAndCoupon.getCoupon().getCouponPolicy().getType()
                ),
                updatedUserAndCoupon.getCoupon().getExpiredDate(),
                updatedUserAndCoupon.getCoupon().getIssueDate()
            ),
            updatedUserAndCoupon.getUserId(),
            updatedUserAndCoupon.getUsedDate(),
            updatedUserAndCoupon.getIsUsed()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAndCouponResponseDTO> getAllUserAndCoupons() {
        List<UserAndCoupon> userAndCoupons = userAndCouponRepository.findAll();
        return userAndCoupons.stream()
                .map(userAndCoupon -> new UserAndCouponResponseDTO(userAndCoupon.getId(),
                        new CouponResponseDTO(userAndCoupon.getCoupon().getId(),
                                new CouponPolicyResponseDTO(userAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
                                        userAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
                                        userAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
                                        userAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
                                        userAndCoupon.getCoupon().getCouponPolicy().getType()),
                                userAndCoupon.getCoupon().getExpiredDate(),
                                userAndCoupon.getCoupon().getIssueDate()),
                        userAndCoupon.getUserId(),
                        userAndCoupon.getUsedDate(),
                        userAndCoupon.getIsUsed()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserAndCouponResponseDTO getUserAndCouponById(Long id) {
        UserAndCoupon userAndCoupon = userAndCouponRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserAndCoupon not found with ID: " + id));

        return new UserAndCouponResponseDTO(userAndCoupon.getId(),
                new CouponResponseDTO(userAndCoupon.getCoupon().getId(),
                        new CouponPolicyResponseDTO(userAndCoupon.getCoupon().getCouponPolicy().getMinOrderPrice(),
                                userAndCoupon.getCoupon().getCouponPolicy().getSalePrice(),
                                userAndCoupon.getCoupon().getCouponPolicy().getSaleRate(),
                                userAndCoupon.getCoupon().getCouponPolicy().getMaxSalePrice(),
                                userAndCoupon.getCoupon().getCouponPolicy().getType()),
                        userAndCoupon.getCoupon().getExpiredDate(),
                        userAndCoupon.getCoupon().getIssueDate()),
                userAndCoupon.getUserId(),
                userAndCoupon.getUsedDate(),
                userAndCoupon.getIsUsed());
    }

}