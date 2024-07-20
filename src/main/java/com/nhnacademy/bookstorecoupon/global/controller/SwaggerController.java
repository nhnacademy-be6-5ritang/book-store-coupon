package com.nhnacademy.bookstorecoupon.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/coupons/api")
@Slf4j
public class SwaggerController {

    @Autowired
    private OpenApiResource openApiResource;

    @GetMapping()
    public String getSwaggerJson(HttpServletRequest request, Model model) throws JsonProcessingException {
        String apiDocsUrl = "/coupon-docs/coupon-api"; // Swagger 문서 URL
        Locale locale = request.getLocale(); // 현재 요청의 Locale

        byte[] swaggerJsonBytes = openApiResource.openapiJson(request, apiDocsUrl, locale);
        if (swaggerJsonBytes == null) {
            model.addAttribute("swaggerJson", "{}");
        } else {
            String swaggerJson = new String(swaggerJsonBytes, StandardCharsets.UTF_8);
            log.info("Swagger JSON: " + swaggerJson); // 로그에 전체 JSON 출력
            model.addAttribute("swaggerJson", swaggerJson);
        }

        return "api/coupon-api";
    }
}