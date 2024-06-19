package com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "categories_coupons")
public class CategoryCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_coupon_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "coupon_policy_id", nullable = false)
	private CouponPolicy couponPolicy;

	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@Builder
	public CategoryCoupon(CouponPolicy couponPolicy, Long categoryId) {
		this.couponPolicy = couponPolicy;
		this.categoryId = categoryId;
	}
}
