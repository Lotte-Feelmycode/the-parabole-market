package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String token;
    private String password;
    private String passwordConfirmation;

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        name = user.getUsername();
        nickname = user.getNickname();
        phone = user.getPhone();
    }

    @Builder
    public UserDto(Long id, String email, String name, String nickname, String phone, String token,
        String password, String passwordConfirmation) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.token = token;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
}
