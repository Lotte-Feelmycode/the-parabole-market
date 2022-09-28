package com.feelmycode.parabole.enumtype;

public enum CouponType {
    RATE("rate", 1),
    AMOUNT("amount", 2);

    private final String name;
    private final int value;

    CouponType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

//    static CouponType getValue(String x) {
//        if ("rate".equals(x)) { return RATE; }
//        else if ("amount".equals(x)) { return AMOUNT; }
//        else throw new IllegalArgumentException();
//    }
}
