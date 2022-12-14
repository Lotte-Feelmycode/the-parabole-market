package com.feelmycode.parabole.enumtype;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderPayState {

    // 카드결제, 실시간 계좌이체, 휴대폰결제, 가상계좌, 카카오페이, 페이코, 토스, 무통장입금, 무통장입금 결제완료, 네이버페이
    CARD("CARD", 1),
    BANK_TRANSFER("BANK_TRANSFER", 2),
    PHONE("PHONE", 3),
    VIRTUAL_ACCOUNT("VIRTUAL_ACCOUNT", 4),
    KAKAO_PAY("KAKAO_PAY", 5),
    TOSS("TOSS", 6),
    WITHOUT_BANK("WITHOUT_BANK", 7),
    WITHOUT_BANK_PAY("WITHOUT_BANK_PAY", 8),
    NAVER_PAY("NAVER_PAY", 9),
    ERROR("ERROR", -99);

    private final String state;
    private final Integer value;

    public static OrderPayState returnValueByName(String state) {
        return Arrays.stream(values())
            .filter(orderPayState -> orderPayState.state.equals(state))
            .findAny()
            .orElse(ERROR);
    }

    public static OrderPayState returnNameByValue(Integer value) {
        return Arrays.stream(values())
                .filter(orderPayState -> orderPayState.getValue() == value)
                .findAny()
                .orElse(ERROR);
    }

}
