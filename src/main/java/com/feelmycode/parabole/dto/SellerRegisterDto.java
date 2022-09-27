package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class SellerRegisterDto {

    private String storeName;
    private String sellerRegistrationNo;

    public SellerRegisterDto(String storeName, String sellerRegistrationNo) {
        this.storeName = storeName;
        this.sellerRegistrationNo = sellerRegistrationNo;
    }

}
