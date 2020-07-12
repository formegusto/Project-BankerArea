package com.bankerarea.controller;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankerarea.common.LoginManagementService;
import com.bankerarea.mapper.UserMapper;
import com.bankerarea.test.TestVO;
import com.bankerarea.vo.UserVO;

@CrossOrigin(origins = "", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserMapper userMapper;
	@Autowired
	JavaMailSender javaMailSender;
	@Autowired
	private LoginManagementService loginManagementService;

	@PostMapping("/signin")
	public UserVO signin(UserVO vo, HttpServletResponse res,
			HttpServletRequest req) throws Exception {
		System.out.println(vo);
		UserVO user = userMapper.signinUser(vo);
		String accessKey = loginManagementService.makeToken(vo.getId());
		
		System.out.println(accessKey);
		
		Cookie cookie = new Cookie("accessKey",accessKey);
		cookie.setPath("/");
		cookie.setMaxAge(60*30);
		
		res.addCookie(cookie);
		
		return user;
	}
	
	@PostMapping("/authApi")
	public ResponseEntity<Object> auth(@CookieValue(name="accessKey") String accessKey) throws Exception {
		System.out.println("[인증완료] 사용 가능한 API 입니다.");
		System.out.println("[accessKey] " + accessKey);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Object> signup() {
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	@PostMapping("/account/signin")
	public UserVO signinUser(@RequestBody UserVO vo, HttpServletResponse res) throws Exception {
		System.out.println("/account/signin ==> " + vo.getId() + "로그인 처리");
		UserVO user = userMapper.signinUser(vo);
		String accessKey = loginManagementService.makeToken(vo.getId());
		
		Cookie cookie = new Cookie("accessKey",accessKey);
		cookie.setPath("/");
		cookie.setMaxAge(60*30);
		
		res.addCookie(cookie);
		
		return user;
	}
	
	@PostMapping("/account/signup")
	public UserVO signupUser(@RequestBody UserVO vo) {
		System.out.println("/account/signup ==> " + vo.getId() + "회원가입 처리");
		userMapper.signupUser(vo);
		return userMapper.signinUser(vo);
	}
	
	@PostMapping("/account/signup/reqsecret")
	public void reqSecretCode(@RequestBody UserVO user) throws MessagingException {
		System.out.println(user.getEmail());
		MimeMessage message = javaMailSender.createMimeMessage();
		message.setSubject("Hello, We Are BankerArea Team!");
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
		message.setText("Ur Secret Code is 123456!! :)");
		message.setSentDate(new Date());
		javaMailSender.send(message);
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
