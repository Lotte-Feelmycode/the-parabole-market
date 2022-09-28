package com.feelmycode.parabole.global.error.exception;

import org.springframework.http.HttpStatus;

public class ExampleException extends ParaboleException{

    public ExampleException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public ExampleException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 형식입니다.");
    }
}
