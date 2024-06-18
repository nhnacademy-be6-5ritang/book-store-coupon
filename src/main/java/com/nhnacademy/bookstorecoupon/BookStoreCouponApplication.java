package com.nhnacademy.bookstorecoupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BookStoreCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreCouponApplication.class, args);
    }

}
