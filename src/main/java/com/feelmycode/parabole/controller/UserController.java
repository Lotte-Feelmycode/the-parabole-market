package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.UserSigninDto;
import com.feelmycode.parabole.dto.UserSignupDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_SIZE = 20;

    @PostMapping()
    public ResponseEntity<ParaboleResponse> signup(@RequestBody UserSignupDto dto) {

        userService.signup(dto);
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "회원가입 성공");
    }

    @GetMapping()
    public ResponseEntity<ParaboleResponse> signin(HttpServletRequest request,
                                                @RequestBody UserSigninDto dto) {

        userService.signin(request, dto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "로그인 성공");
    }

}
