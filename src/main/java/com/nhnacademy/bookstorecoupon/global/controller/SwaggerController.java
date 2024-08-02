package com.nhnacademy.bookstorecoupon.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * @author 이기훈
 * Swagger 문서에 대한 API 를 제공하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons/api")
public class SwaggerController {
    private final OpenApiResource openApiResource;

    /**
     * Swagger API 문서를 JSON 형식으로 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return Swagger 문서의 JSON 표현을 포함한 {@link ResponseEntity}
     * @throws JsonProcessingException JSON 처리 중 오류가 발생한 경우
     */
    @GetMapping()
    public ResponseEntity<String> getSwaggerJson(HttpServletRequest request) throws JsonProcessingException {
        String apiDocsUrl = "/coupon-docs/coupon-api"; // Swagger 문서 URL
        Locale locale = request.getLocale(); // 현재 요청의 Locale

        byte[] swaggerJsonBytes = openApiResource.openapiJson(request, apiDocsUrl, locale);
        if (swaggerJsonBytes == null) {
            return ResponseEntity.ok("{}"); // 빈 JSON 반환
        }

        String swaggerJson = new String(swaggerJsonBytes, StandardCharsets.UTF_8);
        return ResponseEntity.ok(swaggerJson);
    }
}



