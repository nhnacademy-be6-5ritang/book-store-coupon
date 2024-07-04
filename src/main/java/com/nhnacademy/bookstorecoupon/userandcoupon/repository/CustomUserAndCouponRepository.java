package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface CustomUserAndCouponRepository {
	Page<UserAndCouponResponseDTO> findAllByUserPaging(Pageable pageable, Long userId,Map<Long, Long> bookIdMap, Map<Long, Long> categoryIdMap);
	Page<UserAndCouponResponseDTO> findAllByManagerPaging(Pageable pageable, String type, Long userId, Map<Long, Long> bookIdMap, Map<Long, Long> categoryIdMap);
}