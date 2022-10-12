package com.feelmycode.parabole.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderState {

    // 입금전, 주문완료(결제), 배송준비, 배송중, 배송완료,
    // (리뷰 작성 전, 리뷰 작성 완료), 취소, 반품
    BEFORE_PAY("BEFORE_PAY", 1),
    PAY_COMPLETE("PAY_COMPLETE", 2),
    BEFORE_DELIVERY("BEFORE_DELIVERY", 3),
    DELIVERY("DELIVERY", 4),
    DELIVERY_COMPLETE("DELIVERY_COMPLETE", 5),
    BEFORE_REVIEW("BEFORE_REVIEW", 6),
    REVIEW_COMPLETE("REVIEW_COMPLETE", 7),
    ORDER_CANCEL("ORDER_CANCEL", -1),
    REFUND("REFUND", -2),
    ERROR("ERROR", -99);

    private final String state;
    private final Integer value;

    public static Integer returnValueByName(String state) {
        if (state.equals("REFUND")) {
            return -2;
        } else if (state.equals("ORDER_CANCEL")) {
            return -1;
        } else if (state.equals("BEFORE_PAY")) {
            return 1;
        } else if (state.equals("PAY_COMPLETE")) {
            return 2;
        } else if (state.equals("BEFORE_DELIVERY")) {
            return 3;
        } else if (state.equals("DELIVERY")) {
            return 4;
        } else if (state.equals("DELIVERY_COMPLETE")) {
            return 5;
        } else if (state.equals("BEFORE_REVIEW")) {
            return 6;
        } else if (state.equals("REVIEW_COMPLETE")) {
            return 7;
        }
        return -99;
    }

    public static OrderState returnNameByValue(Integer value) {
        switch (value) {
            case -2:
                return OrderState.REFUND;
            case -1:
                return OrderState.ORDER_CANCEL;
            case 1:
                return OrderState.BEFORE_PAY;
            case 2:
                return OrderState.PAY_COMPLETE;
            case 3:
                return OrderState.BEFORE_DELIVERY;
            case 4:
                return OrderState.DELIVERY;
            case 5:
                return OrderState.DELIVERY_COMPLETE;
            case 6:
                return OrderState.BEFORE_REVIEW;
            case 7:
                return OrderState.REVIEW_COMPLETE;
        }
        return OrderState.ERROR;
    }

}
