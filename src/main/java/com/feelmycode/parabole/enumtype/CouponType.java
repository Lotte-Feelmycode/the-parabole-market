package com.feelmycode.parabole.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {

    RATE("rate", 1),
    AMOUNT("amount", 2);

    private final String name;
    private final int value;

}
