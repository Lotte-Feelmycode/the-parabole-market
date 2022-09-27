package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class UserSigninDto {

    public UserSigninDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;

}
