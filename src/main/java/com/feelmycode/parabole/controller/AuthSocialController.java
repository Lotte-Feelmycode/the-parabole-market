package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.security.model.JwtProperties;
import com.feelmycode.parabole.security.model.KakaoOauthToken;
import com.feelmycode.parabole.security.model.NaverOauthToken;
import com.feelmycode.parabole.service.UserService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthSocialController {

    private final UserService userService;

    // 프론트에서 인가코드 받아오는 url
    @GetMapping("/oauth2/code/kakao")
    public HttpServletResponse getKakaoLogin(@RequestParam(required = false) String code, HttpServletResponse httpServletResponse) throws IOException {

        // 넘어온 인가 코드를 통해 access_token 발급
        KakaoOauthToken kakaoOauthToken = userService.getAccessTokenKakao(code);
        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        String jwtToken = userService.saveUserAndGetTokenKakao(kakaoOauthToken.getAccess_token());

        String Front_URL = "http://localhost:3000";

        httpServletResponse.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        httpServletResponse.sendRedirect(Front_URL + "/oauthkakao?token=" + jwtToken);
        return httpServletResponse;
    }

    @GetMapping("/oauth2/code/naver")
    public HttpServletResponse getNaverLogin(@RequestParam(required = false) String code,
        @RequestParam(required = false) String state,
        HttpServletResponse httpServletResponse) throws IOException {

        // 넘어온 인가 코드를 통해 access_token 발급
        NaverOauthToken naverOauthToken = userService.getAccessTokenNaver(code, state);
        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        String jwtToken = userService.saveUserAndGetTokenNaver(naverOauthToken.getAccess_token());

        String Front_URL = "http://localhost:3000";

        httpServletResponse.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        httpServletResponse.sendRedirect(Front_URL + "/oauthnaver?token=" + jwtToken);
        return httpServletResponse;
    }
}
