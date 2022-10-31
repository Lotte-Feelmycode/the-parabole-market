package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/role")
    public ResponseEntity<ParaboleResponse> checkAccountRole(@AuthenticationPrincipal Long userId) {

        if (userService.isSeller(userId)) {
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_SELLER", userService.getSeller(userId).getId());
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_USER", userService.getUser(userId).getId());
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getNonSellerUsers(@RequestParam(required = false) String userName) {

        String getUserName = StringUtil.controllerParamIsBlank(userName) ? "" : userName;

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자가 아닌 사용자 조회 성공",
            userService.getNonSellerUsers(getUserName));
    }

    @GetMapping("/info")
    public ResponseEntity<ParaboleResponse> getUserInfo(@AuthenticationPrincipal Long userId) {

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true,
            "사용자 정보 정상 출력", userService.getUserInfo(userId));
    }
}
