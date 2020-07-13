package com.bankerarea.common;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class LoginManagementService {
	private String secretKey = "ThisisBankerSecretKeyByMKYHTH";
	private Logger logger = LoggerFactory.getLogger(LoginManagementService.class);
	
	public LoginManagementService() {
		// TODO Auto-generated constructor stub
		System.out.println("[Login Management Service] 생성 완료");
	}
	
	public String makeToken(String id) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("typ","JWT");
        headerMap.put("alg","HS256");
        
        Map<String, Object> map= new HashMap<String, Object>();
        
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + (1000 * 60 * 2));
        
        Date refreshTime = new Date();
        refreshTime.setTime(expireTime.getTime() - (1000 * 60 * 1));
        map.put("id", id);
        map.put("exp", expireTime);
        map.put("refresh", refreshTime);
        
        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .signWith(signatureAlgorithm, signingKey);

        System.out.println("[LoginManagementService] 새 토큰 발급 완료");
        return builder.compact();
	}
	
	public String getIdByToken(String accessKey) throws Exception {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(accessKey).getBody(); // 정상 수행된다면 해당 토큰은 정상토큰
		
		logger.info("expireTime :" + claims.getExpiration());
		System.out.println("[LoginManagementService] 아이디 디코딩 완료");
		
        return claims.get("id", String.class);
    }
	
	public Object isExpire(String accessKey) throws Exception {
		Object result = true;
		
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(accessKey).getBody();
		
		Date expireTime = claims.get("exp",Date.class);
		expireTime.setTime(expireTime.getTime() / 1000);
		
		Date refresh = claims.get("refresh",Date.class);
		
		Date now = new Date();
		
		System.out.println("now ===> " + now.getTime());
		System.out.println("Expiration Time ===> " + expireTime.getTime());
		System.out.println("Refresh Time ===> " + refresh.getTime());
		
		// 기간 만료
		if(expireTime.getTime() < now.getTime()) {
			System.out.println("[LoginManagementService] 만료된 토큰 입니다. 다시 로그인을 시도해주세요!");
			result = false;
		}
		// refresh Time
		else if(refresh.getTime() < now.getTime()) {
			System.out.println("[LoginManagementService] 기간이 얼마남지 않은 토큰 입니다. 필터에서 리프레쉬를 해주세요.");
			String id = getIdByToken(accessKey);
			result = makeToken(id);
		}
		
		return result;
	}
	
}
