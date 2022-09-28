package com.feelmycode.parabole.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponUseState {
    NotUsed("rate", 1),
    Used("amount", 2);

    private final String state;
    private final int value;

}
