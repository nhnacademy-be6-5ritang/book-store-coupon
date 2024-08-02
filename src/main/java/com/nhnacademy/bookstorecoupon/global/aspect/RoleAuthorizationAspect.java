package com.nhnacademy.bookstorecoupon.global.aspect;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nhnacademy.bookstorecoupon.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstorecoupon.global.exception.InavailableAuthorizationException;

/**
 * @author 김태환
 * 역할 기반 권한을 검사하는 AOP(Aspect-Oriented Programming) 어스펙트입니다.
 */
@Aspect
@Component
public class RoleAuthorizationAspect {

	/**
	 * {@link AuthorizeRole} 애노테이션이 붙은 메소드 호출 전에 현재 사용자의 역할을 검사합니다.
	 *
	 * @param joinPoint 메소드 호출 시점을 나타내는 {@link JoinPoint} 객체.
	 * @param authorizeRole 메소드에 붙은 {@link AuthorizeRole} 애노테이션.
	 * @throws Throwable 권한 검사 중 발생할 수 있는 모든 예외.
	 */
	@Before("@annotation(authorizeRole)")
	public void checkUserRole(JoinPoint joinPoint, AuthorizeRole authorizeRole) throws Throwable {
		CurrentUserDetails currentUserDetails = getCurrentUserDetails();

		if (currentUserDetails == null) {
			throw new InavailableAuthorizationException();
		}

		List<String> roles = Arrays.asList(authorizeRole.value());
		boolean hasRole = currentUserDetails.getAuthorities().stream()
			.anyMatch(grantedAuthority -> roles.contains(grantedAuthority.getAuthority()));

		if (!hasRole) {
			throw new InavailableAuthorizationException();
		}
	}

	/**
	 * 현재 사용자의 {@link CurrentUserDetails} 객체를 반환합니다.
	 *
	 * @return 현재 사용자의 {@link CurrentUserDetails} 객체, 또는 인증되지 않았거나 해당 타입이 아닌 경우 {@code null}.
	 */
	private CurrentUserDetails getCurrentUserDetails() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.getPrincipal() instanceof CurrentUserDetails) {
				return (CurrentUserDetails)authentication.getPrincipal();
			}
		}
		return null;
	}
}
