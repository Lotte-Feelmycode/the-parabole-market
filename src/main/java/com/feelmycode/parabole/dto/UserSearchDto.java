package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class UserSearchDto {
    private Long userId;
    private String username;
    private String email;
    private String phone;

    public UserSearchDto(Long userId, String username, String email, String phone) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
}
