package com.feelmycode.parabole.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponUseState {
    NotUsed("NOT_USED", 1),
    Used("USED", 2);

    private final String state;
    private final int value;

    public static String returnNameByValue(Integer value) {
        if (value == 1)
            return "NOT_USED";
        return "USED";
    }

    public static Integer returnValueByName(String name) {
        if(name.equals("NOT_USED")) return 1;
        return 2;
    }

    public static CouponUseState returnNameToValue(Integer value) {
        if(value == 1) return CouponUseState.NotUsed;
        return CouponUseState.Used;
    }

}
