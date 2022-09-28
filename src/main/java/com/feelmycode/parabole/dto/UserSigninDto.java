package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class UserSigninDto {

    private String email;
    private String password;

    public UserSigninDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
