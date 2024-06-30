package com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity;

import java.time.LocalDateTime;

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
@Table(name = "coupons_templates")
public class CouponTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_template_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "coupon_policy_id", nullable = false)
	private CouponPolicy couponPolicy;

	@NotNull
	@Column(name = "coupon_template_expired_date", nullable = false)
	private LocalDateTime expiredDate;

	@NotNull
	@Column(name = "coupon_template_issue_date", nullable = false)
	private LocalDateTime issueDate;

	@Builder
	public CouponTemplate(CouponPolicy couponPolicy, LocalDateTime expiredDate, LocalDateTime issueDate) {
		this.couponPolicy = couponPolicy;
		this.expiredDate = expiredDate;
		this.issueDate = issueDate;
	}

	public void update(CouponPolicy couponPolicy, LocalDateTime expiredDate, LocalDateTime issueDate) {
		this.couponPolicy = couponPolicy;
		this.expiredDate = expiredDate;
		this.issueDate = issueDate;
	}
}
