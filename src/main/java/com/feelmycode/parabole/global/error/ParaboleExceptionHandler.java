package com.feelmycode.parabole.global.error;

import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ParaboleExceptionHandler extends Exception {

    private final boolean SUCCESS = false;

    @ExceptionHandler(ParaboleException.class)
    protected ResponseEntity<ParaboleResponse> handleGlobalBusinessException(ParaboleException e) {
        String message = e.getMessage();
        HttpStatus httpStatus = e.getHttpStatus();
        return ParaboleResponse.CommonResponse(httpStatus, SUCCESS, message);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ParaboleResponse> handleException(Exception e) {

        String message = e.getMessage();

        return ParaboleResponse.CommonResponse(HttpStatus.INTERNAL_SERVER_ERROR, SUCCESS, message);
    }
}
