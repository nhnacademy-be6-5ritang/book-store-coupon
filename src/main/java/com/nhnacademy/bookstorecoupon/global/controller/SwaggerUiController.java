package com.nhnacademy.bookstorecoupon.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/coupons/api-spec")
@Controller
public class SwaggerUiController {

    /**
     * api html 반환
     *
     * @return the open api json
     *
     */
    @GetMapping
    public String getCouponApi()  {
        return "swagger-ui/coupon-api";
    }
}
