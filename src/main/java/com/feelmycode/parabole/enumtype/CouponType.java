package com.feelmycode.parabole.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {

    RATE("RATE", 1),
    AMOUNT("AMOUNT", 2);

    private final String name;
    private final int value;

    public static String returnNameByValue(Integer value) {
        if (value == 1)
            return "RATE";
        return "AMOUNT";
    }

    public static Integer returnValueByName(String name) {
        if(name.equals("RATE")) return 1;
        return 2;
    }

    public static CouponType returnNameToValue(Integer value) {
        if(value == 1) return CouponType.RATE;
        return CouponType.AMOUNT;
    }

}
