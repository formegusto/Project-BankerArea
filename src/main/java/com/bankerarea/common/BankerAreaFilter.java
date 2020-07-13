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
					"/users/account/logout" , "/idea/list"
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
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
		res.setHeader("Access-Control-Allow-Max-Age","3600");
		res.setHeader("Access-Control-Allow-Headers","X-Requested-With, Content-Type, "
				+ "Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Requests-Headers, Set-Cookie");
		

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
					System.out.println("[BankerArea Service Filter] 비로그인 사용자에게도 허용된 API 입니다.");
					chain.doFilter(request, response);
				}
			} 
			else {
				System.out.println("[BankerArea Service Filter] 쿠키 인식 검증 체크 들어갑니다.");
				String accessKey = null;
				Cookie accessCookie = null;
				
				for(Cookie cookie : cookies) {
					if(cookie.getName().equals("accessKey")) {
						accessKey = cookie.getValue();
						accessCookie = cookie;
						break;
					}
				}
				
				try {
					// 정상토큰인 사용자
					Object auth = loginManagementService.isExpire(accessKey);
					String authId = null;
					System.out.println("만료정보 (true : 정상 / false : 만료 / String : refresh ) " + auth);
					
					if(auth instanceof Boolean) {
						// 정상 true : 만료 false
						if(((Boolean) auth)) {
							authId = loginManagementService.getIdByToken(accessKey);
							System.out.println("[BankerArea Service Filter] (사용자)" + authId + "검증완료");
							chain.doFilter(request, response);
						}
						else {
							accessCookie.setMaxAge(-1);
							Cookie newAccessCookie = new Cookie("accessKey", "");
							newAccessCookie.setMaxAge(0);
							newAccessCookie.setPath("/");
							newAccessCookie.setHttpOnly(true);
							res.addCookie(newAccessCookie);
							
							if(!unauth_allow_api.contains(URI)) {
								System.out.println("[BankerArea Service Filter] 비로그인 사용자에게 허용이 안된 API 입니다.");
								res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							}
							else {
								System.out.println("[BankerArea Service Filter] 비로그인 사용자에게도 허용된 API 입니다.");
								chain.doFilter(request, response);
							}
						}
							
					} else {
						System.out.println("[BankerArea Service Filter] 필터 입니다. 새 쿠키로 교체 하겠습니다.");
						
						// refresh
						accessCookie.setMaxAge(-1);
						accessCookie.setValue((String)auth);
						
						Cookie newAccessCookie = new Cookie("accessKey", (String)auth);
						newAccessCookie.setMaxAge(60*30);
						newAccessCookie.setPath("/");
						newAccessCookie.setHttpOnly(true);
						res.addCookie(newAccessCookie);
						
						System.out.println("[BankerArea Service Filter] 쿠키 & JWT 리프레쉬 완료");
						authId = loginManagementService.getIdByToken((String) auth);
						
						System.out.println("[BankerArea Service Filter] (사용자)" + authId + "검증완료");
						chain.doFilter(request, response);
					}
				} catch (ExpiredJwtException e) {
					// 유예 기간 ( JWT 기본값 ExpireTime 을 넘어간 사용자 == 비로그인 사용자로 인식)
					accessCookie.setMaxAge(0);
					if(!unauth_allow_api.contains(URI)) {
						System.out.println("[BankerArea Service Filter] 비로그인 사용자에게 허용이 안된 API 입니다.");
						res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					}
					else {
						System.out.println("[BankerArea Service Filter] 비로그인 사용자에게도 허용된 API 입니다.");
						chain.doFilter(request, response);
					}
				} catch (JwtException e) {
					// 토큰 변조
				} catch (Exception e) {
					
				}
			}
		}
	}

}
