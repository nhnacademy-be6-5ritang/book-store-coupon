package com.nhnacademy.bookstorecoupon.bookcoupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity.BookCoupon;

public interface BookCouponRepository extends JpaRepository<BookCoupon, Long>, CustomBookCouponRepository {

}


