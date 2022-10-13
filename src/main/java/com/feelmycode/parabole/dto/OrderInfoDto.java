package com.feelmycode.parabole.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoDto implements Serializable {

//    private Long userId;
    private String state;
    private Integer payState;
    private Long productId;
    private String productName;
    private Integer productCnt;
    private Long productPrice;
    private Long productDiscountPrice;
    private Long productDeliveryFee;
    private Long sellerId;
    private String sellerStoreName;

}
