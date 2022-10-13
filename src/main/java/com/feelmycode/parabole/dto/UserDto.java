package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.User;
import lombok.Getter;

@Getter
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String password;

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        name = user.getName();
        nickname = user.getNickname();
        phone = user.getPhone();
        password = user.getPassword();
    }
}
