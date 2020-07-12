package com.bankerarea.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Configuration
public class BankerAreaFilter implements Filter {
	private List<String> unauth_allow_api = new ArrayList<String>(
			Arrays.asList( 
					"/users/account/signin" , "/users/account/signup" ,
					"/idea/list" , "/idea/detail" , "/users/signin" , "/users/signup"
				));
	private List<String> auth_notallow_api = new ArrayList<String>(
			Arrays.asList( 
					"/users/signin" , "/users/signup"
				));
	@Autowired
	private LoginManagementService loginManagementService;
	
	public BankerAreaFilter() {
		// TODO Auto-generated constructor stub
		System.out.println("[BankerArea Service Filter] 생성 완료");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req= (HttpServletRequest) request;
		String URI = req.getRequestURI();
		String method = req.getMethod();
		System.out.println(req.getMethod());
		
		res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		res.setHeader("Access-Control-Allow-Max-Age","3600");
		res.setHeader("Access-Control-Allow-Headers","X-Requested-With, Content-Type, "
				+ "Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Requests-Headers");
		
		Cookie[] cookies = req.getCookies();
		
		if(!method.equals("OPTIONS")) {
			if(cookies == null) {
				// Code 205 . NoContent 상태라서 요청자의 document view의 reset 요청
				// 쿠키가 없는 and 로그인 기록이 있는 사용자 ( 쿠키 재 생성 및 새 jwt 토큰 발급 글 쓰다가 쿠키가 만료된 경우를 생각 )
				// 쿠키가 없는 and 로그인 기록 없는 사용자
				if(!unauth_allow_api.contains(URI)) {
					System.out.println("[BankerArea Service Filter] 비로그인 사용자에게 허용이 안된 API 입니다.");
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
				else {
					System.out.println("아뇨 뚱인데요");
					chain.doFilter(request, response);
				}
			} 
			else {
				System.out.println("쿠키 있음");
				String accessKey = null;
				
				for(Cookie cookie : cookies) {
					if(cookie.getName().equals("accessKey")) {
						accessKey = cookie.getValue();
						break;
					}
				}
				
				try {
					// 정상토큰인 사용자
					String authId = (String)loginManagementService.getIdByToken(accessKey);
					System.out.println("[BankerArea Service Filter] (사용자)" + authId + "검증완료");
					if(auth_notallow_api.contains(URI)) {
						res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					} else {
						chain.doFilter(request, response);
					}
				} catch (ExpiredJwtException e) {
					// 쿠키가 있는 and 토큰이 만료된 사용자 ( 쿠키 및 재설정 토큰 재설정 )
					
				} catch (JwtException e) {
					
				} catch (Exception e) {
					
				}
			}
		}
	}

}
