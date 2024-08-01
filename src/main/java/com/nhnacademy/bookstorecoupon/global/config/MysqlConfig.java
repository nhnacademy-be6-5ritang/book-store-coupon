package com.nhnacademy.bookstorecoupon.global.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.bookstorecoupon.keymanager.property.MysqlProperty;
import com.nhnacademy.bookstorecoupon.keymanager.service.KeyManagerService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MysqlConfig {
	private final MysqlProperty mysqlProperty;
	private final KeyManagerService keyManagerService;

	@Bean
	public DataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();

		basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

		basicDataSource.setUrl(keyManagerService.getSecret(mysqlProperty.getUrl()));
		basicDataSource.setUsername(keyManagerService.getSecret(mysqlProperty.getUsername()));
		basicDataSource.setPassword(keyManagerService.getSecret(mysqlProperty.getPassword()));

		basicDataSource.setInitialSize(50);    // 초기로 생성할 연결의 수를 설정합니다.
		basicDataSource.setMaxTotal(50);        // 풀에서 유지할 수 있는 최대 연결 수를 설정합니다.
		basicDataSource.setMaxIdle(50);        // 유휴 상태로 유지할 최대 연결 수를 설정합니다.
		basicDataSource.setMinIdle(50);        // 유휴 상태로 유지할 최소 연결 수를 설정합니다.

		basicDataSource.setTestOnBorrow(true);                // 풀에서 연결을 빌릴 때마다 해당 연결이 유효한지 검증할지 여부를 설정합니다.
		basicDataSource.setValidationQuery("SELECT 1");        // 연결 유효성을 검사하기 위해 실행할 SQL 쿼리를 설정합니다.
		return basicDataSource;
	}
}
