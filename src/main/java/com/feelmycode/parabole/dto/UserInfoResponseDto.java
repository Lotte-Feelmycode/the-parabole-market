package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class UserInfoResponseDto {

    private String email;
    private String username;
    private String nickname;
    private String role;

    private String phone;

    public UserInfoResponseDto(String email, String username, String nickname, String role, String phone) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.phone = phone;
    }
}
