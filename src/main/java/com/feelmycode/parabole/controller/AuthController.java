package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.security.TokenProvider;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    // Bean으로 작성해도 됨.
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<ParaboleResponse> registerUser(@RequestBody UserDto userDTO) {
        try {
            // 리퀘스트를 이용해 저장할 유저 만들기
            User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getName())
                .nickname(userDTO.getNickname())
                .phone(userDTO.getPhone())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
            // 서비스를 이용해 리파지토리에 유저 저장
            User registeredUser = userService.create(user);
            UserDto responseUserDTO = UserDto.builder()
                .email(registeredUser.getEmail())
                .id(registeredUser.getId())
                .name(registeredUser.getUsername())
                .build();

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
            userDTO.getPassword(),
            passwordEncoder);

        if (user != null) {
            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDto responseUserDTO = UserDto.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .name(user.getUsername())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .build();

            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "기본 로그인 성공",
                responseUserDTO);
        } else {
            return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "기본 로그인 실패");
        }
    }
}
