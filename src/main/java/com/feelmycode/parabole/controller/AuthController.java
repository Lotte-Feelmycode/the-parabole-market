package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.security.TokenProvider;
import com.feelmycode.parabole.security.UserDTO;
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
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 리퀘스트를 이용해 저장할 유저 만들기
            User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .nickname(userDTO.getNickname())
                .phone(userDTO.getPhone())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
            // 서비스를 이용해 리파지토리에 유저 저장
            User registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                .email(registeredUser.getEmail())
                .id(registeredUser.getId())
                .username(registeredUser.getUsername())
                .build();
            // 유저 정보는 항상 하나이므로 그냥 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "기본 회원가입 성공",
                responseUserDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "기본 회원가입 실패");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        User user = userService.getByCredentials(
            userDTO.getEmail(),
            userDTO.getPassword(),
            passwordEncoder);

        if (user != null) {
            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                .id(user.getId())
                .token(token)
//                .email(user.getUsername())
//                .username(user.getUsername())
//                .nickname(user.getNickname())
//                .phone(user.getPhone())
                .build();

            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "기본 로그인 성공",
                responseUserDTO);
        } else {
            return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "기본 로그인 실패");
        }
    }
}
