package com.feelmycode.parabole.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoResponseDto {

    private Long id;
    private String state;
    private String payState;
    private Long userId;
    private String userEmail;
    private Long productId;
    private String productName;
    private int productCnt;
    private Long productRemain;
    private Long productPrice;
    private Long productDiscountPrice;
    private String productThumbnailImg;

    public OrderInfoResponseDto(Long id, String state, String payState, Long userId, String userEmail, Long productId, String productName,
        int productCnt, Long productPrice, Long productDiscountPrice) {
        this.id = id;
        this.state = state;
        this.payState = payState;
        this.userId = userId;
        this.userEmail = userEmail;
        this.productId = productId;
        this.productName = productName;
        this.productCnt = productCnt;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
    }
    
    public void setProductThumbnailImg(String productThumbnailImg) {
        this.productThumbnailImg = productThumbnailImg;
    }

    public void setProductRemain(Long productRemain) {
        this.productRemain = productRemain;
    }

}
