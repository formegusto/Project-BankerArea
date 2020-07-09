package com.bankerarea.controller;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankerarea.common.JwtService;
import com.bankerarea.mapper.UserMapper;
import com.bankerarea.test.TestVO;
import com.bankerarea.vo.UserVO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserMapper userMapper;
	@Autowired
	JavaMailSender javaMailSender;
	@Autowired
	private JwtService jwtService;

	@PostMapping("/account/signin")
	public UserVO signinUser(@RequestBody UserVO vo, HttpServletRequest req) {
		System.out.println("/account/signin ==> " + vo.getId() + "로그인 처리");
		UserVO user = userMapper.signinUser(vo);
		
		String jwt = req.getHeader("Authorization");
		System.out.println(jwt);
		if(jwt == null) {
			System.out.println("토큰이 없어요");
		} else {
			try {
				if(jwtService.checkJwt(jwt)) {
					System.out.println("유효한 토큰 검사 성공");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("유효한 토큰이 아니에요");
		}
		
		return user;
	}
	
	@PostMapping("/account/token")
	public String createToken(@RequestBody UserVO vo) throws Exception{
		System.out.println("토큰 만들기 성공");
		return jwtService.makeJwt(vo);
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
