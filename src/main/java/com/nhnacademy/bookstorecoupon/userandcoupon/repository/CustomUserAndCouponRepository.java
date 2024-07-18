package com.nhnacademy.bookstorecoupon.userandcoupon.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;
import com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity.CategoryCoupon;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.dto.response.UserAndCouponResponseDTO;

public interface CustomUserAndCouponRepository {
	Page<UserAndCouponResponseDTO> findAllByUserPaging(Pageable pageable, Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);

	Page<UserAndCouponResponseDTO> findAllByManagerPaging(Pageable pageable, String type, Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap, Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap);

	List<UserAndCouponResponseDTO> findCouponByOrder(Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap,
		List<Long> bookIds, List<Long> categoryIds,
		BigDecimal bookPrice);

	List<UserAndCouponResponseDTO> findCouponByCartOrder(
		Long userId,
		Map<Long, BookCoupon.BookInfo> bookIdMap,
		Map<Long, CategoryCoupon.CategoryInfo> categoryIdMap,
		List<GetBookByOrderCouponResponse> bookDetails
	);
}