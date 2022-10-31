package com.feelmycode.parabole.security.utils;

import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.security.model.ApplicationOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5";

    public String create(User User) {
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
            Instant.now()
                .plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .setSubject(User.getId().toString()) // sub
            .setIssuer("The Parabole app") // iss
            .setIssuedAt(new Date()) // iat
//            .setExpiration(expiryDate) // exp
            .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public String create(final Authentication authentication) {
        ApplicationOAuth2User userPrincipal = (ApplicationOAuth2User) authentication.getPrincipal();
        Date expiryDate = Date.from(
            Instant.now()
                .plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .setSubject(userPrincipal.getName())        // this means id(Long) in the form of String
            .setIssuedAt(new Date())
//            .setExpiration(expiryDate)
            .compact();
    }
}
