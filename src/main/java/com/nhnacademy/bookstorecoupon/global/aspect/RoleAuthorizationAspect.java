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

@Aspect
@Component
public class RoleAuthorizationAspect {

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
