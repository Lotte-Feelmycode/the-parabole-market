package com.feelmycode.parabole.enumtype;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderState {

    // OrderInfoState와는 달리 전체적인 주문의 배송완료여부를 판단한다.
    BEFORE_PAY("주문 확정 전", -1),
    PAY_COMPLETE("주문 확정", 0),
    DELIVERY_COMPLETE("모든 배송 완료", 1),
    ERROR("에러", -99);

    private final String state;
    private final Integer value;

    public static OrderState returnValueByName(String state) {
        return Arrays.stream(values())
            .filter(orderState -> orderState.state.equals(state))
            .findAny()
            .orElse(ERROR);
    }

    public static String returnNameByValue(Integer value) {
        return Arrays.stream(values())
            .filter(orderState -> orderState.value.toString().equals(value.toString()))
            .map(orderState -> orderState.state)
            .findFirst()
            .orElse("ERROR");
    }

}
