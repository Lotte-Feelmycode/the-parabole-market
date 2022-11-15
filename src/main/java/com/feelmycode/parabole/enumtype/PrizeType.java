package com.feelmycode.parabole.enumtype;

import lombok.Getter;

@Getter
public enum PrizeType {

    PRODUCT("PRODUCT","상품", 1),
    COUPON("COUPON","쿠폰", 2);

    private String code;
    private String name;
    private Integer value;

    PrizeType(String code, String name, Integer value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }
}
