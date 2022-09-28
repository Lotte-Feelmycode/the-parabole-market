package com.feelmycode.parabole.enumtype;

public enum CouponUseState {
    NotUsed("rate", 1),
    Used("amount", 2);

    private final String state;
    private final int value;

    CouponUseState(String state, int value) {
        this.state = state;
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public int getValue() {
        return value;
    }

}
