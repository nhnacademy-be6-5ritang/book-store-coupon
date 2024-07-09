package com.nhnacademy.bookstorecoupon.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nhnacademy.bookstoreback.auth.resolver.CurrentUserArgumentResolver;
import com.nhnacademy.bookstoreback.global.handler.CustomPageableHandlerMethodArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {


	@Bean
	public CurrentUserArgumentResolver currentUserArgumentResolver() {
		return new CurrentUserArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentUserArgumentResolver());
	}


}
