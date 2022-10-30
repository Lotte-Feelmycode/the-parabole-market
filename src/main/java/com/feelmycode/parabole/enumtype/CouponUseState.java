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
        if (value.equals(NotUsed.value))
            return NotUsed.state;
        return Used.state;
    }

    public static Integer returnValueByName(String name) {
        if(name.equals("NOT_USED")) return NotUsed.value;
        return Used.value;
    }

    public static CouponUseState returnNameToValue(Integer value) {
        if(value.equals(NotUsed.value)) {
            return CouponUseState.NotUsed;
        }
        return CouponUseState.Used;
    }

}
