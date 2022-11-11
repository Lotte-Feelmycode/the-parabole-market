package com.feelmycode.parabole.enumtype;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderInfoState {

    // 입금전, 주문완료(결제), 배송준비, 배송중, 배송완료,
    // (리뷰 작성 전, 리뷰 작성 완료), 취소, 반품
    BEFORE_PAY("BEFORE_PAY", 0),
    PAY_COMPLETE("PAY_COMPLETE", 1),
    BEFORE_DELIVERY("BEFORE_DELIVERY", 2),
    DELIVERY("DELIVERY", 3),
    DELIVERY_COMPLETE("DELIVERY_COMPLETE", 4),
//    BEFORE_REVIEW("BEFORE_REVIEW", 5),
//    REVIEW_COMPLETE("REVIEW_COMPLETE", 6),
    BEFORE_ORDER("BEFORE_ORDER", -1),
    ORDER_CANCEL("ORDER_CANCEL", -2),
    REFUND("REFUND", -3),
    ERROR("ERROR", -99);

    private final String state;
    private final Integer value;

    public static OrderInfoState returnValueByName(String state) {
        return Arrays.stream(values())
            .filter(orderInfoState -> orderInfoState.state == state)
            .findAny()
            .orElse(ERROR);
    }

    public static OrderInfoState returnNameByValue(Integer value) {
        return Arrays.stream(values())
            .filter(orderInfoState -> orderInfoState.value.toString().equals(value.toString()))
            .findAny()
            .orElse(ERROR);
    }

}
