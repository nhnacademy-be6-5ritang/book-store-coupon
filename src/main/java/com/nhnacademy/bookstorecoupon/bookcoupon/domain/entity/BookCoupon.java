package com.nhnacademy.bookstorecoupon.bookcoupon.domain.entity;


import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "coupon_policy_id")
	private CouponPolicy couponPolicy;

	@NotNull
	@Column(name = "book_id")
	private Long bookId;

	@Builder
	public BookCoupon(CouponPolicy couponPolicy, Long bookId) {
		this.couponPolicy = couponPolicy;
		this.bookId = bookId;
	}
}
