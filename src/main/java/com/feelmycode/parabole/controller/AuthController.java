package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.KakaoOauthToken;
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.dto.UserLoginResponseDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.NoSuchAccountException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<ParaboleResponse> registerUser(@RequestBody UserDto userDTO) {
        UserDto dto = null;
        try {
            dto = userService.create(userDTO);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "기본 회원가입 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "기본 회원가입 성공", dto);
    }

    @PostMapping("/signin")
    public ResponseEntity<ParaboleResponse> authenticate(@RequestBody UserDto userDTO) {
        UserLoginResponseDto dto = null;
        try {
            dto = userService.getByCredentials(userDTO);
        } catch (Exception e) {
            throw new NoSuchAccountException();
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "기본 로그인 성공", dto);
    }

    @GetMapping(value = "/token/{provider}")
    public ResponseEntity<ParaboleResponse> getAccessToken(@PathVariable(name = "provider") String provider,
        @RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state) {

        log.info(">> {} 서버로부터 받은 code :: {}", provider, code);

        if (provider.equals("kakao")) {
            // 넘어온 인가 코드를 통해 access_token 발급
            KakaoOauthToken kakaoOauthToken = userService.getAccessTokenKakao(code);
            // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성하고 생성한 JWT 를 dto에 담아서 반환
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "카카오 로그인 성공",
                userService.saveUserAndGetTokenKakao(kakaoOauthToken.getAccess_token()));
        }
        return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "소셜 로그인 실패");
    }
}
