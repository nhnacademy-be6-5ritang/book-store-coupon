package com.nhnacademy.bookstorecoupon.categorycoupon.domain.entity;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "coupon_policy_id")
	private CouponPolicy couponPolicy;

	@NotNull
	@Column(name = "category_id")
	private Long categoryId;


	@NotNull
	@Column(name = "category_name", length = 20)
	private String categoryName;


	@Builder
	public CategoryCoupon(CouponPolicy couponPolicy, Long categoryId, String categoryName) {
		this.couponPolicy = couponPolicy;
		this.categoryId = categoryId;
		this.categoryName=categoryName;
	}

	// 헬퍼 클래스
	public static class CategoryInfo {
		public final Long categoryId;
		public final String categoryName;

		public CategoryInfo(Long categoryId, String categoryName) {
			this.categoryId = categoryId;
			this.categoryName = categoryName;
		}
	}
}
