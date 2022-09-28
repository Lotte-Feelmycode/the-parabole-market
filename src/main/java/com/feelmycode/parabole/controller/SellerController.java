package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.SellerRegisterDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.SellerService;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerController {

    private final UserService userService;
    private final SellerService sellerService;

    @PostMapping()
    public ResponseEntity<ParaboleResponse> registerSeller(
        @RequestParam Long userId, @RequestBody SellerRegisterDto sellerDto) {

        Seller newSeller = sellerService.registerSeller(userId, sellerDto);
        if (newSeller == null) {
            userService.deleteUser(userId);
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "판매자 회원가입 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자 회원가입 성공");
    }

}