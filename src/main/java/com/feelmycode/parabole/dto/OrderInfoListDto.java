package com.feelmycode.parabole.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoListDto {

    private Long userId;
    private int state;
    private int payState;
    private Long productId;
    private String productName;
    private int productCnt;
    private Long productPrice;
    private Long productDiscountPrice;
    private Long productDeliveryFee;
    private Long sellerId;
    private String sellerStoreName;

}
