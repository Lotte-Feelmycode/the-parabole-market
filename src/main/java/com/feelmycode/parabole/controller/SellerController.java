package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.SellerDto;
import com.feelmycode.parabole.dto.SellerInfoResponseDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("/info")
    public ResponseEntity<ParaboleResponse> getSellerInfo(@RequestParam("userId") Long userId) {

        Seller seller = sellerService.getSellerByUserId(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "셀러 정보 출력 성공",
            new SellerInfoResponseDto(seller.getUser().getId(), seller.getStoreName(),
                seller.getRegistrationNo()));
    }

    @GetMapping("/storeList")
    public ResponseEntity<ParaboleResponse> getSellerInfo(@RequestParam("storeName") String storeName) {

        Seller seller = sellerService.getSellerByStoreName(storeName);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "셀러 정보 출력 성공",
            new SellerInfoResponseDto(seller.getUser().getId(), seller.getStoreName(),
                seller.getRegistrationNo()));
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getSeller(@RequestParam Long sellerId) {
        SellerDto dto = sellerService.getSellerBySellerId(sellerId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자 정보", dto);
    }

}
