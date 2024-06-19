package com.nhnacademy.bookstorecoupon.coupon.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity.UserAndCoupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private Long id;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "coupon_policy_id", nullable = false)
	private CouponPolicy couponPolicy;

	@NotNull
	@Column(name = "coupon_expired_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private LocalDateTime expiredDate;

	@NotNull
	@Column(name = "coupon_issue_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private LocalDateTime issueDate;

	@OneToMany(mappedBy = "coupon")
	private List<UserAndCoupon> userAndCoupons;

	@Builder
	public Coupon(CouponPolicy couponPolicy, LocalDateTime expiredDate, LocalDateTime issueDate) {
		this.couponPolicy = couponPolicy;
		this.expiredDate = expiredDate;
		this.issueDate = issueDate;
	}
}
