package com.feelmycode.parabole.global.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ParaboleException extends RuntimeException{

    private final HttpStatus httpStatus;

    public ParaboleException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
