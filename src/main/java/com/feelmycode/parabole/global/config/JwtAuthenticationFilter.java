package com.feelmycode.parabole.global.config;

import com.feelmycode.parabole.global.util.JwtUtils;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String token = null;
        try {
            token = parseBearerToken(request);
            // 위에서 받은 token 은 내가 만든 customized Jwt token. Which means they are not authenticated by Spring Security
            log.info("Filter is running...");

            if (token != null && !token.equalsIgnoreCase("null")) {
                log.info("Jwt Token 해독결과 userId {} role {} sellerId {} ",
                    jwtUtils.extractUserId(token), jwtUtils.extractRole(token), jwtUtils.extractSellerId(token));

                Claims claims = jwtUtils.extractAllClaims(token);
                request.setAttribute("userId", claims.get("userId", Long.class));
                request.setAttribute("email", claims.get("email", String.class));
                request.setAttribute("username", claims.get("username", String.class));
                request.setAttribute("nickname", claims.get("nickname", String.class));
                request.setAttribute("phone", claims.get("phone", String.class));
                request.setAttribute("imageUrl", claims.get("imageUrl", String.class));
                request.setAttribute("role", claims.get("role", String.class));
                if (claims.get("role", String.class).equals("ROLE_SELLER")) {
                    request.setAttribute("sellerId", claims.get("sellerId", Long.class));
                    request.setAttribute("sellerStorename", claims.get("sellerStorename", String.class));
                }
                request.setAttribute("authProvider", claims.get("authProvider", String.class));


            }
        } catch (Exception ex) {
            log.error("Could not Extract Claims and set Request", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("parse bearer token : {}", bearerToken);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
