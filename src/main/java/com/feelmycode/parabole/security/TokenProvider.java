package com.feelmycode.parabole.security;

import com.feelmycode.parabole.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {

//  HS512 사용시 Warning: io.jsonwebtoken.security.Keys class's 'secretKeyFor(SignatureAlgorithm.HS512)
    private static final String SECRET_KEY = "NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5";

    public String create(User User) {
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
            Instant.now()
                .plus(1, ChronoUnit.DAYS));

		/*
		{ // header
		  "alg":"HS512"
		}.
		{ // payload
		  "sub":"40288093784915d201784916a40c0001",
		  "iss": "demo app",
		  "iat":1595733657,
		  "exp":1596597657
		}.
		// SECRET_KEY를 이용해 서명한 부분
		Nn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNrXdJJNWkRv50_l7bPLQPwhMobT4vBOG6Q3JYjhDrKFlBSaUxZOg
		 */

        // JWT Token 생성
        return Jwts.builder()
            // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            // payload에 들어갈 내용
            .setSubject(User.getId().toString()) // sub
            .setIssuer("theparabole app") // iss
            .setIssuedAt(new Date()) // iat
            .setExpiration(expiryDate) // exp
            .compact();
    }

    public String validateAndGetUserId(String token) {
        // parseClaimsJws메서드가 Base 64로 디코딩 및 파싱.
        // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용 해 서명 후, token의 서명 과 비교.
        // 위조되지 않았다면 페이로드(Claims) 리턴
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }
}