package com.nhnacademy.bookstorecoupon.coupontemplate.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.request.CouponTemplateRequestDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.dto.response.CouponTemplateResponseDTO;
import com.nhnacademy.bookstorecoupon.coupontemplate.domain.entity.CouponTemplate;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponAddErrorException;
import com.nhnacademy.bookstorecoupon.coupontemplate.exception.CouponNotFoundException;
import com.nhnacademy.bookstorecoupon.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookstorecoupon.coupontemplate.service.CouponTemplateService;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.dto.response.CouponPolicyResponseDTO;
import com.nhnacademy.bookstorecoupon.couponpolicy.domain.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookstorecoupon.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.global.exception.payload.ErrorStatus;

@Service
@Transactional
public class CouponTemplateServiceImpl implements CouponTemplateService {

	private final CouponTemplateRepository couponTemplateRepository;
	private final CouponPolicyRepository couponPolicyRepository;

	public CouponTemplateServiceImpl(CouponTemplateRepository couponTemplateRepository, CouponPolicyRepository couponPolicyRepository) {
		this.couponTemplateRepository = couponTemplateRepository;
		this.couponPolicyRepository = couponPolicyRepository;
	}

	@Override
	public CouponTemplateResponseDTO createCouponTemplate(CouponTemplateRequestDTO requestDTO) {
		String errorMessage = String.format("해당 쿠폰정책번호 '%d'는 존재하지 않습니다.", requestDTO.couponPolicyId());
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());

		CouponPolicy couponPolicy = couponPolicyRepository.findById(requestDTO.couponPolicyId())
			.orElseThrow(
				() ->  new CouponPolicyNotFoundException(errorStatus));

		if(couponPolicy.getType().equals("birthday") || couponPolicy.getType().equals("welcome")){
			String errorMessage2 = String.format("해당 쿠폰타입 '%s'은 템플릿 발급을 할 수 없습니다.", couponPolicy.getType());
			ErrorStatus errorStatus2 = ErrorStatus.from(errorMessage2, HttpStatus.FORBIDDEN, LocalDateTime.now());
			throw new CouponAddErrorException(errorStatus2);
		}

		CouponTemplate couponTemplate = CouponTemplate.builder()
			.couponPolicy(couponPolicy)
			.expiredDate(requestDTO.expiredDate())
			.issueDate(requestDTO.issueDate())
			.build();

		couponTemplateRepository.save(couponTemplate);


		CouponTemplate savedCouponTemplate = couponTemplateRepository.save(couponTemplate);

		return new CouponTemplateResponseDTO(
			savedCouponTemplate.getId(),
			new CouponPolicyResponseDTO(
				savedCouponTemplate.getCouponPolicy().getId(),
				savedCouponTemplate.getCouponPolicy().getMinOrderPrice(),
				savedCouponTemplate.getCouponPolicy().getSalePrice(),
				savedCouponTemplate.getCouponPolicy().getSaleRate(),
				savedCouponTemplate.getCouponPolicy().getMaxSalePrice(),
				savedCouponTemplate.getCouponPolicy().getType()
			),
			savedCouponTemplate.getExpiredDate(),
			savedCouponTemplate.getIssueDate()
		);
	}



	@Override
	@Transactional(readOnly = true)
	public List<CouponTemplateResponseDTO> getAllCouponTemplates() {
		List<CouponTemplate> couponTemplates = couponTemplateRepository.findAll();
		return couponTemplates.stream()
			.map(couponTemplate -> new CouponTemplateResponseDTO(couponTemplate.getId(),
				new CouponPolicyResponseDTO(
					couponTemplate.getCouponPolicy().getId(),
					couponTemplate.getCouponPolicy().getMinOrderPrice(),
					couponTemplate.getCouponPolicy().getSalePrice(),
					couponTemplate.getCouponPolicy().getSaleRate(),
					couponTemplate.getCouponPolicy().getMaxSalePrice(),
					couponTemplate.getCouponPolicy().getType()),
				couponTemplate.getExpiredDate(),
				couponTemplate.getIssueDate()))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CouponTemplateResponseDTO> getAllCouponTemplatePaging(Pageable pageable) {
		int page=pageable.getPageNumber()-1;
		int pageSize=4;


		List<String> types = List.of("sale", "book", "category");
		Page<CouponTemplate> coupons = couponTemplateRepository.findByCouponPolicyTypes(types, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
		Page<CouponTemplateResponseDTO> couponResponseDTOs = coupons.map(couponTemplate -> new CouponTemplateResponseDTO(
			couponTemplate.getId(), new CouponPolicyResponseDTO(couponTemplate.getCouponPolicy().getId(),
			couponTemplate.getCouponPolicy().getMinOrderPrice(),
			couponTemplate.getCouponPolicy().getSalePrice(),
			couponTemplate.getCouponPolicy().getSaleRate(),
			couponTemplate.getCouponPolicy().getMaxSalePrice(),
			couponTemplate.getCouponPolicy().getType()),
			couponTemplate.getExpiredDate(),
			couponTemplate.getIssueDate()));

		return couponResponseDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public CouponTemplateResponseDTO getCouponTemplateById(Long id) {
		String errorMessage = String.format("해당 쿠폰은 '%d'는 존재하지 않습니다.", id);
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());


		CouponTemplate couponTemplate = couponTemplateRepository.findById(id)
			.orElseThrow(() -> new CouponNotFoundException(errorStatus));

		return new CouponTemplateResponseDTO(couponTemplate.getId(),
			new CouponPolicyResponseDTO(
				couponTemplate.getCouponPolicy().getId(),
				couponTemplate.getCouponPolicy().getMinOrderPrice(),
				couponTemplate.getCouponPolicy().getSalePrice(),
				couponTemplate.getCouponPolicy().getSaleRate(),
				couponTemplate.getCouponPolicy().getMaxSalePrice(),
				couponTemplate.getCouponPolicy().getType()),
			couponTemplate.getExpiredDate(),
			couponTemplate.getIssueDate());
	}


}