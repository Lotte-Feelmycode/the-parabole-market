package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private String productImg;

    public ProductResponseDto(Long productId, String productName, String productImg) {
        this.productId = productId;
        this.productName = productName;
        this.productImg = productImg;
    }

}

