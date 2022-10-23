package com.feelmycode.parabole.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String email;
    private String username;
    private String nickname;
    private String phone;
    private String password;
    private String passwordConfirmation;
    private Long id;
}