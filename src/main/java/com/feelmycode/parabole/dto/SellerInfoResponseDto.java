package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class SellerInfoResponseDto {

    private Long userId;
    private String storeName;
    private String registrationNo;

    public SellerInfoResponseDto(Long userId, String storeName, String registrationNo) {
        this.userId = userId;
        this.storeName = storeName;
        this.registrationNo = registrationNo;
    }
}
