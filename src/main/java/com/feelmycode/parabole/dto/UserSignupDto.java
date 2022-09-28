package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.User;
import lombok.Getter;

@Getter
public class UserSignupDto {

    private Integer role;       // 1:User    2:Seller    0:Admin
    private String email;
    private String username;
    private String nickname;
    private String password;
    private String passwordConfirmation;

    public UserSignupDto(Integer role, String email, String username, String nickname, String password,
        String passwordConfirmation) {
        this.role = role;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public User toEntity(String email, String username, String nickname, String password) {
        return new User(email, username, nickname, password);
    }

    // 입력값이 없는 필드가 있으면 blank exists True 반환
    public boolean checkIfBlankExists() {
        if(this.getEmail().equals("") || this.getUsername().equals("") ||
            this.getNickname().equals("") || this.getPassword().equals("") ||
            this.getPasswordConfirmation().equals("")) return true;
        return false;
    }

}
