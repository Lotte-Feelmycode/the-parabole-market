package com.feelmycode.parabole.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoSimpleDto {

    private Long productId;
    private Integer productCnt;

    public OrderInfoSimpleDto(Long productId, Integer productCnt) {
        this.productId = productId;
        this.productCnt = productCnt;
    }

}
