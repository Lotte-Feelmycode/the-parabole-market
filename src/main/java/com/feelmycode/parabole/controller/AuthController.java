package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.dto.UserLoginResponseDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.security.model.GoogleOauthToken;
import com.feelmycode.parabole.security.model.KakaoOauthToken;
import com.feelmycode.parabole.security.model.NaverOauthToken;
import com.feelmycode.parabole.security.utils.JwtUtils;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<ParaboleResponse> registerUser(@RequestBody UserDto userDTO) {
        try {
            User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getName())
                .nickname(userDTO.getNickname())
                .phone(userDTO.getPhone())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .imageUrl("https://ssl.pstatic.net/static/cafe/cafe_pc/default/cafe_profile_77.png")
                .role("ROLE_USER")
                .authProvider("Home")
//                .seller(null)
//                .imageUrl(null)
                .build();

            User newUser = userService.create(user);
            UserDto responseUserDTO = UserDto.builder()
                .id(newUser.getId())
                .name(newUser.getUsername())
                .nickname(newUser.getNickname())
                .build();       // welcome page 위한 부분

            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "기본 회원가입 성공",
                responseUserDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "기본 회원가입 실패");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ParaboleResponse> authenticate(@RequestBody UserDto userDTO) {
        User user = userService.getByCredentials(
            userDTO.getEmail(),
            userDTO.getPassword(), passwordEncoder);

        if (user != null) {
            // 토큰 생성
            String token = jwtUtils.generateToken(user);
            log.info("generated Token {}", token);
            UserLoginResponseDto dto = new UserLoginResponseDto(user, token);
            log.info("기본 로그인 UserLoginResponse {}", dto);

            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "기본 로그인 성공", dto);
        } else {
            return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "기본 로그인 실패");
        }
    }

    @GetMapping(value = "/token/{provider}")
    public ResponseEntity<ParaboleResponse> getAccessToken(@PathVariable(name = "provider") String provider,
        @RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state) {

        log.info(">> {} 서버로부터 받은 code :: {}", provider, code);

        if (provider.equals("google")) {
            GoogleOauthToken googleOauthToken = userService.getAccessTokenGoogle(code);
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "구글 로그인 성공",
                userService.saveUserAndGetTokenGoogle(googleOauthToken.getAccess_token()));
        } else if (provider.equals("naver")) {
            NaverOauthToken naverOauthToken = userService.getAccessTokenNaver(code, state);
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "네이버 로그인 성공",
                userService.saveUserAndGetTokenNaver(naverOauthToken.getAccess_token()));
        } else if (provider.equals("kakao")) {
            // 넘어온 인가 코드를 통해 access_token 발급
            KakaoOauthToken kakaoOauthToken = userService.getAccessTokenKakao(code);
            // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성하고 생성한 JWT 를 dto에 담아서 반환
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "카카오 로그인 성공",
                userService.saveUserAndGetTokenKakao(kakaoOauthToken.getAccess_token()));
        }
        return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "소셜 로그인 실패");
    }

}
