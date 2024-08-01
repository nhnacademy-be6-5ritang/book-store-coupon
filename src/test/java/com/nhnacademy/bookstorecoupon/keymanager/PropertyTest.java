package com.nhnacademy.bookstorecoupon.keymanager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstorecoupon.keymanager.property.MysqlProperty;

class PropertyTest {


	private MysqlProperty mysqlProperty;


	@BeforeEach
	void setUp() {
		mysqlProperty = new MysqlProperty();
		mysqlProperty.setUrl("jdbc:mysql://localhost:3306/testdb");
		mysqlProperty.setUsername("testUser");
		mysqlProperty.setPassword("testPassword");
	}

	@Test
	void testMysqlProperty() {
		assertEquals("jdbc:mysql://localhost:3306/testdb", mysqlProperty.getUrl());
		assertEquals("testUser", mysqlProperty.getUsername());
		assertEquals("testPassword", mysqlProperty.getPassword());
	}
}