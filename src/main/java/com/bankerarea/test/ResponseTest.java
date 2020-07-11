package com.bankerarea.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankerarea.vo.UserVO;

@CrossOrigin(origins = "", maxAge = 3600, allowCredentials="true")
@RestController
public class ResponseTest {
	@GetMapping("/resTest")
	public ResponseEntity test(UserVO vo, HttpServletResponse res,
			HttpServletRequest req) throws Exception {
		System.out.println("도착");
		
		return new ResponseEntity(HttpStatus.OK);
	}
}
