package com.feelmycode.parabole.enumtype;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderInfoState {

    // 입금전, 주문완료(결제), 배송준비, 배송중, 배송완료,
    // (리뷰 작성 전, 리뷰 작성 완료), 취소, 반품
    BEFORE_PAY("BEFORE_PAY", 1),
    PAY_COMPLETE("PAY_COMPLETE", 2),
    BEFORE_DELIVERY("BEFORE_DELIVERY", 3),
    DELIVERY("DELIVERY", 4),
    DELIVERY_COMPLETE("DELIVERY_COMPLETE", 5),
//    BEFORE_REVIEW("BEFORE_REVIEW", 6),
//    REVIEW_COMPLETE("REVIEW_COMPLETE", 7),
    BEFORE_ORDER("BEFORE_ORDER", -1),
    ORDER_CANCEL("ORDER_CANCEL", -2),
    REFUND("REFUND", -3),
    ERROR("ERROR", -99);

    private final String state;
    private final Integer value;

    public static Integer returnValueByName(String state) {
        return Arrays.stream(values())
            .filter(orderInfoState -> orderInfoState.state.equals(state))
            .map(orderInfoState -> orderInfoState.value)
            .findFirst()
            .orElse(-99);
    }

    public static String returnNameByValue(Integer value) {
        return Arrays.stream(values())
            .filter(orderInfoState -> orderInfoState.value.toString().equals(value))
            .map(orderInfoState -> orderInfoState.state)
            .findFirst()
            .orElse("ERROR");
    }

}
