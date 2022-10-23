package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.User;
import lombok.Getter;

@Getter
public class UserDto {
// NOTICE: 혜원언니가 사용중인 dto같아서 수정하지 않고 인증용 UserDTO 를 만들어서 작업했습니다.
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String phone;

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        name = user.getUsername();
        nickname = user.getNickname();
        phone = user.getPhone();
    }
}
