package com.feelmycode.parabole.global.error.exception;

import org.springframework.http.HttpStatus;

public class NotSellerException extends ParaboleException {

    public NotSellerException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public NotSellerException() {
        super(HttpStatus.BAD_REQUEST, "해당 사용자는 셀러가 아닙니다.");
    }
}
