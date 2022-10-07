package com.feelmycode.parabole.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Getter
@RequiredArgsConstructor
public enum OrderPayState {

    // 카드결제, 실시간 계좌이체, 휴대폰결제, 가상계좌, 카카오페이, 페이코, 토스, 무통장입금, 네이버페이
    CARD("CARD", 1),
    BANK_TRANSFER("BANK_TRANSFER", 2),
    PHONE("PHONE", 3),
    VIRTUAL_ACCOUNT("VIRTUAL_ACCOUNT", 4),
    KAKAO_PAY("KAKAO_PAY", 5),
    TOSS("TOSS", 6),
    WITHOUT_BANK("WITHOUT_BANK", 7),
    NAVER_PAY("NAVER_PAY", 8),
    ERROR("ERROR", -99);

    private final String state;
    private final Integer value;

    public static Integer returnValueByName(String state) {
        if (state.equals("CARD")) {
            return 1;
        } else if (state.equals("BANK_TRANSFER")) {
            return 2;
        } else if (state.equals("PHONE")) {
            return 3;
        } else if (state.equals("VIRTUAL_ACCOUNT")) {
            return 4;
        } else if (state.equals("KAKAO_PAY")) {
            return 5;
        } else if (state.equals("TOSS")) {
            return 6;
        } else if (state.equals("WITHOUT_BANK")) {
            return 7;
        } else if (state.equals("NAVER_PAY")) {
            return 8;
        }
        return -99;
    }

    public static OrderPayState returnNameByValue(Integer value) {
        switch (value) {
            case 1:
                return OrderPayState.CARD;
            case 2:
                return OrderPayState.BANK_TRANSFER;
            case 3:
                return OrderPayState.PHONE;
            case 4:
                return OrderPayState.VIRTUAL_ACCOUNT;
            case 5:
                return OrderPayState.KAKAO_PAY;
            case 6:
                return OrderPayState.TOSS;
            case 7:
                return OrderPayState.WITHOUT_BANK;
            case 8:
                return OrderPayState.NAVER_PAY;
        }
        return OrderPayState.ERROR;
    }

}
