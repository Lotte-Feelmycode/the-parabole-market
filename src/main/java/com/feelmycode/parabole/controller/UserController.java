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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

//    @PostMapping("/signup")
//    public ResponseEntity<ParaboleResponse> signup(@RequestBody UserSignupDto dto) {
//
//        User newUser = userService.signup(dto);
//        return ParaboleResponse.CommonResponse(HttpStatus.CREATED,
//            true, "사용자: 회원가입 성공", newUser.getId());
//    }
//
//    @PostMapping("/signin")
//    public ResponseEntity<ParaboleResponse> signin(@RequestBody UserSigninDto dto) {
//        log.info("email: {}, password: {}", dto.getEmail(), dto.getPassword());
//        User user = userService.signin(dto);
//
//        String message = "판매자 로그인 성공";
//        if (user.sellerIsNull()) {
//            message = "사용자 로그인 성공";
//        }
//        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, message, user.getId());
//    }

    @GetMapping("/role")
    public ResponseEntity<ParaboleResponse> checkAccountRole(@AuthenticationPrincipal Long userId) {

        if (userService.isSeller(userId)) {
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_SELLER", userService.getSeller(userId).getId());
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_USER", userService.getUser(userId).getId());
    }

    @GetMapping("/info")
    public ResponseEntity<ParaboleResponse> getUserInfo(@AuthenticationPrincipal Long userId) {

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true,
            "마이페이지 사용자 개인정보 정상 출력", userService.getUserInfo(userId));
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getNonSellerUsers(@RequestParam(required = false) String userName) {

        String getUserName = StringUtil.controllerParamIsBlank(userName) ? "" : userName;

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자가 아닌 사용자 조회 성공",
            userService.getNonSellerUsers(getUserName));
    }
//      .then((res) => {
//        console.log(res);
//        console.log(res.id);
//        console.log(res.token);
    // for login 완료
    @PostMapping("/welcome")
    public ResponseEntity<ParaboleResponse> getWelcomePage(@AuthenticationPrincipal Long userId) {

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true,
            "마이페이지 사용자 개인정보 정상 출력", userService.getUserInfo(userId));
    }
}
