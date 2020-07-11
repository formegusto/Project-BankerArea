package com.bankerarea.common;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	private List<String> login_user_list = new ArrayList<String>();
	private Logger logger = LoggerFactory.getLogger(LoginManagementService.class);
	
	public LoginManagementService() {
		// TODO Auto-generated constructor stub
		System.out.println("[Login Management Service] 생성 완료");
	}
	
	public boolean isLogin(String id) {
		return login_user_list.contains(id);
	}
	
	public void addLoginUser(String id) {
		login_user_list.add(id);
	}
	
	public String makeToken(String id) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 1);
        // expireTime.setTime(expireTime.getTime() + 1000 * 60 * 60 * 5);
        	// Token Time 5 Hours
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("typ","JWT");
        headerMap.put("alg","HS256");
        
        Map<String, Object> map= new HashMap<String, Object>();
        map.put("id", id);
        
        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
	}
	
	public Object getIdByToken(String accessKey) throws Exception {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(accessKey).getBody(); // 정상 수행된다면 해당 토큰은 정상토큰
		logger.info("expireTime :" + claims.getExpiration());
            
        return claims.get("id");
    }
}
