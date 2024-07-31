package com.nhnacademy.bookstorecoupon.global.aspect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nhnacademy.bookstorecoupon.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstorecoupon.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstorecoupon.global.exception.InavailableAuthorizationException;

class RoleAuthorizationAspectTest {

	@Mock
	private JoinPoint joinPoint;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@Mock
	private CurrentUserDetails currentUserDetails;

	@InjectMocks
	private RoleAuthorizationAspect roleAuthorizationAspect;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		SecurityContextHolder.setContext(securityContext);
	}


	@Test
	void testCheckUserRole_MissingRole_ThrowsException() {
		// Arrange
		String[] roles = {"ROLE_ADMIN"};
		AuthorizeRole authorizeRole = mock(AuthorizeRole.class);
		when(authorizeRole.value()).thenReturn(roles);

		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		when(authentication.getPrincipal()).thenReturn(currentUserDetails);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		RequestContextHolder.setRequestAttributes(mock(ServletRequestAttributes.class));

		// Act & Assert
		InavailableAuthorizationException thrown = assertThrows(
			InavailableAuthorizationException.class,
			() -> roleAuthorizationAspect.checkUserRole(joinPoint, authorizeRole)
		);

		assertNotNull(thrown);
	}

	@Test
	void testCheckUserRole_NoAuthentication_ThrowsException() {
		// Arrange
		String[] roles = {"ROLE_USER"};
		AuthorizeRole authorizeRole = mock(AuthorizeRole.class);
		when(authorizeRole.value()).thenReturn(roles);

		when(securityContext.getAuthentication()).thenReturn(null);
		RequestContextHolder.setRequestAttributes(mock(ServletRequestAttributes.class));

		// Act & Assert
		InavailableAuthorizationException thrown = assertThrows(
			InavailableAuthorizationException.class,
			() -> roleAuthorizationAspect.checkUserRole(joinPoint, authorizeRole)
		);

		assertNotNull(thrown);
	}

	@Test
	void testCheckUserRole_NoCurrentUserDetails_ThrowsException() {
		// Arrange
		String[] roles = {"ROLE_USER"};
		AuthorizeRole authorizeRole = mock(AuthorizeRole.class);
		when(authorizeRole.value()).thenReturn(roles);

		when(authentication.getPrincipal()).thenReturn(null);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		RequestContextHolder.setRequestAttributes(mock(ServletRequestAttributes.class));

		// Act & Assert
		InavailableAuthorizationException thrown = assertThrows(
			InavailableAuthorizationException.class,
			() -> roleAuthorizationAspect.checkUserRole(joinPoint, authorizeRole)
		);

		assertNotNull(thrown);
	}
}
