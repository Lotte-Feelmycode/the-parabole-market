package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserInfoResponseDto;
import com.feelmycode.parabole.dto.UserSigninDto;
import com.feelmycode.parabole.dto.UserSignupDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.UserService;
import lombok.Getter;
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
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ParaboleResponse> signup(@RequestBody UserSignupDto dto) {

        User newUser = userService.signup(dto);
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED,
            true, "사용자: 회원가입 성공", newUser.getId());
    }

    @PostMapping("/signin")
    public ResponseEntity<ParaboleResponse> signin(@RequestBody UserSigninDto dto) {
        log.info("email: {}, password: {}", dto.getEmail(), dto.getPassword());
        User user = userService.signin(dto);

        String message = "판매자 로그인 성공";
        if (user.sellerIsNull()) {
            message = "사용자 로그인 성공";
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, message, user.getId());
    }

    @GetMapping("/role")
    public ResponseEntity<ParaboleResponse> checkAccountRole(@RequestParam Long userId) {

        if (userService.isSeller(userId)) {
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_SELLER", userService.getSeller(userId).getId());
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_USER", userService.getUser(userId).getId());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ParaboleResponse> getUserInfo(@PathVariable("userId") Long userId) {

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true,
            "마이페이지 사용자 개인정보 정상 출력", userService.getUserInfo(userId));
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getAllNonSellerUsers() {

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자가 아닌 모든 사용자 조회 성공",
            userService.getAllNonSellerUsers());
    }

    @GetMapping("listbyname")
    public ResponseEntity<ParaboleResponse> getNonSellerUsersByName(String name) {

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "검색어를 포함한 username을 가진 사용자 조회 성공",
            userService.getNonSellerUsersByName(name));
    }

}
