package com.feelmycode.parabole.global.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ParaboleResponse {

    private boolean success;
    private String message;
    private Object data;

    private ParaboleResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    private ParaboleResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ResponseEntity<ParaboleResponse> CommonResponse(HttpStatus httpStatus, boolean success, String message, Object data) {
        return ResponseEntity.status(httpStatus).body(new ParaboleResponse(success, message, data));
    }

    public static ResponseEntity<ParaboleResponse> CommonResponse(HttpStatus httpStatus, boolean success, String message) {
        return ResponseEntity.status(httpStatus).body(new ParaboleResponse(success, message));
    }
}
