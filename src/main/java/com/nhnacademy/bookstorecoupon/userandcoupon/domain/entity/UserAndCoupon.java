package com.nhnacademy.bookstorecoupon.userandcoupon.domain.entity;

import java.time.LocalDateTime;

import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "users_and_coupons")
public class UserAndCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_and_coupon_id")
	private Long id;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "coupon_policy_id")
	private CouponPolicy couponPolicyId;

	@NotNull
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "user_and_coupon_used_date")
	private LocalDateTime usedDate;

	@NotNull
	@Column(name = "user_and_coupon_is_used")
	private Boolean isUsed = false;

	@NotNull
	@Column(name = "user_and_coupon_expired_date")
	private LocalDateTime expiredDate;

	@NotNull
	@Column(name = "user_and_coupon_issue_date")
	private LocalDateTime issueDate;



	@Builder
	public UserAndCoupon(CouponPolicy couponPolicyId, Long userId, LocalDateTime usedDate, Boolean isUsed, LocalDateTime expiredDate, LocalDateTime issueDate) {
		this.couponPolicyId = couponPolicyId;
		this.userId = userId;
		this.usedDate = usedDate;
		this.isUsed = isUsed;
		this.expiredDate=expiredDate;
		this.issueDate=issueDate;

	}

	public void update(LocalDateTime usedDate, Boolean isUsed) {
		this.usedDate = usedDate;
		this.isUsed = isUsed;
	}
}
