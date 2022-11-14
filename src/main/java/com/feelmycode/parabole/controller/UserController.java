package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserToSellerDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.NoSuchAccountException;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @GetMapping("/role")
    public ResponseEntity<ParaboleResponse> checkAccountRole(@RequestParam Long userId) {

        if (userService.isSeller(userId)) {
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_SELLER",
                userService.getSeller(userId).getId());
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "ROLE_USER",
            userService.getUser(userId).getId());
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getNonSellerUsers(
        @RequestParam(required = false) String userName) {
        // 기능: 쿠폰을 사용자에게 배정시 사용
        String getUserName = StringUtil.controllerParamIsBlank(userName) ? "" : userName;

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자가 아닌 사용자 조회 성공",
            userService.getNonSellerUsers(getUserName));
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getUserInfo(@RequestAttribute Long userId) {

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true,
            "사용자 정보 정상 출력", userService.getUserInfo(userId));
    }

    @PostMapping("/toseller")
    public ResponseEntity<ParaboleResponse> changeUserToSeller(@RequestBody UserToSellerDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new NoSuchAccountException());

        Seller seller = new Seller(dto.getStoreName(), dto.getRegistrationNo());
        Seller newSeller = sellerRepository.save(seller);

        user.setSeller(newSeller);
        user.setRole("ROLE_SELLER");
        User changedUser = userRepository.save(user);

        if(newSeller.getId() == changedUser.getSeller().getId() && newSeller.getUser().getId() == changedUser.getId()){
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "사용자 계정을 판매자로 전환 성공",
                newSeller.getId());
        }
        return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST, false, "사용자 계정을 판매자로 전환 실패");
    }
}
