package com.feelmycode.parabole.global.error.exception;

import org.springframework.http.HttpStatus;

public class NoSuchAccountException extends ParaboleException {

    public NoSuchAccountException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public NoSuchAccountException() {
        super(HttpStatus.BAD_REQUEST, "존재하지 않는 계정입니다.");
    }
}
