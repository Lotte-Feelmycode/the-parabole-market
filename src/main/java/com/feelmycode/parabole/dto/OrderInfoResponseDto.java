package com.feelmycode.parabole.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoResponseDto {

    private Long id;
    private String state;
    private Long userId;
    private String userEmail;
    private Long productId;
    private String productName;
    private Integer productCnt;
    private Long productRemain;
    private Long productPrice;
    private Long productDiscountPrice;
    private String productThumbnailImg;
    private LocalDateTime updatedAt;

    public OrderInfoResponseDto(Long id, String state, Long userId, String userEmail, Long productId, String productName,
        Integer productCnt, Long productPrice, Long productDiscountPrice, String productThumbnailImg, LocalDateTime productUpdatedAt) {
        this.id = id;
        this.state = state;
        this.userId = userId;
        this.userEmail = userEmail;
        this.productId = productId;
        this.productName = productName;
        this.productCnt = productCnt;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.updatedAt = productUpdatedAt;
        this.productThumbnailImg = productThumbnailImg;
    }
    
    public void setProductThumbnailImg(String productThumbnailImg) {
        this.productThumbnailImg = productThumbnailImg;
    }

    public void setProductRemain(Long productRemain) {
        this.productRemain = productRemain;
    }

}
