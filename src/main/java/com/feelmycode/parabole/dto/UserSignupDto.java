package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.User;
import lombok.Getter;

@Getter
public class UserSignupDto {

    public UserSignupDto(String email, String username, String nickname, String password,
        String passwordConfirmation) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    private String email;
    private String username;
    private String nickname;
    private String password;
    private String passwordConfirmation;

    public User toEntity() {
        return new User(email, username, nickname, password);
    }

}
