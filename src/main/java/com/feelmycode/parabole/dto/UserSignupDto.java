package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.User;
import lombok.Getter;

@Getter
public class UserSignupDto {

    private String email;
    private String username;
    private String nickname;
    private String phone;
    private String password;
    private String passwordConfirmation;

    public UserSignupDto(String email, String username, String nickname, String phone, String password,
        String passwordConfirmation) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public User toEntity(String email, String username, String nickname, String phone, String password, Cart cart) {
        return new User(email, username, nickname, phone, password, cart);
    }

    // 입력값이 없는 필드가 있으면 blank exists True 반환
    public boolean checkIfBlankExists() {
        if(this.getEmail().equals("") || this.getUsername().equals("") ||
            this.getNickname().equals("") || this.getPassword().equals("") ||
            this.getPasswordConfirmation().equals("")) return true;
        return false;
    }

}
