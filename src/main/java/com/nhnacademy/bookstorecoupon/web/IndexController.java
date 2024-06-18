

package com.nhnacademy.bookstorecoupon.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("checkstyle:RegexpMultiline")
@RestController
@RequestMapping("/coupon")
public class IndexController {

	@GetMapping("/coupon/{id}")
	public ResponseEntity<String> getMember(@PathVariable("id") String id) {

		return ResponseEntity.ok(id);
	}

}
