package com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity;


import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "books_coupons")
public class BookCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_coupon_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "coupon_policy_id", nullable = false)
	private CouponPolicy couponPolicy;

	@Column(name = "book_id", nullable = false)
	private Long bookId;

	@Builder
	public BookCoupon(CouponPolicy couponPolicy, Long bookId) {
		this.couponPolicy = couponPolicy;
		this.bookId = bookId;
	}
}
