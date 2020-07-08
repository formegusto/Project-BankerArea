package com.bankerarea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankerarea.mapper.UserMapper;
import com.bankerarea.test.TestVO;
import com.bankerarea.vo.UserVO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserMapper userMapper;

	@PostMapping("/account/signin")
	public UserVO signinUser(@RequestBody UserVO vo) {
		System.out.println("/account/signin ==> " + vo.getId() + "로그인 처리");
		return userMapper.signinUser(vo);
	}
	
	@PostMapping("/account/signup")
	public UserVO signupUser(@RequestBody UserVO vo) {
		System.out.println("/account/signup ==> " + vo.getId() + "회원가입 처리");
		userMapper.signupUser(vo);
		
		return userMapper.signinUser(vo);
	}
	
	/* Test Mapping */
	@GetMapping("/test/returnUser")
	public UserVO testReturnUser(){
		UserVO uvo = new UserVO();
		
		uvo.setId("userVO");
		uvo.setPassword("12345678");
		uvo.setEmail("user@userVO.com");
		
		return uvo;
	}
	
	@GetMapping("/test/returnTest")
	public TestVO testReturnTest() {
		TestVO tvo = new TestVO();
		
		tvo.setNum(1);
		tvo.setStr("testVO");
		
		return tvo;
	}
	
}
