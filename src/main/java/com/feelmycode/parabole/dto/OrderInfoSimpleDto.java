package com.feelmycode.parabole.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoSimpleDto {

    private Long userId;
    private Long productId;
    private String productName;
    private Integer productCnt;
    private Long productPrice;

}
