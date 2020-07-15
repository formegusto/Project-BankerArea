package com.bankerarea.controller;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@PostMapping("/account/signin")
	public String signinUser(@RequestBody UserVO vo, HttpServletResponse res,
			@CookieValue(name = "accessKey", defaultValue = "unAuth") String key) throws Exception {
		System.out.println("/account/signin ==> " + vo.getId() + "로그인 처리");
		System.out.println(key);
		UserVO user = userMapper.signinUser(vo);
		String accessKey = loginManagementService.makeToken(vo.getId());
		
		if(user == null) {
			res.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
			return null;
		} else {
			Cookie cookie = new Cookie("accessKey",accessKey);
			cookie.setPath("/");
			cookie.setMaxAge(60*30);
			cookie.setHttpOnly(true);
			
			res.addCookie(cookie);
			return user.getId();
		}
	}
	
	@PostMapping("/account/signup")
	public UserVO signupUser(@Valid @RequestBody UserVO vo) {
		System.out.println("/account/signup ==> " + vo.getId() + "회원가입 처리");
		userMapper.signupUser(vo);
		return userMapper.signinUser(vo);
	}
	
	@PostMapping("/account/signup/reqsecret")
	public String reqSecretCode(@Valid @RequestBody UserVO user) throws MessagingException {
		System.out.println(user.getEmail());
		String secretCode = loginManagementService.numberGen(6, 2);
		MimeMessage msg = javaMailSender.createMimeMessage();
		String message = "Hello! We Are BankerArea Team<br/>" +
				"Ur Secret Code is " + secretCode + " :)<br/>" +
				"Have A Nice Day! THX! :)";
		msg.setContent(message, "text/html");
		msg.setSubject("Hello, We Are BankerArea Team!");
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
		
		javaMailSender.send(msg);
		
		return secretCode;
	}
	
	@GetMapping("/account/logout")
	public void logoutUser(HttpServletRequest req, HttpServletResponse res) {
		Cookie[] cookies = req.getCookies();
		Cookie accessCookie = null;
		
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("accessKey")) {
				accessCookie = cookie;
				break;
			}
		}
		
		accessCookie.setMaxAge(-1);
		Cookie newAccessCookie = new Cookie("accessKey", "");
		newAccessCookie.setMaxAge(0);
		newAccessCookie.setPath("/");
		newAccessCookie.setHttpOnly(true);
		res.addCookie(newAccessCookie);
	}
}
