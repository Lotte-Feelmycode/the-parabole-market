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
//    BEFORE_REVIEW("BEFORE_REVIEW", 6),
//    REVIEW_COMPLETE("REVIEW_COMPLETE", 7),
    ORDER_CANCEL("ORDER_CANCEL", -1),
    REFUND("REFUND", -2),
    ERROR("ERROR", -99);

    private final String state;
    private final Integer value;

    public static Integer returnValueByName(String state) {
        if (state.equals(REFUND.state)) {
            return -2;
        } else if (state.equals(ORDER_CANCEL.state)) {
            return -1;
        } else if (state.equals(BEFORE_PAY.state)) {
            return 1;
        } else if (state.equals(PAY_COMPLETE.state)) {
            return 2;
        } else if (state.equals(BEFORE_DELIVERY.state)) {
            return 3;
        } else if (state.equals(DELIVERY.state)) {
            return 4;
        } else if (state.equals(DELIVERY_COMPLETE.state)) {
            return 5;
//        } else if (state.equals(BEFORE_REVIEW.state)) {
//            return 6;
//        } else if (state.equals(REVIEW_COMPLETE.state)) {
//            return 7;
        }
        return -99;
    }

    public static String returnNameByValue(Integer value) {
        switch (value) {
            case -2:
                return REFUND.state;
            case -1:
                return ORDER_CANCEL.state;
            case 1:
                return BEFORE_PAY.state;
            case 2:
                return PAY_COMPLETE.state;
            case 3:
                return BEFORE_DELIVERY.state;
            case 4:
                return DELIVERY.state;
            case 5:
                return DELIVERY_COMPLETE.state;
//            case 6:
//                return BEFORE_REVIEW.state;
//            case 7:
//                return REVIEW_COMPLETE.state;
        }
        return ERROR.state;
    }

}
