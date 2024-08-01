package com.nhnacademy.bookstorecoupon.keymanager.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("oritang.mysql")
public class MysqlProperty {
	private String url;
	private String username;
	private String password;
}
