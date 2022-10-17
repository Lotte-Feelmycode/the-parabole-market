package com.feelmycode.parabole.enumtype;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderState {

    BEFORE_PAY("주문 확정 전", -1),
    PAY_COMPLETE("주문 확정", 0),
    DELIVERY_COMPLETE("모든 배송 완료", 1);

    private final String state;
    private final Integer value;

    public static Integer returnValueByName(String state) {
        return Arrays.stream(values())
            .filter(orderState -> orderState.state.equals(state))
            .map(orderState -> orderState.value)
            .findFirst()
            .orElse(-99);
    }

    public static String returnNameByValue(Integer value) {
        return Arrays.stream(values())
            .filter(orderState -> orderState.value.toString().equals(value))
            .map(orderState -> orderState.state)
            .findFirst()
            .orElse("ERROR");
    }

}
