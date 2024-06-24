package com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons_policies")
public class CouponPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_policy_id")
	private Long id;

	@NotNull
	@Column(name = "coupon_policy_min_order_price")
	private BigDecimal minOrderPrice;

	@Column(name = "coupon_policy_sale_price")
	private BigDecimal salePrice;

	@Column(name = "coupon_policy_sale_rate")
	private BigDecimal saleRate;

	@Column(name = "coupon_policy_max_sale_price")
	private BigDecimal maxSalePrice;

	@NotNull
	@Column(name = "coupon_policy_type", length = 10)
	private String type;

	@Builder
	public CouponPolicy(BigDecimal minOrderPrice, BigDecimal salePrice, BigDecimal saleRate, BigDecimal maxSalePrice,
		String type) {
		this.minOrderPrice = minOrderPrice;
		this.salePrice = salePrice;
		this.saleRate = saleRate;
		this.maxSalePrice = maxSalePrice;
		this.type = type;
	}

	public void update(BigDecimal minOrderPrice, BigDecimal salePrice, BigDecimal saleRate, BigDecimal maxSalePrice)
		 {
		this.minOrderPrice = minOrderPrice;
		this.salePrice = salePrice;
		this.saleRate = saleRate;
		this.maxSalePrice = maxSalePrice;
	}

}
